package com.github.dockerjava.netty.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListImageHistoryCmd;
import com.github.dockerjava.api.model.ImageHistory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListImageHistoryCmdExec extends AbstrSyncDockerCmdExec<ListImageHistoryCmd, List<ImageHistory>> implements
        ListImageHistoryCmd.Exec  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmdExec.class);

    public ListImageHistoryCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<ImageHistory> execute(ListImageHistoryCmd command) {

        WebTarget webResource = getBaseResource().path("/images/{id}/history").resolveTemplate("id", command.getImageId());

        LOGGER.debug("GET: {}", webResource);

        List<ImageHistory> history =  webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<List<ImageHistory>>() {
        });

        LOGGER.debug("Response: {}", history);

        return history;
    }
}
