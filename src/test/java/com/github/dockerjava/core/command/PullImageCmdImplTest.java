package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Test(groups = "integration")
public class PullImageCmdImplTest extends AbstractDockerClientTest {

    private static final PullImageCmd.Exec NOP_EXEC = new PullImageCmd.Exec() {
        @Override
        public InputStream exec(PullImageCmd command) {
            return null;
        }
    };

    @BeforeTest
	public void beforeTest() throws DockerException {
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
    public void nullAuthConfig() throws Exception {
        PullImageCmdImpl pullImageCmd = new PullImageCmdImpl(NOP_EXEC, null, "");
        try {
            pullImageCmd.withAuthConfig(null);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "authConfig was not specified");
        }
    }

    @Test
	public void testPullImage() throws DockerException, IOException {
		Info info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());

		int imgCount = info.getImages();
		LOG.info("imgCount1: {}", imgCount);

		// This should be an image that is not used by other repositories
		// already
		// pulled down, preferably small in size. If tag is not used pull will
		// download all images in that repository but tmpImgs will only
		// deleted 'latest' image but not images with other tags
		String testImage = "hackmann/empty";

		LOG.info("Removing image: {}", testImage);
		
		try {
			dockerClient.removeImageCmd(testImage).withForce().exec();
		} catch (NotFoundException e) {
			// just ignore if not exist
		}
		

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());

		imgCount = info.getImages();
		LOG.info("imgCount2: {}", imgCount);

		LOG.info("Pulling image: {}", testImage);

		InputStream response = dockerClient.pullImageCmd(testImage).exec();

		assertThat(asString(response), containsString("Download complete"));

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info after pull, {}", info.toString());

		assertThat(imgCount, lessThanOrEqualTo(info.getImages()));

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(testImage).exec();
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
		assertThat(inspectImageResponse, notNullValue());
	}

	@Test
	public void testPullNonExistingImage() throws DockerException, IOException {
		
		// does not throw an exception
		InputStream is = dockerClient.pullImageCmd("nonexisting/foo").exec();
		// stream needs to be fully read in order to close the underlying connection
		this.asString(is);
		
		try {
			dockerClient.pullImageCmd("non-existing/foo").exec();
			fail("expected InternalServerErrorException");
		} catch (InternalServerErrorException ignored) {
		}
		
	}

}
