package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.github.dockerjava.utils.LogContainerTestCallback;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

public class CopyArchiveToContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(CopyArchiveToContainerCmdIT.class);

    @Test
    public void copyFileToContainer() throws Exception {
        CreateContainerResponse container = prepareContainerForCopy("1");
        Path temp = Files.createTempFile("", ".tar.gz");
        CompressArchiveUtil.tar(Paths.get(ClassLoader.getSystemResource("testReadFile").toURI()), temp, true, false);
        try (InputStream uploadStream = Files.newInputStream(temp)) {
            dockerRule.getClient()
                    .copyArchiveToContainerCmd(container.getId())
                    .withTarInputStream(uploadStream)
                    .exec();
            assertFileCopied(container);
        }
    }

    @Test
    public void copyStreamToContainer() throws Exception {
        CreateContainerResponse container = prepareContainerForCopy("2");
        dockerRule.getClient().copyArchiveToContainerCmd(container.getId())
            .withHostResource(Paths.get(ClassLoader.getSystemResource("testReadFile").toURI()).toString())
            .exec();
        assertFileCopied(container);
    }

    @Test
    public void copyStreamToContainerTwice() throws Exception {
        CreateContainerResponse container = prepareContainerForCopy("rerun");
        CopyArchiveToContainerCmd copyArchiveToContainerCmd = dockerRule.getClient().copyArchiveToContainerCmd(container.getId())
            .withHostResource(Paths.get(ClassLoader.getSystemResource("testReadFile").toURI()).toString());
        copyArchiveToContainerCmd.exec();
        assertFileCopied(container);
        //run again to make sure no DockerClientException
        copyArchiveToContainerCmd.exec();
    }

    private CreateContainerResponse prepareContainerForCopy(String method) {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withName("docker-java-itest-copyToContainer" + method)
                .exec();
        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        // Copy a folder to the container
        return container;
    }

    private void assertFileCopied(CreateContainerResponse container) throws IOException {
        try (InputStream response = dockerRule.getClient().copyArchiveFromContainerCmd(container.getId(), "testReadFile").exec()) {
            boolean bytesAvailable = response.read() != -1;
            assertTrue( "The file was not copied to the container.", bytesAvailable);
        }
    }

    @Test(expected = NotFoundException.class)
    public void copyToNonExistingContainer() {
        dockerRule.getClient().copyArchiveToContainerCmd("non-existing")
            .withHostResource(Paths.get(ClassLoader.getSystemResource("testReadFile").toURI()).toString()).exec();
    }

    @Test
    public void copyDirWithLastAddedTarEntryEmptyDir() throws Exception{
        // create a temp dir
        Path localDir = Files.createTempDirectory(null);
        localDir.toFile().deleteOnExit();
        // create empty sub-dir with name b
        Files.createDirectory(localDir.resolve("b"));
        // create sub-dir with name a
        Path dirWithFile = Files.createDirectory(localDir.resolve("a"));
        // create file in sub-dir b, name or conter are irrelevant
        Files.createFile(dirWithFile.resolve("file"));

        // create a test container
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .exec();
        // start the container
        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        // copy data from local dir to container
        dockerRule.getClient().copyArchiveToContainerCmd(container.getId())
                .withHostResource(localDir.toString())
                .exec();

        // cleanup dir
        FileUtils.deleteDirectory(localDir.toFile());
    }
    
    @Test
    public void copyFileWithExecutePermission() throws Exception {
        // create script file, add permission to execute
        Path scriptPath = Files.createTempFile("run", ".sh");
        boolean executable = scriptPath.toFile().setExecutable(true, false);
        if (!executable){
            throw new Exception("Execute permission on file not set!");
        }
        String snippet = "Running script with execute permission.";
        String scriptTextStr = "#!/bin/sh\necho \"" + snippet + "\"";
        // write content for created script
        Files.write(scriptPath, scriptTextStr.getBytes());
        // create a test container which starts and waits 3 seconds for the
        // script to be copied to the container's home dir and then executes it
        String containerCmd = "sleep 3; /home/" + scriptPath.getFileName().toString();
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withName("copyFileWithExecutivePerm")
                .withCmd("/bin/sh", "-c", containerCmd)
                .exec();
        // start the container
        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        // copy script to container home dir
        dockerRule.getClient().copyArchiveToContainerCmd(container.getId())
                .withRemotePath("/home")
                .withHostResource(scriptPath.toString())
                .exec();
        // await exid code
        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId())
                .start()
                .awaitStatusCode();
        // check result
        assertThat(exitCode, equalTo(0));
    }

    @Test
    public void copyFileWithUIDGID() throws Exception {
        Path with = Files.createFile(Files.createTempDirectory("copyFileWithUIDGID").resolve("uidgid.with"));
        Files.write(with, "with".getBytes());

        Path without = Files.createFile(Files.createTempDirectory("copyFileWithUIDGID").resolve("uidgid.without"));
        Files.write(without, "without".getBytes());

        String containerCmd = "while [ ! -f /home/uidgid.with ]; do true; done && stat -c %n:%u /home/uidgid.with /home/uidgid.without";
        Long syncUserUid = 4L; // sync user in busybox uses uid=4
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withName("copyFileWithUIDGID")
                .withCmd("/bin/sh", "-c", containerCmd)
                .withUser(syncUserUid.toString())
                .exec();
        // start the container
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        dockerRule.getClient().copyArchiveToContainerCmd(container.getId())
                .withRemotePath("/home/")
                .withHostResource(without.toString())
                .withCopyUIDGID(false)
                .exec();
        dockerRule.getClient().copyArchiveToContainerCmd(container.getId())
                .withRemotePath("/home/")
                .withHostResource(with.toString())
                .withCopyUIDGID(true)
                .exec();

        // await exit code
        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId())
                .start()
                .awaitStatusCode();
        // check result
        assertThat(exitCode, equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback(true);

        dockerRule.getClient().logContainerCmd(container.getId())
            .withStdOut(true)
            .withTailAll()
            .exec(loggingCallback);

        loggingCallback.awaitCompletion(3, TimeUnit.SECONDS);
        String containerOutput = loggingCallback.toString();

        assertThat(containerOutput, containsString(String.format("/home/uidgid.with:%d", syncUserUid)));

        Long hostUid = getHostUidIfPossible();
        assumeThat("could not get the uid on host platform", hostUid, notNullValue(Long.class));
        assertThat(containerOutput, containsString(String.format("/home/uidgid.without:%d", hostUid)));
    }

    private static Long getHostUidIfPossible() {
        try {
            Class<?> unixSystemClazz = Class.forName("com.sun.security.auth.module.UnixSystem");
            Object unixSystem = unixSystemClazz.newInstance();
            Object uid = unixSystemClazz.getMethod("getUid").invoke(unixSystem);
            if (uid == null) {
                return null;
            }

            return uid instanceof Long ? (Long) uid : Long.parseLong(uid.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
