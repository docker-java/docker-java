package com.github.dockerjava.junit;

import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;

class DockerCmdExecFactoryDelegate extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

    final DockerCmdExecFactory delegate;

    DockerCmdExecFactoryDelegate(DockerCmdExecFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public final DockerCmdExecFactory getDockerCmdExecFactory() {
        return delegate;
    }

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        if (delegate instanceof DockerClientConfigAware) {
            ((DockerClientConfigAware) delegate).init(dockerClientConfig);
        }
    }
}
