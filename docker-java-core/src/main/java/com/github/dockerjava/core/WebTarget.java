package com.github.dockerjava.core;

import java.util.Map;
import java.util.Set;

public interface WebTarget {

    WebTarget path(String... components);

    InvocationBuilder request();

    WebTarget resolveTemplate(String name, Object value);

    WebTarget queryParam(String name, Object value);

    WebTarget queryParamsSet(String name, Set<?> values);

    WebTarget queryParamsJsonMap(String name, Map<String, String> values);
}
