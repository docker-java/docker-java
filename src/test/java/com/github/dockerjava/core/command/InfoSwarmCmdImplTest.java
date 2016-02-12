package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.SwarmInfo;
import com.github.dockerjava.client.AbstractDockerSwarmClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Test(groups = "swarmintegration")
public class InfoSwarmCmdImplTest extends AbstractDockerSwarmClientTest {

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
    public void info() throws DockerException {

        waitForSwarmCompletion();

        Info dockerInfo = swarmClient.infoCmd().exec();
        LOG.info(dockerInfo.toString());

        assertTrue(dockerInfo.toString().contains("containers"));
        assertTrue(dockerInfo.toString().contains("images"));
        assertTrue(dockerInfo.toString().contains("debug"));

        assertTrue(dockerInfo.getContainers() > 0);
        assertTrue(dockerInfo.getImages() > 0);

        assertTrue(dockerInfo instanceof SwarmInfo);
        SwarmInfo casted = (SwarmInfo)dockerInfo;
        assertTrue(casted.getSwarmNodes().size() > 0);

        SwarmInfo.Node node = casted.getSwarmNodes().entrySet().iterator().next().getValue();
        assertEquals(node.getContainers(), (int)dockerInfo.getContainers());
        assertEquals(node.getStatus(), "Healthy");
    }
}