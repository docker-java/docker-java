package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.RemoveVolumeCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Remove a volume.
 *
 * @author Marcus Linke
 */
public class RemoveVolumeCmdImpl extends AbstrDockerCmd<RemoveVolumeCmd, Void> implements RemoveVolumeCmd {

    private String name;

    public RemoveVolumeCmdImpl(RemoveVolumeCmd.Exec exec, String name) {
        super(exec);
        withName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RemoveVolumeCmd withName(String name) {
        this.name = Objects.requireNonNull(name, "name was not specified");
        return this;
    }

    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
