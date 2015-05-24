package com.github.dockerjava.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.Identifier;
import com.github.dockerjava.core.command.*;

/**
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 * @see "https://github.com/docker/docker/blob/master/api/client/commands.go"
 */
public class DockerClientImpl implements Closeable, DockerClient {

	private final DockerClientConfig dockerClientConfig;

	private DockerCmdExecFactory dockerCmdExecFactory;

	private DockerClientImpl() {
		this(DockerClientConfig.createDefaultConfigBuilder().build());
	}

	private DockerClientImpl(String serverUrl) {
		this(configWithServerUrl(serverUrl));
	}

	private DockerClientImpl(DockerClientConfig dockerClientConfig) {
		checkNotNull(dockerClientConfig,
				"config was not specified");
		this.dockerClientConfig = dockerClientConfig;
	}

	private static DockerClientConfig configWithServerUrl(String serverUrl) {
		return DockerClientConfig.createDefaultConfigBuilder()
				.withUri(serverUrl).build();
	}

	public static DockerClientImpl getInstance() {
		return new DockerClientImpl();
	}

	public static DockerClientImpl getInstance(
			DockerClientConfig dockerClientConfig) {
		return new DockerClientImpl(dockerClientConfig);
	}

	public static DockerClientImpl getInstance(String serverUrl) {
		return new DockerClientImpl(serverUrl);
	}

	public DockerClientImpl withDockerCmdExecFactory(
			DockerCmdExecFactory dockerCmdExecFactory) {
		checkNotNull(dockerCmdExecFactory,
				"dockerCmdExecFactory was not specified");
		this.dockerCmdExecFactory = dockerCmdExecFactory;
		this.dockerCmdExecFactory.init(dockerClientConfig);
		return this;
	}

	private DockerCmdExecFactory getDockerCmdExecFactory() {
		checkNotNull(dockerCmdExecFactory,
				"dockerCmdExecFactory was not specified");
		return dockerCmdExecFactory;
	}

	public AuthConfig authConfig() {
		checkNotNull(dockerClientConfig.getUsername(),
				"Configured username is null.");
		checkNotNull(dockerClientConfig.getServerAddress(),
				"Configured serverAddress is null.");

		AuthConfig authConfig = new AuthConfig();
		authConfig.setUsername(dockerClientConfig.getUsername());
		authConfig.setPassword(dockerClientConfig.getPassword());
		authConfig.setEmail(dockerClientConfig.getEmail());
		authConfig.setServerAddress(dockerClientConfig.getServerAddress());

		return authConfig;
	}

	/**
	 * * MISC API *
	 */

	/**
	 * Authenticate with the server, useful for checking authentication.
	 */
	public AuthCmd authCmd() {
		return new AuthCmdImpl(getDockerCmdExecFactory().createAuthCmdExec(),
				authConfig());
	}

	public InfoCmd infoCmd() {
		return new InfoCmdImpl(getDockerCmdExecFactory().createInfoCmdExec());
	}

	
	
	public PingCmd pingCmd() {
		return new PingCmdImpl(getDockerCmdExecFactory().createPingCmdExec());
	}

	public VersionCmd versionCmd() {
		return new VersionCmdImpl(getDockerCmdExecFactory()
				.createVersionCmdExec());
	}

	/**
	 * * IMAGE API *
	 */
	public PullImageCmd pullImageCmd(String repository) {
		return new PullImageCmdImpl(getDockerCmdExecFactory()
				.createPullImageCmdExec(), dockerClientConfig.effectiveAuthConfig(repository), repository);
	}

	public PushImageCmd pushImageCmd(String name) {
          PushImageCmd cmd = new PushImageCmdImpl(getDockerCmdExecFactory()
				.createPushImageCmdExec(), name);

          AuthConfig cfg = dockerClientConfig.effectiveAuthConfig(name);
          if( cfg != null )
            cmd.withAuthConfig(cfg);
          return cmd;
	}

        public PushImageCmd pushImageCmd(Identifier identifier) {
          PushImageCmd cmd = pushImageCmd(identifier.repository.name);
          if( identifier.tag.isPresent() )
            cmd.withTag(identifier.tag.get());

          AuthConfig cfg = dockerClientConfig.effectiveAuthConfig(identifier.repository.name);
          if( cfg != null )
            cmd.withAuthConfig(cfg);

          return cmd;
        }

    public SaveImageCmd saveImageCmd(String name) {
        return new SaveImageCmdImpl(getDockerCmdExecFactory().createSaveImageCmdExec(), name);
    }

	public CreateImageCmd createImageCmd(String repository,
			InputStream imageStream) {
		return new CreateImageCmdImpl(getDockerCmdExecFactory()
				.createCreateImageCmdExec(), repository, imageStream);
	}

	public SearchImagesCmd searchImagesCmd(String term) {
		return new SearchImagesCmdImpl(getDockerCmdExecFactory()
				.createSearchImagesCmdExec(), term);
	}

	public RemoveImageCmd removeImageCmd(String imageId) {
		return new RemoveImageCmdImpl(getDockerCmdExecFactory()
				.createRemoveImageCmdExec(), imageId);
	}

	public ListImagesCmd listImagesCmd() {
		return new ListImagesCmdImpl(getDockerCmdExecFactory()
				.createListImagesCmdExec());
	}

	public InspectImageCmd inspectImageCmd(String imageId) {
		return new InspectImageCmdImpl(getDockerCmdExecFactory()
				.createInspectImageCmdExec(), imageId);
	}

