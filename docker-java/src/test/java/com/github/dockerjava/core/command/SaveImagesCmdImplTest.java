package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.SaveImagesCmd;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SaveImagesCmdImplTest {

    private static final SaveImagesCmd.Exec NOOP_EXEC = new SaveImagesCmd.Exec() {
        @Override
        public InputStream exec(SaveImagesCmd command) {
            return null;
        }
    };

    @Test
    public void withImageNullTagProducesNameOnly() {
        SaveImagesCmd cmd = new SaveImagesCmdImpl(NOOP_EXEC).withImage("busybox", null);

        List<SaveImagesCmd.TaggedImage> images = cmd.getImages();
        assertEquals(1, images.size());
        assertEquals("busybox", images.get(0).asString());
    }

    @Test
    public void withImageTaggedFormRemainsNameColonTag() {
        SaveImagesCmd cmd = new SaveImagesCmdImpl(NOOP_EXEC).withImage("busybox", "latest");

        List<SaveImagesCmd.TaggedImage> images = cmd.getImages();
        assertEquals(1, images.size());
        assertEquals("busybox:latest", images.get(0).asString());
    }
}
