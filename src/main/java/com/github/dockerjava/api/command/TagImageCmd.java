package com.github.dockerjava.api.command;

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
public interface TagImageCmd extends DockerCmd<Void> {

    public String getImageId();

    public String getRepository();

    public String getTag();

    public boolean hasForceEnabled();

    public TagImageCmd withImageId(String imageId);

    public TagImageCmd withRepository(String repository);

    public TagImageCmd withTag(String tag);

    public TagImageCmd withForce();

    public TagImageCmd withForce(boolean force);

    public static interface Exec extends DockerCmdExec<TagImageCmd, Void> {
    }

}