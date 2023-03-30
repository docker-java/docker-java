package com.github.dockerjava.core.command;

import java.io.InputStream;
import java.util.Objects;

import com.github.dockerjava.api.command.LoadImageCmd;

import javax.annotation.Nonnull;

public class LoadImageCmdImpl extends AbstrDockerCmd<LoadImageCmd, Void> implements LoadImageCmd {

    private InputStream imageStream;

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    public LoadImageCmdImpl(LoadImageCmd.Exec exec, InputStream imageStream) {
        super(exec);
        withImageStream(imageStream);
    }

    @Override
    public InputStream getImageStream() {
        return imageStream;
    }

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    @Override
    public LoadImageCmdImpl withImageStream(@Nonnull InputStream imageStream) {
        this.imageStream = Objects.requireNonNull(imageStream, "imageStream was not specified");
        return this;
    }
}
