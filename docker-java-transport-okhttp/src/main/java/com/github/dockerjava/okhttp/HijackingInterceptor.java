package com.github.dockerjava.okhttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

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
        Hijacked hijacked = request.tag(Hijacked.class);

        if (hijacked == null) {
            return response;
        }

        InputStream inputStream = hijacked.getInputStream();

        if (inputStream == null) {
            return response;
        }

        RealWebSocket.Streams streams = Internal.instance.exchange(response).newWebSocketStreams();
        Thread thread = new Thread(() -> {
            try (
                    BufferedSink sink = streams.sink;
                    Source source = Okio.source(inputStream);
            ) {
                while (sink.isOpen()) {
                    int available = inputStream.available();
                    if (available > 0) {
                        sink.write(source, available);
                        sink.emit();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        thread.setName("docker-java-hijack-" + System.identityHashCode(request));
        thread.start();
        return response;
    }
}
