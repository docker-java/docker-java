package com.github.dockerjava.okhttp;

import com.github.dockerjava.transport.DockerHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;

import java.io.IOException;
import java.io.InputStream;

class HijackingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (!response.isSuccessful()) {
            return response;
        }

        DockerHttpClient.Request originalRequest = request.tag(DockerHttpClient.Request.class);

        if (originalRequest == null) {
            // WTF?
            return response;
        }

        InputStream stdin = originalRequest.hijackedInput();

        if (stdin == null) {
            return response;
        }

        chain.call().timeout().clearTimeout().clearDeadline();

        Exchange exchange = ((RealInterceptorChain) chain).exchange();
        RealWebSocket.Streams streams = exchange.newWebSocketStreams();
        Thread thread = new Thread(() -> {
            try (BufferedSink sink = streams.sink) {
                while (sink.isOpen()) {
                    int aByte = stdin.read();
                    if (aByte < 0) {
                        break;
                    }
                    sink.writeByte(aByte);
                    sink.emit();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        thread.setName("okhttp-hijack-streaming-" + System.identityHashCode(request));
        thread.setDaemon(true);
        thread.start();
        return response;
    }
}
