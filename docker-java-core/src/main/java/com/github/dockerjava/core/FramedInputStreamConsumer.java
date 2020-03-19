package com.github.dockerjava.core;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Consumer;

class FramedInputStreamConsumer implements Consumer<DockerHttpClient.Response> {

    private static final int HEADER_SIZE = 8;

    private final ResultCallback<Frame> resultCallback;

    FramedInputStreamConsumer(ResultCallback<Frame> resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    public void accept(DockerHttpClient.Response response) {
        try {
            InputStream body = response.getBody();
            while (true) {
                // See https://docs.docker.com/engine/api/v1.37/#operation/ContainerAttach
                // [8]byte{STREAM_TYPE, 0, 0, 0, SIZE1, SIZE2, SIZE3, SIZE4}[]byte{OUTPUT}

                byte[] header = new byte[HEADER_SIZE];
                if (body.read(header) < 0) {
                    // TODO log?
                    return;
                }

                int streamTypeByte = header[0];

                StreamType streamType = streamType(streamTypeByte);

                byte[] buffer = new byte[1024];

                if (streamType == StreamType.RAW) {
                    // FIXME should check the header instead
                    resultCallback.onNext(new Frame(StreamType.RAW, header));

                    int readBytes;
                    while ((readBytes = body.read(buffer)) >= 0) {
                        resultCallback.onNext(new Frame(StreamType.RAW, Arrays.copyOf(buffer, readBytes)));
                    }
                    return;
                }

                int bytesToRead = ByteBuffer.wrap(header, 4, 4).getInt();

                do {
                    int readBytes = body.read(buffer);
                    if (readBytes < 0) {
                        // TODO log?
                        return;
                    }

                    if (readBytes == buffer.length) {
                        resultCallback.onNext(new Frame(streamType, buffer));
                    } else {
                        resultCallback.onNext(new Frame(streamType, Arrays.copyOf(buffer, readBytes)));
                    }
                    bytesToRead -= buffer.length;
                } while (bytesToRead > 0);
            }
        } catch (Exception e) {
            resultCallback.onError(e);
        }
    }

    private static StreamType streamType(int streamType) {
        switch (streamType) {
            case 0:
                return StreamType.STDIN;
            case 1:
                return StreamType.STDOUT;
            case 2:
                return StreamType.STDERR;
            default:
                return StreamType.RAW;
        }
    }
}
