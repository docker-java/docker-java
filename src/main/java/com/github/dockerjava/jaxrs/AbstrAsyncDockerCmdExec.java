package com.github.dockerjava.jaxrs;

import java.io.Closeable;
import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;

public abstract class AbstrAsyncDockerCmdExec<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T, RES_T>, A_RES_T, RES_T>
        extends AbstrDockerCmdExec<CMD_T, RES_T> {

    public AbstrAsyncDockerCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    public final RES_T exec(CMD_T command) {
        // this hack works because of ResponseStatusExceptionFilter
        RES_T result;
        try {
            result = execute(command);

        } catch (ProcessingException e) {
            if (e.getCause() instanceof DockerException) {
                throw (DockerException) e.getCause();
            } else {
                throw e;
            }
        }

        // do not close the command yet. this will be done when the stream is consumed

        return result;
    }

    @Override
    protected final RES_T execute(final CMD_T command) {

        final ResultCallback<A_RES_T> resultCallback = command.getResultCallback();

        ResultCallback<A_RES_T> delegatingResultCallback = new ResultCallback<A_RES_T>() {

            @Override
            public void close() throws IOException {
                resultCallback.close();
                command.close();
            }

            @Override
            public void onStart(Closeable closeable) {
                resultCallback.onStart(closeable);
            }

            @Override
            public void onNext(A_RES_T object) {
                resultCallback.onNext(object);
            }

            @Override
            public void onError(Throwable throwable) {
                resultCallback.onError(throwable);
            }

            @Override
            public void onComplete() {
                resultCallback.onComplete();
                try {
                    command.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        AbstractCallbackNotifier<A_RES_T> callbackNotifier = callbackNotifier(command, delegatingResultCallback);

        AbstractCallbackNotifier.startAsyncProcessing(callbackNotifier);

        return null;
    }

    protected abstract AbstractCallbackNotifier<A_RES_T> callbackNotifier(CMD_T command,
            ResultCallback<A_RES_T> resultCallback);

}
