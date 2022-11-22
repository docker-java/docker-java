package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.LoadImageAsyncCmd;
import com.github.dockerjava.api.model.LoadResponseItem;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoadImageAsyncCmdImpl extends AbstrAsyncDockerCmd<LoadImageAsyncCmd, LoadResponseItem> implements LoadImageAsyncCmd {

    private InputStream inputStream;

    public LoadImageAsyncCmdImpl(LoadImageAsyncCmd.Exec exec, InputStream inputStream) {
        super(exec);
        this.inputStream = inputStream;
    }

    @Override
    public InputStream getImageStream() {
        return this.inputStream;
    }

    @Override
    public LoadImageAsyncCmd withImageStream(InputStream imageStream) {
        checkNotNull(imageStream, "imageStream was not specified");
        this.inputStream = imageStream;
        return this;
    }

    @Override
    public void close() {
        super.close();

        try {
            this.inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
