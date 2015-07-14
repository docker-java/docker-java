package com.github.dockerjava.api;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.ExecCreateCmd;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectExecCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.api.model.Statistics;

// https://godoc.org/github.com/fsouza/go-dockerclient
public interface DockerClient extends Closeable {

    public AuthConfig authConfig() throws DockerException;

    /**
     * Authenticate with the server, useful for checking authentication.
     */
    public AuthCmd authCmd();

    public InfoCmd infoCmd();

    public PingCmd pingCmd();

    public VersionCmd versionCmd();

    /**
     * * IMAGE API *
     */

    public PullImageCmd pullImageCmd(String repository, ResultCallback<PullResponseItem> resultCallback);

    public PushImageCmd pushImageCmd(String name, ResultCallback<PushResponseItem> resultCallback);

    public PushImageCmd pushImageCmd(Identifier identifier, ResultCallback<PushResponseItem> resultCallback);

    public CreateImageCmd createImageCmd(String repository, InputStream imageStream);

    public SearchImagesCmd searchImagesCmd(String term);

    public RemoveImageCmd removeImageCmd(String imageId);

    public ListImagesCmd listImagesCmd();

    public InspectImageCmd inspectImageCmd(String imageId);

    public SaveImageCmd saveImageCmd(String name);

    /**
     * * CONTAINER API *
     */

    public ListContainersCmd listContainersCmd();

    public CreateContainerCmd createContainerCmd(String image);

    /**
     * Creates a new {@link StartContainerCmd} for the container with the given ID. The command can then be further
     * customized by using builder methods on it like {@link StartContainerCmd#withDns(String...)}.
     * <p>
     * <b>If you customize the command, any existing configuration of the target container will get reset to its default
     * before applying the new configuration. To preserve the existing configuration, use an unconfigured
     * {@link StartContainerCmd}.</b>
     * <p>
     * This command corresponds to the <code>/containers/{id}/start</code> endpoint of the Docker Remote API.
     */
    public StartContainerCmd startContainerCmd(String containerId);

    public ExecCreateCmd execCreateCmd(String containerId);

    public InspectContainerCmd inspectContainerCmd(String containerId);

    public RemoveContainerCmd removeContainerCmd(String containerId);

    public WaitContainerCmd waitContainerCmd(String containerId);

    public AttachContainerCmd attachContainerCmd(String containerId, ResultCallback<Frame> resultCallback);

    public ExecStartCmd execStartCmd(String containerId);

    public InspectExecCmd inspectExecCmd(String execId);

    public LogContainerCmd logContainerCmd(String containerId, ResultCallback<Frame> resultCallback);

    public CopyFileFromContainerCmd copyFileFromContainerCmd(String containerId, String resource);

    public ContainerDiffCmd containerDiffCmd(String containerId);

    public StopContainerCmd stopContainerCmd(String containerId);

    public KillContainerCmd killContainerCmd(String containerId);

    public RestartContainerCmd restartContainerCmd(String containerId);

    public CommitCmd commitCmd(String containerId);

    public BuildImageCmd buildImageCmd(ResultCallback<BuildResponseItem> resultCallback);

    public BuildImageCmd buildImageCmd(File dockerFileOrFolder, ResultCallback<BuildResponseItem> resultCallback);

    public BuildImageCmd buildImageCmd(InputStream tarInputStream, ResultCallback<BuildResponseItem> resultCallback);

    public TopContainerCmd topContainerCmd(String containerId);

    public TagImageCmd tagImageCmd(String imageId, String repository, String tag);

    public PauseContainerCmd pauseContainerCmd(String containerId);

    public UnpauseContainerCmd unpauseContainerCmd(String containerId);

    public EventsCmd eventsCmd(ResultCallback<Event> resultCallback);

    public StatsCmd statsCmd(ResultCallback<Statistics> resultCallback);

    @Override
    public void close() throws IOException;

}
