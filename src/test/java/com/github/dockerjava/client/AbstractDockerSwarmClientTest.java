package com.github.dockerjava.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.TestDockerCmdExecFactory;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDockerSwarmClientTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(AbstractDockerSwarmClientTest.class);

    protected TestDockerCmdExecFactory swarmCmdExecFactory = initTestDockerCmdExecFactory();
    protected DockerClient swarmClient;

    public void beforeTest() throws Exception {
        super.beforeTest();
        LOG.info("======================= BEFORETEST-SWARM =======================");

        try {
            dockerClient.inspectImageCmd("swarm").exec();
        } catch (NotFoundException e) {
            LOG.info("Pulling image 'swarm'");
            // need to block until image is pulled completely
            dockerClient.pullImageCmd("swarm").withTag("latest").exec(new PullImageResultCallback()).awaitSuccess();
        }

        cleanSwarmContainers();

        DockerClientConfig conf = config(null);
        URI dockerHost = conf.getDockerHost();
        String ip = dockerHost.getHost();
        int port = dockerHost.getPort();

        String agentId = dockerClient.createContainerCmd("swarm")
                .withName("swarm-agent")
                .withCmd("join", "--advertise="+ip+":"+port)
                .exec().getId();
        dockerClient.startContainerCmd(agentId).exec();

        List<String> cmds = new ArrayList<>();
        cmds.add("manage");
        cmds.add("--advertise=" + ip + ":3375");

        CreateContainerCmd createMaster = dockerClient.createContainerCmd("swarm")
                .withName("swarm-master")
                .withPortBindings(new PortBinding(new Ports.Binding(3375), new ExposedPort(2375)))
                .withExposedPorts(new ExposedPort(2375));

        if (conf.getDockerTlsVerify()) {
            createMaster.withVolumes(new Volume("/certs"))
                    .withBinds(new Bind("/var/lib/boot2docker/", new Volume("/certs"), AccessMode.ro));

            cmds.add("--tlsverify");
            cmds.add("--tlscacert=/certs/ca.pem");
            cmds.add("--tlscert=/certs/server.pem");
            cmds.add("--tlskey=/certs/server-key.pem");
        }

        cmds.add("nodes://" + ip + ":" + port);

        String masterId = createMaster.withCmd(cmds).exec().getId();
        dockerClient.startContainerCmd(masterId).exec();

        swarmClient = DockerClientBuilder.getInstance(swarmConfig(ip)).withDockerCmdExecFactory(swarmCmdExecFactory).build();

        LOG.info("======================= END OF BEFORETEST-SWARM =======================\n\n");
    }

    /**
     * call this from your test, if its essential, that the agent is registered at the master
     * could take a few seconds
     */
    protected void waitForSwarmCompletion() {
        boolean nodesThere = false;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //nothing
            }
            SwarmInfo info = (SwarmInfo)swarmClient.infoCmd().exec();
            nodesThere = info.getSwarmNodes().size() > 0;
        } while (!nodesThere);
    }

    private DockerClientConfig swarmConfig(String ip) {
        return DockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://" + ip + ":3375")
                .withSwarmEndpoint()
                .withApiVersion(apiVersion).build();
    }

    @Override
    public void afterTest() {
        cleanSwarmContainers();
        super.afterTest();
    }

    private void cleanSwarmContainers() {
        try {
            dockerClient.removeContainerCmd("swarm-master").withForce(true).exec();
        } catch (DockerException e) {
            LOG.error(e.toString());
        }
        try {
            dockerClient.removeContainerCmd("swarm-agent").withForce(true).exec();
        } catch (DockerException e) {
            LOG.error(e.toString());
        }
    }



    public void afterMethod(ITestResult result) {

        for (String container : swarmCmdExecFactory.getContainerNames()) {
            if (container.contains("swarm")) continue;
            LOG.info("Cleaning up temporary container {}", container);


            try {
                swarmClient.removeContainerCmd(container).withForce(true).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        for (String image : swarmCmdExecFactory.getImageNames()) {
            if (image.contains("swarm")) continue;
            LOG.info("Cleaning up temporary image with {}", image);
            try {
                swarmClient.removeImageCmd(image).withForce(true).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        for (String volume : swarmCmdExecFactory.getVolumeNames()) {
            LOG.info("Cleaning up temporary volume with {}", volume);
            try {
                swarmClient.removeVolumeCmd(volume).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        for (String networkId : swarmCmdExecFactory.getNetworkIds()) {
            LOG.info("Cleaning up temporary network with {}", networkId);
            try {
                swarmClient.removeNetworkCmd(networkId).exec();
            } catch (DockerException ignore) {
                // ignore.printStackTrace();
            }
        }

        LOG.info("################################## END OF {} ##################################\n", result.getName());
    }
}
