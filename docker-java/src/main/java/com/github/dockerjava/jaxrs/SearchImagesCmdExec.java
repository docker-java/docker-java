package com.github.dockerjava.jaxrs;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientConfig;

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
        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<SearchItem>>() {
        });
    }

}
