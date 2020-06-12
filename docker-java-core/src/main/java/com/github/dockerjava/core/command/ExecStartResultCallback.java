package com.github.dockerjava.core.command;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Marcus Linke
 *
 * @deprecated use {@link com.github.dockerjava.api.async.ResultCallback.Adapter}
 */
@Deprecated
public class ExecStartResultCallback extends ResultCallbackTemplate<ExecStartResultCallback, Frame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecStartResultCallback.class);

    private OutputStream stdout, stderr;

    public ExecStartResultCallback(OutputStream stdout, OutputStream stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public ExecStartResultCallback() {
        this(null, null);
    }

    @Override
    public void onNext(Frame frame) {
        if (frame != null) {
            try {
                switch (frame.getStreamType()) {
                    case STDOUT:
                    case RAW:
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

            LOGGER.debug(frame.toString());
        }
    }
}
