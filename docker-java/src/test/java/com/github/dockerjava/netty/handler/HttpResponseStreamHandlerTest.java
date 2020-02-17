package com.github.dockerjava.netty.handler;

import com.github.dockerjava.core.async.ResultCallbackTemplate;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.github.dockerjava.netty.handler.HttpResponseStreamHandler.HttpResponseInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Koshevoy
 */
public class HttpResponseStreamHandlerTest {
    @Test
    public void testNoBytesSkipped() throws Exception {
        ResultCallbackTest callback = new ResultCallbackTest();
        HttpResponseStreamHandler streamHandler = new HttpResponseStreamHandler(callback);
        ChannelHandlerContext ctx = Mockito.mock(ChannelHandlerContext.class);
        ByteBuf buffer = generateByteBuf();
        ByteBuf readBuffer = buffer.copy();
        assertEquals(buffer.refCnt(), 1);
        streamHandler.channelRead(ctx, buffer);
        streamHandler.channelInactive(ctx);
        assertEquals(buffer.refCnt(), 0);
        try (InputStream inputStream = callback.getInputStream()) {
            assertTrue(IOUtils.contentEquals(inputStream, new ByteBufInputStream(readBuffer)));
        }
        ReferenceCountUtil.release(readBuffer);
    }

    @Test
    public void testReadByteByByte() throws Exception {
        ResultCallbackTest callback = new ResultCallbackTest();
        HttpResponseStreamHandler streamHandler = new HttpResponseStreamHandler(callback);
        ChannelHandlerContext ctx = Mockito.mock(ChannelHandlerContext.class);
        ByteBuf buffer = generateByteBuf();
        ByteBuf readBuffer = buffer.copy();
        assertEquals(buffer.refCnt(), 1);
        streamHandler.channelRead(ctx, buffer);
        streamHandler.channelInactive(ctx);
        assertEquals(buffer.refCnt(), 0);
        try (InputStream inputStream = callback.getInputStream()) {
            for (int i = 0; i < readBuffer.readableBytes(); i++) {
                int b = inputStream.read();
                assertEquals(b, readBuffer.getByte(i));
            }
            assertTrue(inputStream.read() == -1);
        }
        ReferenceCountUtil.release(readBuffer);
    }

    @Test
    public void testCloseResponseStreamBeforeWrite() throws Exception {
        HttpResponseInputStream inputStream = new HttpResponseInputStream();
        ByteBuf buffer = generateByteBuf();

        inputStream.write(buffer);
        inputStream.close();
        inputStream.write(buffer);
    }

    @Test
    public void testCloseResponseStreamOnWrite() throws Exception {
        final HttpResponseInputStream inputStream = new HttpResponseInputStream();

        final ByteBuf buffer = generateByteBuf();

        final CountDownLatch firstWrite = new CountDownLatch(1);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> submit = executor.submit(() -> {
            try {
                inputStream.write(buffer);
                firstWrite.countDown();
                inputStream.write(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        firstWrite.await();
        assertTrue(inputStream.read() != -1);

        // second write should have started
        Thread.sleep(500L);
        inputStream.close();

        submit.get();
    }

    @Test(expected = IOException.class)
    public void testReadClosedResponseStream() throws Exception {
        HttpResponseInputStream inputStream = new HttpResponseInputStream();
        ByteBuf buffer = generateByteBuf();

        inputStream.write(buffer);
        inputStream.close();
        inputStream.read();
    }

    private ByteBuf generateByteBuf() {
        byte[] array = new byte[256];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) i;
        }
        return Unpooled.copiedBuffer(array);
    }

    private static class ResultCallbackTest extends ResultCallbackTemplate<ResultCallbackTest, InputStream> {
        private InputStream stream;

        @Override
        public void onNext(InputStream stream) {
            this.stream = stream;
        }

        private InputStream getInputStream() {
            return stream;
        }
    }
}
