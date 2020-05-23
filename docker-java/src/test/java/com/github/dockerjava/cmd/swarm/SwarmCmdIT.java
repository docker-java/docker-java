package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.junit.category.Integration;
import com.github.dockerjava.junit.category.SwarmModeIntegration;
import org.junit.Before;
import org.junit.experimental.categories.Category;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_24;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.junit.Assume.assumeThat;

@Category({SwarmModeIntegration.class, Integration.class})
public abstract class SwarmCmdIT extends CmdIT {

    @Before
    public final void setUpSwarmCmdIT() {
        assumeThat(dockerRule, isGreaterOrEqual(VERSION_1_24));
        leaveIfInSwarm();
    }

    private void leaveIfInSwarm() {
        try {
            // force in case this is a swarm manager
            dockerRule.getClient().leaveSwarmCmd().withForceEnabled(true).exec();
        } catch (NotAcceptableException e) {
            // do nothing, node is not part of a swarm
        } catch (DockerException ex) {
            if (!ex.getMessage().contains("node is not part of a swarm")) {
                throw ex;
            }
        }
    }
}
