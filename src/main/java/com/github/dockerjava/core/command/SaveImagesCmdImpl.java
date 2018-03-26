package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.SaveImagesCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SaveImagesCmdImpl extends AbstrDockerCmd<SaveImagesCmd, InputStream> implements SaveImagesCmd {

    //CHECKSTYLE:OFF

    private List<String> images;

    public SaveImagesCmdImpl(SaveImagesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    /**
     * @param images
     *            The image name, with or wihout tag, e.g. "mysql", "mysql:8.0",
     *            "mysql/mysql-server" or "mysql/mysql-server:8.0". Not null.
     */
    @Override
    public SaveImagesCmd withImages(List<String> images) {
        checkNotNull(images, "images was not specified");
        this.images = images;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
