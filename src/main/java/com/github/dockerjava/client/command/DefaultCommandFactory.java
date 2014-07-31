package com.github.dockerjava.client.command;

import com.github.dockerjava.client.model.AuthConfig;

import java.io.File;
import java.io.InputStream;

public class DefaultCommandFactory implements CommandFactory {
    @Override
    public AttachContainerCmd attachContainerCmd(String containerId) {
        return new AttachContainerCmd(containerId);
    }

    @Override
    public AuthCmd authCmd(AuthConfig authConfig) {
        return new AuthCmd(authConfig);
    }

    @Override
    public BuildImgCmd buildImgCmd(File dockerFolder) {
        return new BuildImgCmd(dockerFolder);
    }

    @Override
    public BuildImgCmd buildImgCmd(InputStream tarInputStream) {
        return new BuildImgCmd(tarInputStream);
    }

    @Override
    public CommitCmd commitCmd(String containerId) {
        return new CommitCmd(containerId);
    }

    @Override
    public ContainerDiffCmd containerDiffCmd(String containerId) {
        return new ContainerDiffCmd(containerId);
    }

    @Override
    public CopyFileFromContainerCmd copyFileFromContainerCmd(String containerId, String resource) {
        return new CopyFileFromContainerCmd(containerId, resource);
    }

    @Override
    public CreateContainerCmd createContainerCmd(String image) {
        return new CreateContainerCmd(image);
    }

    @Override
    public CreateImageCmd createImageCmd(String repository, InputStream imageStream) {
        return new CreateImageCmd(repository, imageStream);
    }

    @Override
    public InfoCmd infoCmd() {
        return new InfoCmd();
    }

    @Override
    public InspectContainerCmd inspectContainerCmd(String containerId) {
        return new InspectContainerCmd(containerId);
    }

    @Override
    public InspectImageCmd inspectImageCmd(String imageId) {
        return new InspectImageCmd(imageId);
    }

    @Override
    public KillContainerCmd killContainerCmd(String containerId) {
        return new KillContainerCmd(containerId);
    }

    @Override
    public ListContainersCmd listContainersCmd() {
        return new ListContainersCmd();
    }

    @Override
    public ListImagesCmd listImagesCmd() {
        return new ListImagesCmd();
    }

    @Override
    public LogContainerCmd logContainerCmd(String containerId) {
        return new LogContainerCmd(containerId);
    }

    @Override
    public PauseContainerCmd pauseContainerCmd(String containerId) {
        return new PauseContainerCmd(containerId);
    }

    @Override
    public PullImageCmd pullImageCmd(String repository) {
        return new PullImageCmd(repository);
    }

    @Override
    public PushImageCmd pushImageCmd(String imageName) {
        return new PushImageCmd(imageName);
    }

    @Override
    public RemoveContainerCmd removeContainerCmd(String containerId) {
        return new RemoveContainerCmd(containerId);
    }

    @Override
    public RemoveImageCmd removeImageCmd(String imageId) {
        return new RemoveImageCmd(imageId);
    }

    @Override
    public RestartContainerCmd restartContainerCmd(String containerId) {
        return new RestartContainerCmd(containerId);
    }

    @Override
    public SearchImagesCmd searchImagesCmd(String searchTerm) {
        return new SearchImagesCmd(searchTerm);
    }

    @Override
    public StartContainerCmd startContainerCmd(String containerId) {
        return new StartContainerCmd(containerId);
    }

    @Override
    public StopContainerCmd stopContainerCmd(String containerId) {
        return new StopContainerCmd(containerId);
    }

    @Override
    public TagImageCmd tagImageCmd(String imageId, String repository, String tag) {
        return new TagImageCmd(imageId, repository, tag);
    }

    @Override
    public TopContainerCmd topContainerCmd(String containerId) {
        return new TopContainerCmd(containerId);
    }

    @Override
    public UnpauseContainerCmd unpauseContainerCmd(String containerId) {
        return new UnpauseContainerCmd(containerId);
    }

    @Override
    public VersionCmd versionCmd() {
        return new VersionCmd();
    }

    @Override
    public WaitContainerCmd waitContainerCmd(String containerId) {
        return new WaitContainerCmd(containerId);
    }

    @Override
    public PingCmd pingCmd() {
    	return new PingCmd();
    }
}
