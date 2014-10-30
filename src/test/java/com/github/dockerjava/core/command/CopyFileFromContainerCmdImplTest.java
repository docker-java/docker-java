package com.github.dockerjava.core.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

import org.apache.commons.io.IOUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.InputStream;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Test(groups = "integration")
public class CopyFileFromContainerCmdImplTest extends AbstractDockerClientTest {

    @BeforeTest
    public void beforeTest() {
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
    public void copyFromContainer() throws Exception {
        // TODO extract this into a shared method
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("docker-java-itest-copyFromContainer")
                .withCmd("touch", "/test")
                .exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream response = null;
        try {
        	response = dockerClient.copyFileFromContainerCmd(container.getId(), "/test").exec();
        	boolean condition = response.available() > 0;
			
        	// read the stream fully. Otherwise, the underlying stream will not be closed.
        	for(String line:IOUtils.readLines(response)) {
        		assertThat("", null != line);
        	}
        	
        	assertTrue(condition, "The file was not copied from the container.");        	
        } finally {
        	System.out.println("Close copyFromContainer stream.");
        	response.close();        	
        }
    }
    
    @Test
    public void copyFromNonExistingContainer() throws Exception {
    	try {
    		dockerClient.copyFileFromContainerCmd("non-existing", "/test").exec();
    		fail("expected NotFoundException");
		} catch (NotFoundException e) {
		}
    }
}
