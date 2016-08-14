package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.model.LoadImageResponseItem;

import javax.annotation.Nonnull;

public class LoadImageCmdImpl extends AbstrAsyncDockerCmd<LoadImageCmd, LoadImageResponseItem> implements LoadImageCmd {

    private InputStream imageStream;

    // set false because in quiet mode it returns not JSON!
    private Boolean quiet = false;

    /**
     * @param imageStream the InputStream of the tar file
     */
    public LoadImageCmdImpl(LoadImageCmd.Exec exec, InputStream imageStream) {
        super(exec);
        withImageStream(imageStream);
    }

    @Override
    public InputStream getImageStream() {
        return imageStream;
    }

    @Override
    public Boolean getQuiet() {
        return quiet;
    }

    @Override
    public LoadImageCmd withQuiet(Boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    /**
     * @param imageStream the InputStream of the tar file
     */
    @Override
    public LoadImageCmdImpl withImageStream(@Nonnull InputStream imageStream) {
        checkNotNull(imageStream, "imageStream was not specified");
        this.imageStream = imageStream;
        return this;
    }
}
