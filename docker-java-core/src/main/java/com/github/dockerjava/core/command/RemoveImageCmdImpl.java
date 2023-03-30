package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 *
 * Remove an image, deleting any tags it might have.
 *
 */
public class RemoveImageCmdImpl extends AbstrDockerCmd<RemoveImageCmd, Void> implements RemoveImageCmd {

    private String imageId;

    private Boolean force, noPrune;

    public RemoveImageCmdImpl(RemoveImageCmd.Exec exec, String imageId) {
        super(exec);
        withImageId(imageId);
    }

    @Override
    public String getImageId() {
        return imageId;
    }

    @Override
    public Boolean hasForceEnabled() {
        return force;
    }

    @Override
    public Boolean hasNoPruneEnabled() {
        return noPrune;
    }

    @Override
    public RemoveImageCmd withImageId(String imageId) {
        this.imageId = Objects.requireNonNull(imageId, "imageId was not specified");
        return this;
    }

    @Override
    public RemoveImageCmd withForce(Boolean force) {
        this.force = force;
        return this;
    }

    @Override
    public RemoveImageCmd withNoPrune(Boolean noPrune) {
        this.noPrune = noPrune;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
