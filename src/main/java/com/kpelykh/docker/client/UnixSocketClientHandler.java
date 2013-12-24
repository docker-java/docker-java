package com.kpelykh.docker.client;

import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.core.util.ReaderWriter;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpRequestWriter;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.BasicLineParser;
import org.apache.http.params.BasicHttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.net.URI;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Make thread-safe.
 */
@NotThreadSafe
public class UnixSocketClientHandler extends RequestWriter implements ClientHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnixSocketClientHandler.class);

  public static final int BUFFERSIZE = 1024;
  public static final String DOCKER_SOCKET_PATH = "/var/run/docker.sock";

  @Override
  public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
    try {
      File path = new File(DOCKER_SOCKET_PATH);
      UnixSocketAddress address = new UnixSocketAddress(path);
      UnixSocketChannel channel = UnixSocketChannel.open(address);
      OutputStream unixSocketChannelOutputStream = Channels.newOutputStream(channel);

      final HttpUriRequest request = getUriHttpRequest(cr);
      BasicHttpParams params = new BasicHttpParams();

      UnixSocketSessionOutputBuffer outputBuffer = new UnixSocketSessionOutputBuffer();
      outputBuffer.init(unixSocketChannelOutputStream, BUFFERSIZE, params);
      HttpRequestWriter writer = new HttpRequestWriter(outputBuffer, new BasicLineFormatter(), params);
      writer.write(request);
      outputBuffer.flush();

      UnixSocketSessionInputBuffer inputBuffer = new UnixSocketSessionInputBuffer();
      inputBuffer.init(Channels.newInputStream(channel), BUFFERSIZE, params);

      HttpResponse response = new DefaultHttpResponseParser(inputBuffer, new BasicLineParser(), new DefaultHttpResponseFactory(), params).parse();
      LOGGER.trace(response.toString());

      ClientResponse clientResponse = new ClientResponse(response.getStatusLine().getStatusCode(),
        getInBoundHeaders(response),
        new HttpClientResponseInputStream(response),
        getMessageBodyWorkers());

      clientResponse.bufferEntity();
      clientResponse.close();

      return clientResponse;

    } catch (IOException e) {
      e.printStackTrace();
    } catch (HttpException e) {
      e.printStackTrace();
    }
    return null;
  }

  private HttpUriRequest getUriHttpRequest(final ClientRequest cr) {
    final String strMethod = cr.getMethod();
    final URI uri = cr.getURI();

    final HttpEntity entity = getHttpEntity(cr);
    final HttpUriRequest request;

    if (strMethod.equals("GET")) {
      request = new HttpGet(uri);
    } else if (strMethod.equals("POST")) {
      request = new HttpPost(uri);
    } else if (strMethod.equals("PUT")) {
      request = new HttpPut(uri);
    } else if (strMethod.equals("DELETE")) {
      request = new HttpDelete(uri);
    } else if (strMethod.equals("HEAD")) {
      request = new HttpHead(uri);
    } else if (strMethod.equals("OPTIONS")) {
      request = new HttpOptions(uri);
    } else {
      request = new HttpEntityEnclosingRequestBase() {
        @Override
        public String getMethod() {
          return strMethod;
        }

        @Override
        public URI getURI() {
          return uri;
        }
      };
    }

    if (entity != null && request instanceof HttpEntityEnclosingRequestBase) {
      ((HttpEntityEnclosingRequestBase) request).setEntity(entity);
    } else if (entity != null) {
      throw new ClientHandlerException("Adding entity to http method " + cr.getMethod() + " is not supported.");
    }

    return request;
  }

  private HttpEntity getHttpEntity(final ClientRequest cr) {
    final Object entity = cr.getEntity();

    if (entity == null)
      return null;

    final RequestEntityWriter requestEntityWriter = getRequestEntityWriter(cr);

    try {
      HttpEntity httpEntity = new AbstractHttpEntity() {
        @Override
        public boolean isRepeatable() {
          return false;
        }

        @Override
        public long getContentLength() {
          return requestEntityWriter.getSize();
        }

        @Override
        public InputStream getContent() throws IOException, IllegalStateException {
          return null;
        }

        @Override
        public void writeTo(OutputStream outputStream) throws IOException {
          requestEntityWriter.writeRequestEntity(outputStream);
        }

        @Override
        public boolean isStreaming() {
          return false;
        }
      };

      if (cr.getProperties().get(ClientConfig.PROPERTY_CHUNKED_ENCODING_SIZE) != null) {
        // TODO return InputStreamEntity
        return httpEntity;
      } else {
        return new BufferedHttpEntity(httpEntity);
      }
    } catch (Exception ex) {
      // TODO warning/error?
    }

    return null;
  }

  private InBoundHeaders getInBoundHeaders(final HttpResponse response) {
    final InBoundHeaders headers = new InBoundHeaders();
    final Header[] respHeaders = response.getAllHeaders();
    for (Header header : respHeaders) {
      List<String> list = headers.get(header.getName());
      if (list == null) {
        list = new ArrayList<String>();
      }
      list.add(header.getValue());
      headers.put(header.getName(), list);
    }
    return headers;
  }

  private static final class HttpClientResponseInputStream extends FilterInputStream {

    HttpClientResponseInputStream(final HttpResponse response) throws IOException {
      super(getInputStream(response));
    }

    @Override
    public void close()
      throws IOException {
      super.close();
    }
  }

  private static InputStream getInputStream(final HttpResponse response) throws IOException {

    if (response.getEntity() == null) {
      return new ByteArrayInputStream(new byte[0]);
    } else {
      final InputStream i = response.getEntity().getContent();
      if (i.markSupported())
        return i;
      return new BufferedInputStream(i, ReaderWriter.BUFFER_SIZE);
    }
  }
}
