package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.ListTasksCmd;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.FiltersEncoder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
                    .queryParam("filters", FiltersEncoder.jsonEncode(command.getFilters()));
        }

        LOGGER.trace("GET: {}", webTarget);

        List<Task> tasks = webTarget.request().accept(MediaType.APPLICATION_JSON)
                .get(new TypeReference<List<Task>>() {
                });

        LOGGER.trace("Response: {}", tasks);

        return tasks;
    }

}
