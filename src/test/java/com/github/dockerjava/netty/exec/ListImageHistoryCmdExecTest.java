package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ImageHistory;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_19;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ListImageHistoryCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void listImageHistory() throws DockerException {
        final File dockerfile = getDockerfile();

        final String tag = "history_test";

        final String imageIdDockerfile = dockerClient.buildImageCmd(dockerfile)
                .withNoCache(true)
                .withTag(tag)
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        final String containerId = dockerClient
                .createContainerCmd(imageIdDockerfile)
                .exec()
                .getId();

        final String comment = "myCommentWithoutSpaces";
        final String imageIdCommit = dockerClient.commitCmd(containerId).withMessage(comment).exec();

        final List<ImageHistory> history = dockerClient.listImageHistoryCmd(imageIdCommit).exec();

        assertThat(history, notNullValue());

        Long creationTimeStamp = MILLISECONDS.toSeconds(currentTimeMillis());
        for(ImageHistory entry : history){

            // history is ordered by creation timestamp descending
            Long currentCreationTimestamp = entry.getCreated();
            assertThat(currentCreationTimestamp, lessThanOrEqualTo(creationTimeStamp));
            creationTimeStamp = currentCreationTimestamp;

            assertThat(entry.getId(), not(isEmptyOrNullString()));
            assertThat(entry.getCreatedBy(), not(isEmptyOrNullString()));
        }

        final ImageHistory entryWithoutSizeTagsOrComment = history.get(2); // LABEL testLabel myTestLabel
        assertThat(entryWithoutSizeTagsOrComment.getCreatedBy(), both(containsString("#(nop)")).and(containsString("testLabel=myTestLabel")));

        final ImageHistory entryWithSizeAndTag = history.get(1); // RUN dd if=/dev/zero of=/myLargeFile bs=1M count=1
        assertThat(entryWithSizeAndTag.getCreatedBy(), containsString("dd if=/dev/zero of=/myLargeFile bs=1M count=1"));

        if (!getVersion(dockerClient).isGreaterOrEqual(VERSION_1_19)) {

            assertThat(entryWithoutSizeTagsOrComment.getSize(), is(0L));
            assertThat(entryWithoutSizeTagsOrComment.getTags(), nullValue());
            assertThat(entryWithoutSizeTagsOrComment.getComment(), isEmptyString());

            final Long oneMegaByteInBytes = 1024L * 1024L;
            assertThat(entryWithSizeAndTag.getSize(), greaterThanOrEqualTo(oneMegaByteInBytes));
            final List<String> tags = entryWithSizeAndTag.getTags();
            assertThat(tags, notNullValue());
            assertThat(tags.get(0), containsString(tag + ":latest"));
            assertThat(entryWithSizeAndTag.getComment(), isEmptyString());

            final ImageHistory entryWithComment = history.get(0);
            assertThat(entryWithComment.getSize(), is(0L));
            assertThat(entryWithComment.getComment(), equalToIgnoringCase(comment));
        }
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void listImageHistoryOfNonExistingImage(){
        dockerClient.listImageHistoryCmd("non-existing").exec();
    }

    private File getDockerfile() {
        return new File(Thread.currentThread().getContextClassLoader()
                .getResource("historyTestDockerfile/Dockerfile").getFile());
    }
}
