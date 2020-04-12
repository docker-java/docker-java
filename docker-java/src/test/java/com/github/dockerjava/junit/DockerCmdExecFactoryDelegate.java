package com.github.dockerjava.junit;

import com.github.dockerjava.api.command.CreatePluginCmd;
import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.InspectPluginCmd;
import com.github.dockerjava.api.command.ListPluginsCmd;
import com.github.dockerjava.api.command.RemovePluginCmd;
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
    @Override
    public ListPluginsCmd.Exec listPluginsCmdExec() { return delegate.listPluginsCmdExec(); }

    @Override
    public InspectPluginCmd.Exec createInspectPluginCmdExec() { return delegate.createInspectPluginCmdExec(); }

    @Override
    public RemovePluginCmd.Exec createRemovePluginCmdExec() { return delegate.createRemovePluginCmdExec(); }

    @Override
    public CreatePluginCmd.Exec createPluginCmdExec() { return delegate.createPluginCmdExec(); }
}
