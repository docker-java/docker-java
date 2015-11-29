package com.github.dockerjava.netty.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchImagesCmdExec extends AbstrSyncDockerCmdExec<SearchImagesCmd, List<SearchItem>> implements
        SearchImagesCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchImagesCmdExec.class);

    public SearchImagesCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<SearchItem> execute(SearchImagesCmd command) {
        WebTarget webResource = getBaseResource().path("/images/search").queryParam("term", command.getTerm());

        LOGGER.trace("GET: {}", webResource);
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<List<SearchItem>>() {
        }).awaitResult();
    }

}
