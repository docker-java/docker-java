package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

public class ExposedPorts implements Serializable {
    private static final long serialVersionUID = 1L;

    private ExposedPort[] exposedPorts;

    public ExposedPorts(ExposedPort... exposedPorts) {
        this.exposedPorts = exposedPorts;
    }

    public ExposedPorts(List<ExposedPort> exposedPorts) {
        this.exposedPorts = exposedPorts.toArray(new ExposedPort[exposedPorts.size()]);
    }

    public ExposedPort[] getExposedPorts() {
        return exposedPorts;
    }

    @FromPrimitive
    public static ExposedPorts fromPrimitive(Map<String, Object> object) {
        return new ExposedPorts(
                object.keySet().stream().map(ExposedPort::parse).toArray(ExposedPort[]::new)
        );
    }

    @ToPrimitive
    public Map<String, Object> toPrimitive() {
        return Stream.of(exposedPorts).collect(Collectors.toMap(
                ExposedPort::toString,
                __ -> new Object()
        ));
    }

}
