package com.github.dockerjava.okhttp;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import okio.BufferedSource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Consumer;

class FramedSink implements Consumer<BufferedSource> {

    private static final int HEADER_SIZE = 8;

    private final ResultCallback<Frame> resultCallback;

    FramedSink(ResultCallback<Frame> resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    public void accept(BufferedSource source) {
        try {
            while (true) {
                try {
                    if (source.exhausted()) {
                        break;
                    }
                } catch (IOException e) {
                    break;
                }
                // See https://docs.docker.com/engine/api/v1.37/#operation/ContainerAttach
                // [8]byte{STREAM_TYPE, 0, 0, 0, SIZE1, SIZE2, SIZE3, SIZE4}[]byte{OUTPUT}

                if (!source.request(HEADER_SIZE)) {
                    return;
                }
                byte[] bytes = source.readByteArray(HEADER_SIZE);

                StreamType streamType = streamType(bytes[0]);

                if (streamType == StreamType.RAW) {
                    resultCallback.onNext(new Frame(StreamType.RAW, bytes));
                    byte[] buffer = new byte[1024];
                    while (!source.exhausted()) {
                        int readBytes = source.read(buffer);
                        if (readBytes != -1) {
                            resultCallback.onNext(new Frame(StreamType.RAW, Arrays.copyOf(buffer, readBytes)));
                        }
                    }
                    return;
                }

                int payloadSize = ByteBuffer.wrap(bytes, 4, 4).getInt();
                if (!source.request(payloadSize)) {
                    return;
                }
                byte[] payload = source.readByteArray(payloadSize);

                resultCallback.onNext(new Frame(streamType, payload));
            }
        } catch (Exception e) {
            resultCallback.onError(e);
        }
    }

    private static StreamType streamType(byte streamType) {
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
