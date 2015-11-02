package com.github.dockerjava.jaxrs.jersey.jaxrs.jersey;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.jaxrs.jersey.jaxrs.DockerCmdExecFactoryImpl;

/**
 * Created by cruffalo on 11/2/15.
 */
public class JerseyDockerCmdExecFactoryImpl extends DockerCmdExecFactoryImpl {

    @Override
    public BuildImageCmd.Exec createBuildImageCmdExec() {
        return new JerseyBuildImageCmdExec(this.getBaseResource(), this.getDockerClientConfig());
    }

}
