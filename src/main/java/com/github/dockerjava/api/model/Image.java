package com.github.dockerjava.api.model;

import com.github.dockerjava.core.util.DockerImageName;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

	@JsonProperty("Created")
	private long created;

	@JsonProperty("Id")
    private String id;

	@JsonProperty("ParentId")
	private String parentId;

	@JsonProperty("RepoTags")
    private String[] repoTags;
    private DockerImageName[] imageNames;

    @JsonProperty("Size")
    private long size;

    @JsonProperty("VirtualSize")
    private long virtualSize;

    public String getId() {
        return id;
    }

    public DockerImageName[] getImageNames()
    {
        if(imageNames == null)
        {
            imageNames = new DockerImageName[repoTags.length];
            for(int i = 0;i<repoTags.length;i++)
            {
                imageNames[i] = new DockerImageName(repoTags[i]);
            }
        }
        return imageNames;
    }

    public String getParentId() {
        return parentId;
    }

    public long getCreated() {
        return created;
    }

    public long getSize() {
        return size;
    }

    public long getVirtualSize() {
        return virtualSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
