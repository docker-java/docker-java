package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Marcus Linke
 */
@EqualsAndHashCode
@ToString
public class CreateVolumeResponse {

    @FieldName("Name")
    private String name;

    @FieldName("Driver")
    private String driver;

    @FieldName("Mountpoint")
    private String mountpoint;

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }

    public String getMountpoint() {
        return mountpoint;
    }
}
