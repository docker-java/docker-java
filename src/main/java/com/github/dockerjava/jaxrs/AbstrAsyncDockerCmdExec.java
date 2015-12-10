package com.github.dockerjava.jaxrs;

import java.io.Closeable;
import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.api.command.DockerCmdAsyncExec;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;

public abstract class AbstrAsyncDockerCmdExec<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T>, A_RES_T> extends
        AbstrDockerCmdExec implements DockerCmdAsyncExec<CMD_T, A_RES_T> {

    public AbstrAsyncDockerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    public Void exec(CMD_T command, ResultCallback<A_RES_T> resultCallback) {
        return execute(command, resultCallback);
    }

    protected final Void execute(final CMD_T command, final ResultCallback<A_RES_T> resultCallback) {

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
                command.close();
            }
        };

        AbstractCallbackNotifier<A_RES_T> callbackNotifier = callbackNotifier(command, delegatingResultCallback);

        AbstractCallbackNotifier.startAsyncProcessing(callbackNotifier);

        return null;
    }

    protected abstract AbstractCallbackNotifier<A_RES_T> callbackNotifier(CMD_T command,
            ResultCallback<A_RES_T> resultCallback);

}
