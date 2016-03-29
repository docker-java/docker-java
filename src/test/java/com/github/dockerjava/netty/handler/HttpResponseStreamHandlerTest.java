package com.github.dockerjava.netty.handler;

import static org.testng.Assert.assertTrue;

import java.io.InputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
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
        streamHandler.channelReadComplete(ctx);

        assertTrue(IOUtils.contentEquals(callback.getInputStream(), new ByteBufInputStream(buffer)));
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

        public InputStream getInputStream() {
            return stream;
        }
    }
}
