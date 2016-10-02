package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListImageHistoryCmd;
import com.github.dockerjava.api.model.History;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class ListImageHistoryCmdExec extends AbstrSyncDockerCmdExec<ListImageHistoryCmd, List<History>> implements
        ListImageHistoryCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListImagesCmdExec.class);

    public ListImageHistoryCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<History> execute(ListImageHistoryCmd command) {

        WebTarget webTarget = getBaseResource().path("/images/{id}/history").resolveTemplate("id", command.getImageId());

        LOGGER.trace("GET: {}", webTarget);

        List<History> history = webTarget.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<History>>() {
        });
        LOGGER.trace("Response: {}", history);

        return history;
    }
}
