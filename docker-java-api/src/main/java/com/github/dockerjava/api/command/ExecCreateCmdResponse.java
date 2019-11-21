package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ExecCreateCmdResponse {

    @FieldName("Id")
    private String id;

    public String getId() {
        return id;
    }
}
