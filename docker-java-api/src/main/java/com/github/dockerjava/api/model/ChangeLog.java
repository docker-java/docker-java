package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class ChangeLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Path")
    private String path;

    @FieldName("Kind")
    private Integer kind;

    public String getPath() {
        return path;
    }

    public Integer getKind() {
        return kind;
    }
}
