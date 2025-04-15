package com.github.dockerjava.core.command;

import java.io.InputStream;
import java.util.Objects;

import com.github.dockerjava.api.command.ExportImageCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Implementation of the ExportImageCmd.
 */
public class ExportImageCmdImpl extends AbstrDockerCmd<ExportImageCmd, InputStream> implements ExportImageCmd {

    private String imageId;

    public ExportImageCmdImpl(ExportImageCmd.Exec exec, String imageId) {
        super(exec);
        withImageId(imageId);
    }

    @Override
    public ExportImageCmd withImageId(String imageId) {
        this.imageId = Objects.requireNonNull(imageId, "imageId was not specified");
        return this;
    }

    @Override
    public String getImageId() {
        return imageId;
    }

    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
