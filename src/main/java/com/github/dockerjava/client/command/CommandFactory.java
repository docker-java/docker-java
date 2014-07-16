package com.github.dockerjava.client.command;

import com.github.dockerjava.client.model.AuthConfig;

import java.io.File;
import java.io.InputStream;

public interface CommandFactory {
    public AttachContainerCmd attachContainerCmd(String containerId);
    public AuthCmd authCmd(AuthConfig authConfig);
    public BuildImgCmd buildImgCmd(File dockerFolder);
    public BuildImgCmd buildImgCmd(InputStream tarInputStream);
    public CommitCmd commitCmd(String containerId);
    public ContainerDiffCmd containerDiffCmd(String containerId);
    public CopyFileFromContainerCmd copyFileFromContainerCmd(String containerId, String resource);
    public CreateContainerCmd createContainerCmd(String image);
    public ImportImageCmd importImageCmd(String repository, InputStream imageStream);
    public InfoCmd infoCmd();
    public InspectContainerCmd inspectContainerCmd(String containerId);
    public InspectImageCmd inspectImageCmd(String imageId);
    public KillContainerCmd killContainerCmd(String containerId);
    public ListContainersCmd listContainersCmd();
    public ListImagesCmd listImagesCmd();
    public LogContainerCmd logContainerCmd(String containerId);
    public PauseContainerCmd pauseContainerCmd(String containerId);
    public PullImageCmd pullImageCmd(String repository);
    public PushImageCmd pushImageCmd(String imageName);
    public RemoveContainerCmd removeContainerCmd(String containerId);
    public RemoveImageCmd removeImageCmd(String imageId);
    public RestartContainerCmd restartContainerCmd(String containerId);
    public SearchImagesCmd searchImagesCmd(String searchTerm);
    public StartContainerCmd startContainerCmd(String containerId);
    public StopContainerCmd stopContainerCmd(String containerId);
    public TagImageCmd tagImageCmd(String imageId, String repository, String tag);
    public TopContainerCmd topContainerCmd(String containerId);
    public UnpauseContainerCmd unpauseContainerCmd(String containerId);
    public VersionCmd versionCmd();
    public WaitContainerCmd waitContainerCmd(String containerId);
}
