package com.github.dockerjava.jaxrs;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;

/**
 * Special {@link DockerCmdExecFactory} implementation that collects container and image creations
 * while test execution for the purpose of automatically cleanup.
 * 
 * @author marcus
 *
 */
public class TestDockerCmdExecFactory extends DockerCmdExecFactoryImpl {
	
	private List<String> containerIds = new ArrayList<String>();
	
	private List<String> imagesIds = new ArrayList<String>();
	
	@Override
	public CreateContainerCmd.Exec createCreateContainerCmdExec() {
		return new CreateContainerCmdExec(getBaseResource()) {
			@Override
			public CreateContainerResponse exec(CreateContainerCmd command) {
				CreateContainerResponse createContainerResponse = super.exec(command);
				containerIds.add(createContainerResponse.getId());
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
				containerIds.remove(command.getContainerId());
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
				imagesIds.add(createImageResponse.getId());
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
				imagesIds.remove(command.getImageId());
				return null;
			}
		};
	}
	
	public List<String> getContainerIds() {
		return new ArrayList<String>(containerIds);
	}
	
	public List<String> getImagesIds() {
		return new ArrayList<String>(imagesIds);
	}
	
	
}
