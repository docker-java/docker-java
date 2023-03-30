package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.TagImageCmd;

/**
 * Tag an image into a repository
 *
 * @param image
 *            The local image to tag (either a name or an id)
 * @param repository
 *            The repository to tag in
 * @param force
 *            (not documented)
 *
 */
public class TagImageCmdImpl extends AbstrDockerCmd<TagImageCmd, Void> implements TagImageCmd {

    private String imageId, repository, tag;

    private Boolean force;

    public TagImageCmdImpl(TagImageCmd.Exec exec, String imageId, String repository, String tag) {
        super(exec);
        withImageId(imageId);
        withRepository(repository);
        withTag(tag);
    }

    @Override
    public String getImageId() {
        return imageId;
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
    public Boolean hasForceEnabled() {
        return force;
    }

    @Override
    public TagImageCmd withImageId(String imageId) {
        this.imageId = Objects.requireNonNull(imageId, "imageId was not specified");
        return this;
    }

    @Override
    public TagImageCmd withRepository(String repository) {
        this.repository = Objects.requireNonNull(repository, "repository was not specified");
        return this;
    }

    @Override
    public TagImageCmd withTag(String tag) {
        this.tag = Objects.requireNonNull(tag, "tag was not specified");
        return this;
    }

    @Override
    public TagImageCmd withForce() {
        return withForce(true);
    }

    @Override
    public TagImageCmd withForce(Boolean force) {
        this.force = force;
        return this;
    }

}
