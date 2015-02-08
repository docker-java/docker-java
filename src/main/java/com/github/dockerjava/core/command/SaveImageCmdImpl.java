package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.SaveImageCmd;

import java.io.InputStream;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

public class SaveImageCmdImpl extends AbstrAuthCfgDockerCmd<SaveImageCmd, InputStream> implements SaveImageCmd {
    private String name;
    private String tag;

    public SaveImageCmdImpl(SaveImageCmd.Exec exec, String name) {
        super(exec);
        withName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    /**
     * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    @Override
    public SaveImageCmd withName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    /**
     * @param tag The image's tag. Can be null or empty.
     */
    @Override
    public SaveImageCmd withTag(String tag) {
        checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("get ")
                .append(name)
                .toString();
    }

    /**
     * @throws com.github.dockerjava.api.NotFoundException No such image
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
