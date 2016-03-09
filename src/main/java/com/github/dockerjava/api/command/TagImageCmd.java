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
    String getImageId();

    @CheckForNull
    String getRepository();

    @CheckForNull
    String getTag();

    @CheckForNull
    Boolean hasForceEnabled();

    TagImageCmd withImageId(@Nonnull String imageId);

    TagImageCmd withRepository(String repository);

    TagImageCmd withTag(String tag);

    TagImageCmd withForce();

    TagImageCmd withForce(Boolean force);

    interface Exec extends DockerCmdSyncExec<TagImageCmd, Void> {
    }

}
