package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.History;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Test(groups = "integration")
public class ListImageHistoryCmdImplTest extends AbstractDockerClientTest {

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
    public void testListHistory() throws Exception {
        final File dockerfile = getDockerfile();

        final String tag = "history_test";

        final String imageIdDockerfile = dockerClient.buildImageCmd(dockerfile)
                .withNoCache(true)
                .withTag(tag)
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        final CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd(imageIdDockerfile).exec();

        final String comment = "my comment";
        final String imageIdCommit = dockerClient.commitCmd(createContainerResponse.getId()).withMessage(comment).exec();

        final List<History> imageHistory = dockerClient.listImageHistoryCmd(imageIdCommit).exec();

        assertThat(imageHistory, notNullValue());

        Long creationTimeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        for(History history : imageHistory){

            // history is ordered by creation timestamp descending
            Long currentCreationTimestamp = history.getCreated();
            assertThat(currentCreationTimestamp, lessThanOrEqualTo(creationTimeStamp));
            creationTimeStamp = currentCreationTimestamp;

            assertThat(history.getId(), not(isEmptyOrNullString()));
            assertThat(history.getCreatedBy(), not(isEmptyOrNullString()));
        }

        final History secondToLatestDockerfile = imageHistory.get(2); // LABEL testLabel myTestLabel
        assertThat(secondToLatestDockerfile.getCreatedBy(), both(containsString("#(nop)")).and(containsString("testLabel=myTestLabel")));

        final History latestFromDockerfile = imageHistory.get(1); // RUN dd if=/dev/zero of=/myLargeFile bs=1M count=1
        assertThat(latestFromDockerfile.getCreatedBy(), containsString("dd if=/dev/zero of=/myLargeFile bs=1M count=1"));

        if (!getVersion(dockerClient).isGreaterOrEqual(RemoteApiVersion.VERSION_1_19)) {

            assertThat(secondToLatestDockerfile.getSize(), is(0L));
            assertThat(secondToLatestDockerfile.getTags(), nullValue());
            assertThat(secondToLatestDockerfile.getComment(), isEmptyString());

            final Long oneMegaByteInBytes = 1024L * 1024L;
            assertThat(latestFromDockerfile.getSize(), greaterThanOrEqualTo(oneMegaByteInBytes));
            final List<String> tags = latestFromDockerfile.getTags();
            assertThat(tags, notNullValue());
            assertThat(tags.get(0), containsString(tag + ":latest"));
            assertThat(latestFromDockerfile.getComment(), isEmptyString());

            final History commit = imageHistory.get(0);
            assertThat(commit.getSize(), is(0L));
            assertThat(commit.getComment(), equalToIgnoringCase(comment));
        }
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testListHistoryOfNonExistingImage(){

        dockerClient.listImageHistoryCmd("non-existing").exec();
    }

    private File getDockerfile() {
        return new File(Thread.currentThread().getContextClassLoader()
                .getResource("historyTestDockerfile/Dockerfile").getFile());
    }
}
