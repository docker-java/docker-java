package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.PushEventStreamItem;

import java.io.IOException;
import java.io.InputStream;

public interface SaveImageCmd extends DockerCmd<InputStream>{

    public String getName();

    public String getTag();

    /**
     * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    public SaveImageCmd withName(String name);

    /**
     * @param tag The image's tag. Not null.
     */
    public SaveImageCmd withTag(String tag);

    public AuthConfig getAuthConfig();

    public SaveImageCmd withAuthConfig(AuthConfig authConfig);

    /**
     * @throws com.github.dockerjava.api.NotFoundException No such image
     */
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdExec<SaveImageCmd, InputStream> {
    }


}