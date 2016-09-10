package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.UpdateContainerResponse;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * @author Kanstantsin Shautsou
 */
@Test(groups = "integration")
public class UpdateContainerCmdExecTest extends AbstractNettyDockerClientTest {
    public static final Logger LOG = LoggerFactory.getLogger(UpdateContainerCmdExecTest.class);
    private static final String BUSYBOX_IMAGE = "busybox";

    @BeforeTest
    public void beforeTest() throws Exception {
        super.beforeTest();
    }

    @AfterTest
    public void afterTest() {
        super.afterTest();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
    }

    @Test
    public void updateContainer() throws DockerException, IOException {
        final RemoteApiVersion apiVersion = getVersion(dockerClient);

        if (!apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_22)) {
            throw new SkipException("API version should be >= 1.22");
        }

        CreateContainerResponse response = dockerClient.createContainerCmd(BUSYBOX_IMAGE)
                .withCmd("sleep", "9999")
                .exec();
        String containerId = response.getId();
        dockerClient.startContainerCmd(containerId).exec();

        InspectContainerResponse inspectBefore = dockerClient.inspectContainerCmd(containerId).exec();
        final HostConfig beforeHostConfig = inspectBefore.getHostConfig();

        final UpdateContainerResponse updateResponse = dockerClient.updateContainerCmd(containerId)
                .withBlkioWeight(300)
                .withCpuShares(512)
                .withCpuPeriod(100000)
                .withCpuQuota(50000)
//                .withCpusetCpus("0") // depends on env
                .withCpusetMems("0")
                .withMemory(314572800L)
//              .withMemorySwap(514288000L) Your kernel does not support swap limit capabilities, memory limited without swap.
                .withMemoryReservation(209715200L)
//                .withKernelMemory(52428800) Can not update kernel memory to a running container, please stop it first.
                .exec();

        // found only on docker toolbox (1.10.1)
//        assertThat(updateResponse.getWarnings(), hasSize(1));
//        assertThat(updateResponse.getWarnings().get(0),
//                is("Your kernel does not support Block I/O weight. Weight discarded."));

        InspectContainerResponse inspectAfter = dockerClient.inspectContainerCmd(containerId).exec();
        final HostConfig afterHostConfig = inspectAfter.getHostConfig();

        assertThat(afterHostConfig.getMemory(),
                is(314572800L));

//        assertThat(afterHostConfig.getBlkioWeight(), is(300));
        assertThat(afterHostConfig.getCpuShares(), is(512));
        assertThat(afterHostConfig.getCpuPeriod(), is(100000));
        assertThat(afterHostConfig.getCpuQuota(), is(50000));
        assertThat(afterHostConfig.getCpusetMems(), is("0"));

        assertThat(afterHostConfig.getMemoryReservation(), is(209715200L));
//        assertThat(afterHostConfig.getMemorySwap(), is(514288000L));
    }

}
