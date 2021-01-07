package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.LoadImageAsyncCmd;
import com.github.dockerjava.api.model.LoadImageAsyncResponseItem;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author luwt
 * @date 2021/1/7.
 */
public class LoadImageAsyncCmdImpl extends AbstrAsyncDockerCmd<LoadImageAsyncCmd, LoadImageAsyncResponseItem> implements LoadImageAsyncCmd {

    private InputStream imageStream;

    public LoadImageAsyncCmdImpl(Exec exec, InputStream imageStream) {
        super(exec);
        withImageStream(imageStream);
    }

    @CheckForNull
    @Override
    public InputStream getImageStream() {
        return imageStream;
    }

    @Override
    public LoadImageAsyncCmd withImageStream(@Nonnull InputStream imageStream) {
        checkNotNull(imageStream, "imageStream was not specified");
        this.imageStream = imageStream;
        return this;
    }

    @Override
    public void close() {
        super.close();
        try {
            imageStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
