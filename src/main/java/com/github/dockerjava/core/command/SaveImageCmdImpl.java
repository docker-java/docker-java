package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.exception.NotFoundException;

public class SaveImageCmdImpl extends AbstrDockerCmd<SaveImageCmd, InputStream> implements SaveImageCmd {
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
     * @param name
     *            The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    @Override
    public SaveImageCmd withName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    /**
     * @param tag
     *            The image's tag. Can be null or empty.
     */
    @Override
    public SaveImageCmd withTag(String tag) {
        checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
