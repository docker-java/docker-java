package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.core.util.DockerImageName;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.CreateImageResponse;

/**
 * Create an image by importing the given stream of a tar file.
 */
public class CreateImageCmdImpl extends	AbstrDockerCmd<CreateImageCmd, CreateImageResponse> implements CreateImageCmd {

	private DockerImageName imageName;
	
	private InputStream imageStream;

	/**
	 * @param imageStream       the InputStream of the tar file
	 */
	public CreateImageCmdImpl(CreateImageCmd.Exec exec, DockerImageName imageName, InputStream imageStream) {
		super(exec);
		withImageName(imageName);
		withImageStream(imageStream);
	}

    @Override
	public DockerImageName getImageName() {
        return imageName;
    }
    
    @Override
    public InputStream getImageStream() {
    	return imageStream;
    }

    /**
	 * @param imageName        the repository to import to
	 */
	@Override
	public CreateImageCmdImpl withImageName(DockerImageName imageName) {
		checkNotNull(imageName, "imageName was not specified");
		this.imageName = imageName;
		return this;
	}

	/**
	 * @param imageStream       the InputStream of the tar file
	 */
	@Override
	public CreateImageCmdImpl withImageStream(InputStream imageStream) {
		checkNotNull(imageStream, "imageStream was not specified");
		this.imageStream = imageStream;
		return this;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this).append("import - ")
            .append(imageName)
            .toString();
    }
}
