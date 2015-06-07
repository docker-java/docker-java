package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.core.util.DockerImageName;

import java.io.InputStream;

public interface SaveImageCmd extends DockerCmd<InputStream>{

    public DockerImageName getImageName();

    /**
     * @param imageName The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    public SaveImageCmd withImageName(DockerImageName imageName);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent
	 * connection leaks.
	 * 
     * @throws com.github.dockerjava.api.NotFoundException No such image
     */
    public InputStream exec() throws NotFoundException;

    public static interface Exec extends DockerCmdExec<SaveImageCmd, InputStream> {
    }


}
