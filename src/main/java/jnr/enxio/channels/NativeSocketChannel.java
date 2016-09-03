/*
 * Copyright (C) 2008 Wayne Meissner
 *
 * This file is part of the JNR project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jnr.enxio.channels;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

import jnr.constants.platform.Errno;
import jnr.constants.platform.Shutdown;

public abstract class NativeSocketChannel extends SocketChannel implements ByteChannel, NativeSelectableChannel {

    private int fd = -1;

    public NativeSocketChannel(int fd) {
        this(NativeSelectorProvider.getInstance(), fd);
    }

    public NativeSocketChannel() {
        super(NativeSelectorProvider.getInstance());
    }

    NativeSocketChannel(SelectorProvider provider, int fd) {
        super(provider);
        this.fd = fd;
    }

    public void setFD(int fd) {
        this.fd = fd;
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        Native.close(fd);
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        Native.setBlocking(fd, block);
    }

    public final int getFD() {
        return fd;
    }

    public int read(ByteBuffer dst) throws IOException {
        // System.out.println("dst.remaining: " + dst.remaining());
        // System.out.println("dst.limit: " + dst.limit());

        ByteBuffer buffer = ByteBuffer.allocate(dst.remaining());

        int n = Native.read(fd, buffer);
        System.out.println("n: " + n);

        dst.put(buffer.array());

        switch (n) {
        case 0:
            return -1;

        case -1:
            Errno lastError = Native.getLastError();
            switch (lastError) {
            case EAGAIN:
            case EWOULDBLOCK:
                return 0;

            default:
                throw new IOException(Native.getLastErrorString());
            }

        default: {

            return n;
        }
        }
    }

    public int write(ByteBuffer src) throws IOException {

        System.out.println("write: " + hexDump(src, ""));

        int n = Native.write(fd, src);
        if (n < 0) {
            throw new IOException(Native.getLastErrorString());
        }

        return n;
    }

    public SocketChannel shutdownInput() throws IOException {
        System.out.println("shutdownInput");
        int n = Native.shutdown(fd, SHUT_RD);
        if (n < 0) {
            throw new IOException(Native.getLastErrorString());
        }
        return this;
    }

    public SocketChannel shutdownOutput() throws IOException {
        System.out.println("shutdownOutput");
        int n = Native.shutdown(fd, SHUT_WR);
        if (n < 0) {
            throw new IOException(Native.getLastErrorString());
        }
        return this;
    }

    private static final int SHUT_RD = Shutdown.SHUT_RD.intValue();
    private static final int SHUT_WR = Shutdown.SHUT_WR.intValue();

    public static String hexDump(ByteBuffer buf, String prefix) {
      buf = buf.duplicate();
      StringWriter str = new StringWriter();
      PrintWriter out = new PrintWriter(str);
      int i = 0;
      int len = buf.remaining();
      byte[] line = new byte[16];
      while (i < len) {
          if (prefix != null) {
            out.print(prefix);
          }
          out.print(formatInt(i, 16, 8));
          out.print("  ");
          int l = Math.min(16, len - i);
          buf.get(line, 0, l);
          String s = toHexString(line, 0, l, ' ');
          out.print(s);
          for (int j = s.length(); j < 49; j++) {
            out.print(' ');
          }
          for (int j = 0; j < l; j++) {
              int c = line[j] & 0xFF;
              if (c < 0x20 || c > 0x7E) {
                out.print('.');
              } else {
                out.print((char) c);
              }
            }
          out.println();
          i += 16;
        }
      return str.toString();
    }

    public static String formatInt(int i, int radix, int len) {
      String s = Integer.toString(i, radix);
      StringBuffer buf = new StringBuffer();
      for (int j = 0; j < len - s.length(); j++) {
        buf.append("0");
      }
      buf.append(s);
      return buf.toString();
    }

    public static String toHexString(byte[] buf, int off, int len, char sep) {
      StringBuffer str = new StringBuffer();
      for (int i = 0; i < len; i++) {
          str.append(HEX.charAt(buf[i + off] >>> 4 & 0x0F));
          str.append(HEX.charAt(buf[i + off] & 0x0F));
          if (i < len - 1) {
            str.append(sep);
          }
        }
      return str.toString();
    }

    static final String HEX = "0123456789abcdef";


}
