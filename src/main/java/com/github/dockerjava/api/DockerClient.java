package com.github.dockerjava.api;

import java.io.*;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.AuthConfig;

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

	public PullImageCmd pullImageCmd(String repository);

	public PushImageCmd pushImageCmd(String name);

	public CreateImageCmd createImageCmd(String repository,
			InputStream imageStream);

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
	 * Creates a new {@link StartContainerCmd} for the container with the
	 * given ID.
	 * The command can then be further customized by using builder
	 * methods on it like {@link StartContainerCmd#withDns(String...)}.
	 * <p>
	 * <b>If you customize the command, any existing configuration of the
	 * target container will get reset to its default before applying the
	 * new configuration. To preserve the existing configuration, use an 
	 * unconfigured {@link StartContainerCmd}.</b> 
	 * <p>
	 * This command corresponds to the <code>/containers/{id}/start</code>
	 * endpoint of the Docker Remote API.
	 */
	public StartContainerCmd startContainerCmd(String containerId);

    public ExecCreateCmd execCreateCmd(String containerId);

	public InspectContainerCmd inspectContainerCmd(String containerId);

	public RemoveContainerCmd removeContainerCmd(String containerId);

	public WaitContainerCmd waitContainerCmd(String containerId);

	public AttachContainerCmd attachContainerCmd(String containerId);

    public ExecStartCmd execStartCmd(String containerId);

	public LogContainerCmd logContainerCmd(String containerId);

	public CopyFileFromContainerCmd copyFileFromContainerCmd(
			String containerId, String resource);

	public ContainerDiffCmd containerDiffCmd(String containerId);

	public StopContainerCmd stopContainerCmd(String containerId);

	public KillContainerCmd killContainerCmd(String containerId);

	public RestartContainerCmd restartContainerCmd(String containerId);

	public CommitCmd commitCmd(String containerId);

	public BuildImageCmd buildImageCmd(File dockerFolder);

	public BuildImageCmd buildImageCmd(InputStream tarInputStream);

	public TopContainerCmd topContainerCmd(String containerId);

	public TagImageCmd tagImageCmd(String imageId, String repository, String tag);
	
	public PauseContainerCmd pauseContainerCmd(String containerId);
	
	public UnpauseContainerCmd unpauseContainerCmd(String containerId);

	public EventsCmd eventsCmd(EventCallback eventCallback);

	@Override
	public void close() throws IOException;

}