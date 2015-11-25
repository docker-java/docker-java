package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

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
public interface TagImageCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getImageId();

    @CheckForNull
    public String getRepository();

    @CheckForNull
    public String getTag();

    @CheckForNull
    public Boolean hasForceEnabled();

    public TagImageCmd withImageId(@Nonnull String imageId);

    public TagImageCmd withRepository(String repository);

    public TagImageCmd withTag(String tag);

    public TagImageCmd withForce();

    public TagImageCmd withForce(Boolean force);

    public static interface Exec extends DockerCmdSyncExec<TagImageCmd, Void> {
    }

}