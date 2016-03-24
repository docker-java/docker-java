package com.github.dockerjava.core.command;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

public class FrameReaderTest {
    public static final int HEADER_SIZE = 8;

    private final List<Integer> bytes = new ArrayList<>();

    private final InputStream inputStream = new InputStream() {
        @Override
        public int read() throws IOException {
            return bytes.isEmpty() ? -1 : bytes.remove(0);
        }
    };

    private final FrameReader frameReader = new FrameReader(inputStream);

    @Test
    public void endOfStreamReturnsNull() throws Exception {
        assertNull(nextFrame());
    }

    @Test
    public void stdInBytesFrameReturnsFrame() throws Exception {
        assertEquals(nextFrame(0, 0, 0, 0, 0, 0, 0, 0), new Frame(StreamType.STDIN, new byte[0]));
    }

    private Frame nextFrame(int... bytes) throws IOException {
        setBytes(bytes);
        return frameReader.readFrame();
    }

    @Test
    public void stdOutBytesFrameReturnsFrame() throws Exception {
        assertEquals(nextFrame(1, 0, 0, 0, 0, 0, 0, 0), new Frame(StreamType.STDOUT, new byte[0]));
    }

    @Test
    public void stdErrBytesFrameReturnsFrame() throws Exception {
        assertEquals(nextFrame(2, 0, 0, 0, 0, 0, 0, 0), new Frame(StreamType.STDERR, new byte[0]));
    }

    private void setBytes(int... bytes) {
        this.bytes.clear();
        for (int aByte : bytes) {
            this.bytes.add(aByte);
        }
    }
}
