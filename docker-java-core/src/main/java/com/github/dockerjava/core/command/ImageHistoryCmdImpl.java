package com.github.dockerjava.core.command;

import java.util.List;
import java.util.Objects;

import com.github.dockerjava.api.command.ImageHistoryCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ImageHistory;

/**
 * Get the history of an image.
 */
public class ImageHistoryCmdImpl extends AbstrDockerCmd<ImageHistoryCmd, List<ImageHistory>> implements
        ImageHistoryCmd {

    private String imageId;

    public ImageHistoryCmdImpl(ImageHistoryCmd.Exec exec, String imageId) {
        super(exec);
        withImageId(imageId);
    }

    @Override
    public String getImageId() {
        return imageId;
    }

    @Override
    public ImageHistoryCmd withImageId(String imageId) {
        this.imageId = Objects.requireNonNull(imageId, "imageId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public List<ImageHistory> exec() throws NotFoundException {
        return super.exec();
    }
}
