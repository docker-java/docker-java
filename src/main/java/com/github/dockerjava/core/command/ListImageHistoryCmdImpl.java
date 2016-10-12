package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListImageHistoryCmd;
import com.github.dockerjava.api.model.ImageHistory;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List history entries of an image.
 */
public class ListImageHistoryCmdImpl extends AbstrDockerCmd<ListImageHistoryCmd, List<ImageHistory>> implements
        ListImageHistoryCmd {

    private String imageId;

    public ListImageHistoryCmdImpl(ListImageHistoryCmd.Exec exec, String imageId) {
        super(exec);
        withImageId(imageId);
    }

    @Override
    public String getImageId() {
        return this.imageId;
    }

    @Override
    public ListImageHistoryCmd withImageId(@Nonnull String imageId) {
        checkNotNull(imageId, "image name not specified");
        this.imageId = imageId;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
