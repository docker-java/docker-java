package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Inspect the details of a volume.
 */
public class InspectVolumeCmdImpl extends AbstrDockerCmd<InspectVolumeCmd, InspectVolumeResponse> implements
        InspectVolumeCmd {

    private String name;

    public InspectVolumeCmdImpl(InspectVolumeCmd.Exec exec, String name) {
        super(exec);
        withName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InspectVolumeCmd withName(String name) {
        this.name = Objects.requireNonNull(name, "name was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such volume
     */
    @Override
    public InspectVolumeResponse exec() throws NotFoundException {
        return super.exec();
    }
}
