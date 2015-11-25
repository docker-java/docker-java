package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Image;

/**
 * List images
 *
 * @param showAll
 *            - Show all images (by default filter out the intermediate images used to build)
 * @param filters
 *            - a json encoded value of the filters (a map[string][]string) to process on the images list.
 */
public class ListImagesCmdImpl extends AbstrDockerCmd<ListImagesCmd, List<Image>> implements ListImagesCmd {

	private String imageNameFilter;
    private String filters;

    private Boolean showAll = false;

    public ListImagesCmdImpl(ListImagesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public String getFilters() {
        return filters;
    }

    @Override
    public Boolean hasShowAllEnabled() {
        return showAll;
    }

    @Override
    public ListImagesCmd withShowAll(Boolean showAll) {
        this.showAll = showAll;
        return this;
    }

    @Override
    public ListImagesCmd withFilters(String filter) {
        checkNotNull(filter, "filters have not been specified");
        this.filters = filter;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("images ").append(showAll ? "--all=true" : "")
                .append(filters != null ? "--filters " + filters : "")
                .append(imageNameFilter != null ? "--filter " + imageNameFilter : "").toString();
    }

    @Override
    public ListImagesCmd withImageNameFilter(String imageNameFilter) {
        checkNotNull(imageNameFilter, "image name filter not specified");
        this.imageNameFilter = imageNameFilter;
        return this;
    }

    @Override
    public String getImageNameFilter() {
        return this.imageNameFilter;
    }
}
