/*
 * Created on 23.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.FrameReader;

/**
 *
 *
 * @author Marcus Linke
 *
 */
@SuppressWarnings("unused")
@Deprecated
public class FrameStreamProcessor implements ResponseStreamProcessor<Frame> {

    @Override
    public void processResponseStream(InputStream response, ResultCallback<Frame> resultCallback) {
        resultCallback.onStart(response);

        FrameReader frameReader = new FrameReader(response);
        try {

            Frame frame = frameReader.readFrame();
            while (frame != null) {
                try {
                    resultCallback.onNext(frame);
                } catch (Exception e) {
                    resultCallback.onError(e);
                } finally {
                    frame = frameReader.readFrame();
                }
            }
        } catch (Throwable t) {
            resultCallback.onError(t);
        } finally {
            try {
                frameReader.close();
                response.close();
            } catch (IOException e) {
                resultCallback.onError(e);
            } finally {
                resultCallback.onComplete();
            }
        }
    }
}
