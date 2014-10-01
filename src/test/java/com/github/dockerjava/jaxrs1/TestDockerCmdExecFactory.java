package com.github.dockerjava.jaxrs1;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.jaxrs1.BuildImageCmdExec;
import com.github.dockerjava.jaxrs1.CreateContainerCmdExec;
import com.github.dockerjava.jaxrs1.CreateImageCmdExec;
import com.github.dockerjava.jaxrs1.DockerCmdExecFactoryImpl;
import com.github.dockerjava.jaxrs1.RemoveContainerCmdExec;
import com.github.dockerjava.jaxrs1.RemoveImageCmdExec;

/**
 * Special {@link DockerCmdExecFactory} implementation that collects container and image creations
 * while test execution for the purpose of automatically cleanup.
 * 
 * @author marcus
 *
 */
public class TestDockerCmdExecFactory extends DockerCmdExecFactoryImpl {
	
	private List<String> containerNames = new ArrayList<String>();
	
	private List<String> imageNames = new ArrayList<String>();
	
	@Override
	public CreateContainerCmd.Exec createCreateContainerCmdExec() {
		return new CreateContainerCmdExec(getBaseResource()) {
			@Override
			public CreateContainerResponse exec(CreateContainerCmd command) {
				CreateContainerResponse createContainerResponse = super.exec(command);
				containerNames.add(createContainerResponse.getId());
				return createContainerResponse;
			}
		};
	}
	
	@Override
	public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
		return new RemoveContainerCmdExec(getBaseResource()) {
			@Override
			public Void exec(RemoveContainerCmd command) {
				super.exec(command);
				containerNames.remove(command.getContainerId());
				return null;
			}
		};
	}
	
	@Override
	public CreateImageCmd.Exec createCreateImageCmdExec() {
		return new CreateImageCmdExec(getBaseResource()) {
			@Override
			public CreateImageResponse exec(CreateImageCmd command) {
				CreateImageResponse createImageResponse = super.exec(command);
				imageNames.add(createImageResponse.getId());
				return createImageResponse;
			}
		};
	}
	
	
	
	@Override
	public RemoveImageCmd.Exec createRemoveImageCmdExec() {
		return new RemoveImageCmdExec(getBaseResource()) {
			@Override
			public Void exec(RemoveImageCmd command) {
				super.exec(command);
				imageNames.remove(command.getImageId());
				return null;
			}
		};
	}
	
	@Override
	public BuildImageCmd.Exec createBuildImageCmdExec() {
		return new BuildImageCmdExec(getBaseResource()) {
			@Override
			public InputStream exec(BuildImageCmd command) {
				// can't detect image id here so tagging it
				String tag = command.getTag();
				if(tag == null || "".equals(tag.trim())) {
					tag = "" + new SecureRandom().nextInt(Integer.MAX_VALUE);
					command.withTag(tag);
				}
				InputStream inputStream = super.exec(command); 
				imageNames.add(tag);
				return inputStream;
			}
		};
	}
	
	public List<String> getContainerNames() {
		return new ArrayList<String>(containerNames);
	}
	
	public List<String> getImageNames() {
		return new ArrayList<String>(imageNames);
	}
	
}
