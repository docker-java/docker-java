/*
 * Created on 17.06.2015
 */
package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.async.ResultCallbackTemplate;

/**
 *
 *
 * @author Marcus Linke
 *
 */
public interface AsyncDockerCmd<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T>, A_RES_T> extends DockerCmd<Void> {

    <T extends ResultCallback<A_RES_T>> T exec(T resultCallback);

    default ResultCallbackTemplate<?, A_RES_T> start() {
        return exec(new ResultCallback.Adapter<>());
    }
}
