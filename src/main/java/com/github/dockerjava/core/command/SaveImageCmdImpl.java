package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.core.util.DockerImageName;

import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class SaveImageCmdImpl extends AbstrDockerCmd<SaveImageCmd, InputStream> implements SaveImageCmd {

    private DockerImageName imageName;

    public SaveImageCmdImpl(SaveImageCmd.Exec exec, DockerImageName imageName) {
        super(exec);
        withImageName(imageName);
    }

    @Override
    public DockerImageName getImageName() {
        return imageName;
    }

    /**
     * @param imageName The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    @Override
    public SaveImageCmd withImageName(DockerImageName imageName) {
        checkNotNull(imageName, "imageName was not specified");
        this.imageName = imageName;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("get ")
                .append(imageName)
                .toString();
    }

    /**
     * @throws com.github.dockerjava.api.NotFoundException No such image
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
