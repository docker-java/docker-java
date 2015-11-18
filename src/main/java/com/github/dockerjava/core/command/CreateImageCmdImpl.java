package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;

/**
 * Create an image by importing the given stream of a tar file.
 */
public class CreateImageCmdImpl extends AbstrDockerCmd<CreateImageCmd, CreateImageResponse> implements CreateImageCmd {

    private String repository, tag;

    private InputStream imageStream;

    /**
     * @param repository
     *            the repository to import to
     * @param imageStream
     *            the InputStream of the tar file
     */
    public CreateImageCmdImpl(CreateImageCmd.Exec exec, String repository, InputStream imageStream) {
        super(exec);
        withRepository(repository);
        withImageStream(imageStream);
    }

    @Override
    public String getRepository() {
        return repository;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public InputStream getImageStream() {
        return imageStream;
    }

    /**
     * @param repository
     *            the repository to import to
     */
    @Override
    public CreateImageCmdImpl withRepository(String repository) {
        checkNotNull(repository, "repository was not specified");
        this.repository = repository;
        return this;
    }

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    @Override
    public CreateImageCmdImpl withImageStream(InputStream imageStream) {
        checkNotNull(imageStream, "imageStream was not specified");
        this.imageStream = imageStream;
        return this;
    }

    /**
     * @param tag
     *            any tag for this image
     */
    @Override
    public CreateImageCmdImpl withTag(String tag) {
        checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }
}
