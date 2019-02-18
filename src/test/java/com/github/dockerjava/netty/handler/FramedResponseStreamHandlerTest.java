package com.github.dockerjava.netty.handler;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.netty.handler.FramedResponseStreamHandler;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FramedResponseStreamHandlerTest {

  public class MockedResponseHandler implements ResultCallback<Frame> {

    public List<Frame> frames = new ArrayList<Frame>();
    public List<Throwable> exceptions = new ArrayList<Throwable>();

    @Override
    public void close() {
    }

    @Override
    public void onStart(Closeable closeable) {
    }

    @Override
    public void onNext(Frame object) {
      frames.add(object);
    }

    @Override
    public void onError(Throwable throwable) {
      exceptions.add(throwable);
    }

    @Override
    public void onComplete() {
    }
  }


  @Test
  public void channelRead0emptyHeaderCount() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {};

    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertTrue(responseHandler.frames.isEmpty());
  }

  @Test
  public void channelRead0headerTooSmall() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {0};

    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertTrue(responseHandler.frames.isEmpty());
  }

  @Test
  public void channelRead0rawStream() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {3, 0, 0, 0, 0, 0, 0, 0, 0};

    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertEquals(responseHandler.frames.get(0).toString(), "RAW: ");
  }

  @Test
  public void channelRead0emptyNonRaw() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    
    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertTrue(responseHandler.frames.isEmpty());
  }

  @Test
  public void channelRead0stdIn() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {0, 0, 0, 0, 0, 0, 0, 1, 0};
    
    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertEquals(responseHandler.frames.get(0).toString(), "STDIN: ");
  }

  @Test
  public void channelRead0stdOut() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {1, 0, 0, 0, 0, 0, 0, 1, 0};
    
    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertEquals(responseHandler.frames.get(0).toString(), "STDOUT: ");
  }

  @Test
  public void channelRead0stdErr() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {2, 0, 0, 0, 0, 0, 0, 1, 0};
    
    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertEquals(responseHandler.frames.get(0).toString(), "STDERR: ");
  }

  @Test
  public void channelRead0largePayload() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    byte[] msg = {1, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0}; 
    
    // Act
    objectUnderTest.channelRead0(Mockito.mock(ChannelHandlerContext.class), Unpooled.wrappedBuffer(msg));

    // Assert result
    assertEquals(responseHandler.frames.get(0).toString(), "STDOUT: ");
  }

  @Test
  public void exceptionCaught() throws Exception {

    // Arrange
    final MockedResponseHandler responseHandler = new MockedResponseHandler();
    final FramedResponseStreamHandler objectUnderTest = new FramedResponseStreamHandler(responseHandler);
    final Exception exception = new Exception();
    final Throwable throwable = new Throwable();
    throwable.initCause(exception);
    
    // Act
    objectUnderTest.exceptionCaught(Mockito.mock(ChannelHandlerContext.class), throwable);

    // Assert result
    assertEquals(responseHandler.exceptions.get(0).getCause(), exception);
  }
}
