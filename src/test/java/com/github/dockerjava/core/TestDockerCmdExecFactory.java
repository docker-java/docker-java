package com.github.dockerjava.core;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.command.AuthCmd.Exec;
import com.github.dockerjava.jaxrs.BuildImageCmdExec;

/**
 * Special {@link DockerCmdExecFactory} implementation that collects container
 * and image creations while test execution for the purpose of automatically
 * cleanup.
 * 
 * @author marcus
 * 
 */
public class TestDockerCmdExecFactory implements DockerCmdExecFactory {

	private List<String> containerNames = new ArrayList<String>();

	private List<String> imageNames = new ArrayList<String>();

	private DockerCmdExecFactory delegate;

	public TestDockerCmdExecFactory(DockerCmdExecFactory delegate) {
		this.delegate = delegate;
	}

	public void init(DockerClientConfig dockerClientConfig) {
		delegate.init(dockerClientConfig);
	}

	public void close() throws IOException {
		delegate.close();
	}

	public CreateContainerCmd.Exec createCreateContainerCmdExec() {
		return new CreateContainerCmd.Exec() {
			public CreateContainerResponse exec(CreateContainerCmd command) {
				CreateContainerResponse createContainerResponse = delegate
						.createCreateContainerCmdExec().exec(command);
				containerNames.add(createContainerResponse.getId());
				return createContainerResponse;
			}
		};
	}

	public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
		return new RemoveContainerCmd.Exec() {
			public Void exec(RemoveContainerCmd command) {
				delegate.createRemoveContainerCmdExec().exec(command);
				containerNames.remove(command.getContainerId());
				return null;
			}
		};
	}

	public CreateImageCmd.Exec createCreateImageCmdExec() {
		return new CreateImageCmd.Exec() {
			public CreateImageResponse exec(CreateImageCmd command) {
				CreateImageResponse createImageResponse = delegate
						.createCreateImageCmdExec().exec(command);
				imageNames.add(createImageResponse.getId());
				return createImageResponse;
			}
		};
	}

	public RemoveImageCmd.Exec createRemoveImageCmdExec() {
		return new RemoveImageCmd.Exec() {
			public Void exec(RemoveImageCmd command) {
				delegate.createRemoveImageCmdExec().exec(command);
				imageNames.remove(command.getImageId());
				return null;
			}
		};
	}

	public BuildImageCmd.Exec createBuildImageCmdExec() {
		return new BuildImageCmd.Exec() {
			public BuildImageCmd.Response exec(BuildImageCmd command) {
				// can't detect image id here so tagging it
				String tag = command.getTag();
				if (tag == null || "".equals(tag.trim())) {
					tag = "" + new SecureRandom().nextInt(Integer.MAX_VALUE);
					command.withTag(tag);
				}
				InputStream inputStream = delegate.createBuildImageCmdExec()
						.exec(command);
				imageNames.add(tag);
				return new BuildImageCmdExec.ResponseImpl(inputStream);
			}
		};
	}

	public Exec createAuthCmdExec() {
		return delegate.createAuthCmdExec();
	}

	public InfoCmd.Exec createInfoCmdExec() {
		return delegate.createInfoCmdExec();
	}

	public PingCmd.Exec createPingCmdExec() {
		return delegate.createPingCmdExec();
	}

	public ExecCreateCmd.Exec createExecCmdExec() {
		return delegate.createExecCmdExec();
	}

	public VersionCmd.Exec createVersionCmdExec() {
		return delegate.createVersionCmdExec();
	}

	public PullImageCmd.Exec createPullImageCmdExec() {
		return delegate.createPullImageCmdExec();
	}

	public PushImageCmd.Exec createPushImageCmdExec() {
		return delegate.createPushImageCmdExec();
	}

	public SaveImageCmd.Exec createSaveImageCmdExec() {
		return delegate.createSaveImageCmdExec();
	}

	public SearchImagesCmd.Exec createSearchImagesCmdExec() {
		return delegate.createSearchImagesCmdExec();
	}

	public ListImagesCmd.Exec createListImagesCmdExec() {
		return delegate.createListImagesCmdExec();
	}

	public InspectImageCmd.Exec createInspectImageCmdExec() {
		return delegate.createInspectImageCmdExec();
	}

	public ListContainersCmd.Exec createListContainersCmdExec() {
		return delegate.createListContainersCmdExec();
	}

	public StartContainerCmd.Exec createStartContainerCmdExec() {
		return delegate.createStartContainerCmdExec();
	}

	public InspectContainerCmd.Exec createInspectContainerCmdExec() {
		return delegate.createInspectContainerCmdExec();
	}

	public WaitContainerCmd.Exec createWaitContainerCmdExec() {
		return delegate.createWaitContainerCmdExec();
	}

	public AttachContainerCmd.Exec createAttachContainerCmdExec() {
		return delegate.createAttachContainerCmdExec();
	}

	public ExecStartCmd.Exec createExecStartCmdExec() {
		return delegate.createExecStartCmdExec();
	}

    public InspectExecCmd.Exec createInspectExecCmdExec() {
        return delegate.createInspectExecCmdExec();
    }

	public LogContainerCmd.Exec createLogContainerCmdExec() {
		return delegate.createLogContainerCmdExec();
	}

	public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
		return delegate.createCopyFileFromContainerCmdExec();
	}

	public StopContainerCmd.Exec createStopContainerCmdExec() {
		return delegate.createStopContainerCmdExec();
	}

	public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
		return delegate.createContainerDiffCmdExec();
	}

	public KillContainerCmd.Exec createKillContainerCmdExec() {
		return delegate.createKillContainerCmdExec();
	}

	public RestartContainerCmd.Exec createRestartContainerCmdExec() {
		return delegate.createRestartContainerCmdExec();
	}

	public CommitCmd.Exec createCommitCmdExec() {
		return delegate.createCommitCmdExec();
	}

	public TopContainerCmd.Exec createTopContainerCmdExec() {
		return delegate.createTopContainerCmdExec();
	}

	public TagImageCmd.Exec createTagImageCmdExec() {
		return delegate.createTagImageCmdExec();
	}

	public PauseContainerCmd.Exec createPauseContainerCmdExec() {
		return delegate.createPauseContainerCmdExec();
	}

	public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
		return delegate.createUnpauseContainerCmdExec();
	}

	public EventsCmd.Exec createEventsCmdExec() {
		return delegate.createEventsCmdExec();
	}

	public List<String> getContainerNames() {
		return new ArrayList<String>(containerNames);
	}

	public List<String> getImageNames() {
		return new ArrayList<String>(imageNames);
	}

	public StatsCmd.Exec createStatsCmdExec() {
		return delegate.createStatsCmdExec();
	}

}
