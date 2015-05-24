package com.github.dockerjava.api.command;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Stats;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * @author Heng WU(wuheng09@otcaix.iscas.ac.cn)
 *
 * if container is not exist, throws com.github.dockerjava.api.NotFoundException
 * if container is shutdown, throws java.net.SocketTimeoutException
 * if container is startup, return Object <code>Stats<code>
 */
public class StatsCmdTest {

	final static String DOCKER_SERVER = "http://127.0.0.1:2375";

	final static String STARTUP_CONTAINER_ID = "4b4fb43779bcc2cb4d5aad954dac99224ff1c2ea623cd95322ef657a5c2f2c36";

	final static String SHUTDOWN_CONTAINER_ID = "c571b02e26d30e03dfc21829de618df440275c303fbddb384679a5949d0de5d0";
	
	final static String NOT_EXIST_CONTAINER_ID = "test1234";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		noramlStatsImplWithContainer();
//		simpleStatsImplWithContainer();
	}

	
	private static void noramlStatsImplWithContainer() throws IOException {
		DockerClient client = DockerClientBuilder.getInstance(DOCKER_SERVER)
				.build();
		Stats stats = client.statsCmd(SHUTDOWN_CONTAINER_ID).exec();
		System.out.println("Read:" + stats.getRead());
		System.out.println("Cpu_stats:" + stats.getCpu_stats());
		System.out.println("Memory_stats:" + stats.getMemory_stats());
		client.close();
	}

	private static void simpleStatsImplWithContainer() {
		JerseyClient jcli = new JerseyClientBuilder().build();
		jcli.register(JacksonJsonProvider.class);
		WebTarget baseTar = jcli.target(DOCKER_SERVER);
		WebTarget target = baseTar.path("/containers/" + SHUTDOWN_CONTAINER_ID
				+ "/stats");
		Stats stats = target.request().accept(MediaType.APPLICATION_JSON)
				.get(Stats.class);
		System.out.println("Read:" + stats.getRead());
		System.out.println("Cpu_stats:" + stats.getCpu_stats());
		System.out.println("Memory_stats:" + stats.getMemory_stats());
		jcli.close();
	}

}
