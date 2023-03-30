package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.SaveImagesCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class SaveImagesCmdImpl extends AbstrDockerCmd<SaveImagesCmd, InputStream> implements SaveImagesCmd {

    private static class TaggedImageImpl implements TaggedImage {
        private final String name;
        private final String tag;

        private TaggedImageImpl(String name, String tag) {
            this.name = Objects.requireNonNull(name, "image name was not specified");
            this.tag = Objects.requireNonNull(tag, "image tag was not specified");
        }

        @Override
        public String asString() {
            return name + ":" + tag;
        }

        @Override
        public String toString() {
            return asString();
        }
    }

    private final ImmutableList.Builder<TaggedImage> taggedImages = ImmutableList.builder();

    public SaveImagesCmdImpl(final SaveImagesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public SaveImagesCmd withImage(@Nonnull final String name, @Nonnull final String tag) {
        taggedImages.add(new TaggedImageImpl(name, tag));
        return this;
    }



    @Override
    public List<TaggedImage> getImages() {
        return taggedImages.build();
    }

    /**
     * @throws NotFoundException No such images
     */
    @Override
    public InputStream exec() throws NotFoundException {
        return super.exec();
    }
}
