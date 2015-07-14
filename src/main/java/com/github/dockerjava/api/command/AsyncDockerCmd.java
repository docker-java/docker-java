/*
 * Created on 17.06.2015
 */
package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallback;

/**
 *
 *
 * @author marcus
 *
 */
public interface AsyncDockerCmd<CMD_T extends AsyncDockerCmd<CMD_T, A_RES_T, RES_T>, A_RES_T, RES_T> extends DockerCmd<RES_T> {

    public ResultCallback<A_RES_T> getResultCallback();

    public CMD_T withResultCallback(ResultCallback<A_RES_T> resultCallback);

}
