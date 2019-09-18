package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(name, "name was not specified");
        this.name = name;
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
