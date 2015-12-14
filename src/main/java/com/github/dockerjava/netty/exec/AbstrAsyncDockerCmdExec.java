package com.github.dockerjava.netty.exec;

import java.io.Closeable;
import java.io.IOException;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AsyncDockerCmd;
import com.github.dockerjava.api.command.DockerCmdAsyncExec;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.WebTarget;

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

        execute0(command, delegatingResultCallback);

        return null;
    }

    protected abstract Void execute0(final CMD_T command, final ResultCallback<A_RES_T> resultCallback);

}