	/**
	 * * CONTAINER API *
	 */
	public ListContainersCmd listContainersCmd() {
		return new ListContainersCmdImpl(getDockerCmdExecFactory()
				.createListContainersCmdExec());
	}

	public CreateContainerCmd createContainerCmd(String image) {
		return new CreateContainerCmdImpl(getDockerCmdExecFactory()
				.createCreateContainerCmdExec(), image);
	}

	public StartContainerCmd startContainerCmd(String containerId) {
		return new StartContainerCmdImpl(getDockerCmdExecFactory()
				.createStartContainerCmdExec(), containerId);
	}

	public InspectContainerCmd inspectContainerCmd(String containerId) {
		return new InspectContainerCmdImpl(getDockerCmdExecFactory()
				.createInspectContainerCmdExec(), containerId);
	}

    public ExecCreateCmd execCreateCmd(String containerId) {
        return new ExecCreateCmdImpl(getDockerCmdExecFactory().createExecCmdExec(), containerId);
    }

	public RemoveContainerCmd removeContainerCmd(String containerId) {
		return new RemoveContainerCmdImpl(getDockerCmdExecFactory()
				.createRemoveContainerCmdExec(), containerId);
	}

	public WaitContainerCmd waitContainerCmd(String containerId) {
		return new WaitContainerCmdImpl(getDockerCmdExecFactory()
				.createWaitContainerCmdExec(), containerId);
	}

	public AttachContainerCmd attachContainerCmd(String containerId) {
		return new AttachContainerCmdImpl(getDockerCmdExecFactory()
				.createAttachContainerCmdExec(), containerId);
	}

    public ExecStartCmd execStartCmd(String containerId) {
        return new ExecStartCmdImpl(getDockerCmdExecFactory().createExecStartCmdExec(), containerId);
    }

    public InspectExecCmd inspectExecCmd(String execId) {
        return new InspectExecCmdImpl(getDockerCmdExecFactory().createInspectExecCmdExec(), execId);
    }

	public LogContainerCmd logContainerCmd(String containerId) {
		return new LogContainerCmdImpl(getDockerCmdExecFactory()
				.createLogContainerCmdExec(), containerId);
	}
    
   

	public CopyFileFromContainerCmd copyFileFromContainerCmd(
			String containerId, String resource) {
		return new CopyFileFromContainerCmdImpl(getDockerCmdExecFactory()
				.createCopyFileFromContainerCmdExec(), containerId, resource);
	}

	public ContainerDiffCmd containerDiffCmd(String containerId) {
		return new ContainerDiffCmdImpl(getDockerCmdExecFactory()
				.createContainerDiffCmdExec(), containerId);
	}

	public StopContainerCmd stopContainerCmd(String containerId) {
		return new StopContainerCmdImpl(getDockerCmdExecFactory()
				.createStopContainerCmdExec(), containerId);
	}

	public KillContainerCmd killContainerCmd(String containerId) {
		return new KillContainerCmdImpl(getDockerCmdExecFactory()
				.createKillContainerCmdExec(), containerId);
	}

	public RestartContainerCmd restartContainerCmd(String containerId) {
		return new RestartContainerCmdImpl(getDockerCmdExecFactory()
				.createRestartContainerCmdExec(), containerId);
	}

	public CommitCmd commitCmd(String containerId) {
		return new CommitCmdImpl(getDockerCmdExecFactory()
				.createCommitCmdExec(), containerId);
	}

    public BuildImageCmd buildImageCmd() {
        return augmentBuildImageCmd(new BuildImageCmdImpl(getDockerCmdExecFactory()
                .createBuildImageCmdExec()));
    }

	public BuildImageCmd buildImageCmd(File dockerFileOrFolder) {
		return augmentBuildImageCmd(new BuildImageCmdImpl(getDockerCmdExecFactory()
				.createBuildImageCmdExec(), dockerFileOrFolder));
	}

    public BuildImageCmd buildImageCmd(InputStream tarInputStream) {
        return augmentBuildImageCmd(new BuildImageCmdImpl(getDockerCmdExecFactory()
				.createBuildImageCmdExec(), tarInputStream));
    }

	private BuildImageCmd augmentBuildImageCmd(BuildImageCmd buildImageCmd) {
		final AuthConfigurations authConfigurations = dockerClientConfig.getAuthConfigurations();
		if (!authConfigurations.getConfigs().isEmpty()) {
			buildImageCmd.withBuildAuthConfigs(authConfigurations);
		}

		return buildImageCmd;
	}

	
	public TopContainerCmd topContainerCmd(String containerId) {
		return new TopContainerCmdImpl(getDockerCmdExecFactory()
				.createTopContainerCmdExec(), containerId);
	}

	public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
		return new TagImageCmdImpl(getDockerCmdExecFactory()
				.createTagImageCmdExec(), imageId, repository, tag);
	}

	public PauseContainerCmd pauseContainerCmd(String containerId) {
		return new PauseContainerCmdImpl(getDockerCmdExecFactory()
				.createPauseContainerCmdExec(), containerId);
	}

	
	public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
		return new UnpauseContainerCmdImpl(getDockerCmdExecFactory()
				.createUnpauseContainerCmdExec(), containerId);
	}

	public StatsCmd statsCmd(String containerId) {
		return new StatsCmdImpl(getDockerCmdExecFactory()
				.createStatsCmdExec(), containerId);
	}
	
	public EventsCmd eventsCmd(EventCallback eventCallback) {
		return new EventsCmdImpl(getDockerCmdExecFactory()
				.createEventsCmdExec(), eventCallback);
	}

	public void close() throws IOException {
		getDockerCmdExecFactory().close();
	}
	
}
