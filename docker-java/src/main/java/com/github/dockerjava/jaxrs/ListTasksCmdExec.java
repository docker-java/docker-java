package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.ListTasksCmd;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;

public class ListTasksCmdExec extends AbstrSyncDockerCmdExec<ListTasksCmd, List<Task>> implements
        ListTasksCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListTasksCmdExec.class);

    public ListTasksCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected List<Task> execute(ListTasksCmd command) {
        WebTarget webTarget = getBaseResource().path("/tasks");

        if (command.getFilters() != null && !command.getFilters().isEmpty()) {
            webTarget = webTarget
                    .queryParam("filters", urlPathSegmentEscaper().escape(FiltersEncoder.jsonEncode(command.getFilters())));
        }

        LOGGER.trace("GET: {}", webTarget);
        List<Task> tasks = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Task>>() {
                });
        LOGGER.trace("Response: {}", tasks);

        return tasks;
    }
}

