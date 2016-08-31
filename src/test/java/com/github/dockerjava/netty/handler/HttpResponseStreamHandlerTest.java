package com.github.dockerjava.netty.handler;

import static com.github.dockerjava.netty.handler.HttpResponseStreamHandler.HttpResponseInputStream;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.github.dockerjava.core.async.ResultCallbackTemplate;

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
        streamHandler.channelRead0(ctx, buffer);
        streamHandler.channelInactive(ctx);

        try (InputStream inputStream = callback.getInputStream()) {
            assertTrue(IOUtils.contentEquals(inputStream, new ByteBufInputStream(buffer)));
        }
    }

    @Test
    public void testReadByteByByte() throws Exception {
        ResultCallbackTest callback = new ResultCallbackTest();
        HttpResponseStreamHandler streamHandler = new HttpResponseStreamHandler(callback);
        ChannelHandlerContext ctx = Mockito.mock(ChannelHandlerContext.class);
        ByteBuf buffer = generateByteBuf();
        streamHandler.channelRead0(ctx, buffer);
        streamHandler.channelInactive(ctx);

        try (InputStream inputStream = callback.getInputStream()) {
            for (int i = 0; i < buffer.readableBytes(); i++) {
                int b = inputStream.read();
                assertEquals(b, buffer.getByte(i));
            }
            assertTrue(inputStream.read() == -1);
        }
    }

    @Test(expectedExceptions = IOException.class)
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
