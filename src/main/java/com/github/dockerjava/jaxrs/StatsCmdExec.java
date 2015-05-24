package com.github.dockerjava.jaxrs;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Stats;

/**
 * @author Heng WU(wuheng09@otcaix.iscas.ac.cn)
 *
 */
public class StatsCmdExec extends AbstrDockerCmdExec<StatsCmd, Stats> implements
		StatsCmd.Exec {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatsCmdExec.class);

	public StatsCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	protected Stats execute(StatsCmd command) {
		WebTarget webResource = getBaseResource()
				.path("/containers/{id}/stats").resolveTemplate("id",
						command.getContainerId());

		LOGGER.trace("GET: {}", webResource);
		JerseyInvocation.Builder request = (JerseyInvocation.Builder) webResource.request();
		// see #221
		// This call would results in the application blocking if container is shutdown.
		// So we need timeout settings 
		// see <code>com.github.dockerjava.api.command.StatsCmdTest<code>
		request.property(ClientProperties.READ_TIMEOUT, 5000);
		return request.accept(MediaType.APPLICATION_JSON)
				.get(Stats.class);
	}

	
}
