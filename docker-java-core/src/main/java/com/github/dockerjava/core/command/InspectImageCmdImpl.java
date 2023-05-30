package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Inspect the details of an image.
 */
public class InspectImageCmdImpl extends AbstrDockerCmd<InspectImageCmd, InspectImageResponse> implements
        InspectImageCmd {

    private String imageId;

    public InspectImageCmdImpl(InspectImageCmd.Exec exec, String imageId) {
        super(exec);
        withImageId(imageId);
    }

    @Override
    public String getImageId() {
        return imageId;
    }

    @Override
    public InspectImageCmd withImageId(String imageId) {
        this.imageId = Objects.requireNonNull(imageId, "imageId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public InspectImageResponse exec() throws NotFoundException {
        return super.exec();
    }
}
