package com.github.dockerjava.core.command;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

/**
 *
 * @author Marcus Linke
 *
 */
public class StartExecResultCallback extends ResultCallbackTemplate<StartExecResultCallback, Frame> {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartExecResultCallback.class);

    private OutputStream stdout, stderr;

    public StartExecResultCallback(OutputStream stdout, OutputStream stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public StartExecResultCallback() {
        this(null, null);
    }

    @Override
    public void onNext(Frame frame) {
        if (frame != null) {
            try {
                switch (frame.getStreamType()) {
                case STDOUT:
                    if (stdout != null) {
                        stdout.write(frame.getPayload());
                        stdout.flush();
                    }
                    break;
                case STDERR:
                    if (stderr != null) {
                        stderr.write(frame.getPayload());
                        stderr.flush();
                    }
                    break;
                default:
                    LOGGER.error("unknown stream type:" + frame.getStreamType());
                }
            } catch (IOException e) {
                onError(e);
            }

        }
        LOGGER.debug(frame.toString());
    }
}
