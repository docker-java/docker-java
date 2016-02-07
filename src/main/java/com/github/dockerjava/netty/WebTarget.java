package com.github.dockerjava.netty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This class is basically a replacement of javax.ws.rs.client.WebTarget to allow simpler migration of JAX-RS code to a netty based
 * implementation.
 *
 * @author Marcus Linke
 */
public class WebTarget {

    private ChannelProvider channelProvider;

    private List<String> path = new ArrayList<String>();

    private Map<String, String> queryParams = new HashMap<String, String>();

    private static final String PATH_SEPARATOR = "/";

    public WebTarget(ChannelProvider channelProvider) {
        this.channelProvider = channelProvider;
    }

    public WebTarget path(String... components) {

        for (String component : components) {

            path.addAll(Arrays.asList(StringUtils.split(component, PATH_SEPARATOR)));
        }

        return this;
    }

    public InvocationBuilder request() {
        String resource = PATH_SEPARATOR + StringUtils.join(path, PATH_SEPARATOR);

        List<String> params = new ArrayList<String>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.add(entry.getKey() + "=" + entry.getValue());
        }

        if (!params.isEmpty()) {
            resource = resource + "?" + StringUtils.join(params, "&");
        }

        return new InvocationBuilder(channelProvider, resource);
    }

    public WebTarget resolveTemplate(String name, Object value) {
        List<String> newPath = new ArrayList<String>();
        for (String component : path) {
            component = component.replaceAll("\\{" + name + "\\}", value.toString());
            newPath.add(component);
        }
        path = newPath;
        return this;
    }

    public WebTarget queryParam(String name, Object value) {
        if (value != null) {
            queryParams.put(name, value.toString());
        }
        return this;
    }

}
