package com.github.dockerjava.httpclient5;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ConnectionReuseStrategy;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.ProtocolVersion;
import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
import org.apache.hc.core5.http.io.HttpClientConnection;
import org.apache.hc.core5.http.io.HttpResponseInformationCallback;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.protocol.HttpCoreContext;
import org.apache.hc.core5.io.Closer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

class HijackingHttpRequestExecutor extends HttpRequestExecutor {

    static final String HIJACKED_INPUT_ATTRIBUTE = "com.github.docker-java.hijackedInput";

    HijackingHttpRequestExecutor(ConnectionReuseStrategy connectionReuseStrategy) {
        super(connectionReuseStrategy);
    }

    @Override
    public ClassicHttpResponse execute(
        ClassicHttpRequest request,
        HttpClientConnection conn,
        HttpResponseInformationCallback informationCallback,
        HttpContext context
    ) throws IOException, HttpException {
        Objects.requireNonNull(request, "HTTP request");
        Objects.requireNonNull(conn, "Client connection");
        Objects.requireNonNull(context, "HTTP context");

        InputStream hijackedInput = (InputStream) context.getAttribute(HIJACKED_INPUT_ATTRIBUTE);
        if (hijackedInput != null) {
            return executeHijacked(request, conn, context, hijackedInput);
        }

        return super.execute(request, conn, informationCallback, context);
    }

    private ClassicHttpResponse executeHijacked(
        ClassicHttpRequest request,
        HttpClientConnection conn,
        HttpContext context,
        InputStream hijackedInput
    ) throws HttpException, IOException {
        try {
            context.setAttribute(HttpCoreContext.SSL_SESSION, conn.getSSLSession());
            context.setAttribute(HttpCoreContext.CONNECTION_ENDPOINT, conn.getEndpointDetails());
            final ProtocolVersion transportVersion = request.getVersion();
            if (transportVersion != null) {
                context.setProtocolVersion(transportVersion);
            }

            conn.sendRequestHeader(request);
            conn.sendRequestEntity(request);
            conn.flush();

            ClassicHttpResponse response = conn.receiveResponseHeader();
            if (response.getCode() != HttpStatus.SC_SWITCHING_PROTOCOLS) {
                conn.terminateRequest(request);
                throw new ProtocolException("Expected 101 Switching Protocols, got: " + new StatusLine(response));
            }

            Thread thread = new Thread(() -> {
                try {
                    BasicClassicHttpRequest fakeRequest = new BasicClassicHttpRequest("POST", "/");
                    fakeRequest.setHeader(HttpHeaders.CONTENT_LENGTH, Long.MAX_VALUE);
                    fakeRequest.setEntity(new HijackedEntity(hijackedInput));
                    conn.sendRequestEntity(fakeRequest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setName("docker-java-httpclient5-hijacking-stream-" + System.identityHashCode(request));
            thread.setDaemon(true);
            thread.start();

            // 101 -> 200
            response.setCode(200);
            conn.receiveResponseEntity(response);
            return response;

        } catch (final HttpException | IOException | RuntimeException ex) {
            Closer.closeQuietly(conn);
            throw ex;
        }
    }

    private static class HijackedEntity extends AbstractHttpEntity {

        private final InputStream inStream;

        HijackedEntity(InputStream inStream) {
            super((String) null, null, false);
            this.inStream = inStream;
        }

        @Override
        public void writeTo(OutputStream outStream) throws IOException {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, read);
                outStream.flush();
            }
        }

        @Override
        public InputStream getContent() {
            return inStream;
        }

        @Override
        public boolean isStreaming() {
            return true;
        }

        @Override
        public boolean isRepeatable() {
            return false;
        }

        @Override
        public void close() throws IOException {
            inStream.close();
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }
}
