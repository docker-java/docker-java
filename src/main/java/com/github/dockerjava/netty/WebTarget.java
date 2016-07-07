package com.github.dockerjava.netty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * This class is basically a replacement of javax.ws.rs.client.WebTarget to allow simpler migration of JAX-RS code to a netty based
 * implementation.
 *
 * @author Marcus Linke
 */
public class WebTarget {

    private final ChannelProvider channelProvider;

    private final ImmutableList<String> path;

    private final ImmutableMap<String, String> queryParams;

    private static final String PATH_SEPARATOR = "/";

    public WebTarget(ChannelProvider channelProvider) {
        this(channelProvider, ImmutableList.<String>of(), ImmutableMap.<String, String>of());
    }

    private WebTarget(ChannelProvider channelProvider,
                      ImmutableList<String> path,
                      ImmutableMap<String, String> queryParams) {
        this.channelProvider = channelProvider;
        this.path = path;
        this.queryParams = queryParams;
    }

    public WebTarget path(String... components) {
        ImmutableList.Builder<String> newPath = ImmutableList.<String>builder().addAll(this.path);

        for (String component : components) {
            newPath.addAll(Arrays.asList(StringUtils.split(component, PATH_SEPARATOR)));
        }

        return new WebTarget(channelProvider, newPath.build(), queryParams);
    }

    public InvocationBuilder request() {
        String resource = PATH_SEPARATOR + StringUtils.join(path, PATH_SEPARATOR);

        List<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.add(entry.getKey() + "=" + entry.getValue());
        }

        if (!params.isEmpty()) {
            resource = resource + "?" + StringUtils.join(params, "&");
        }

        return new InvocationBuilder(channelProvider, resource);
    }

    public WebTarget resolveTemplate(String name, Object value) {
        ImmutableList.Builder<String> newPath = ImmutableList.builder();
        for (String component : path) {
            component = component.replaceAll("\\{" + name + "\\}", value.toString());
            newPath.add(component);
        }
        return new WebTarget(channelProvider, newPath.build(), queryParams);
    }

    public WebTarget queryParam(String name, Object value) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder().putAll(queryParams);
        if (value != null) {
            builder.put(name, value.toString());
        }
        return new WebTarget(channelProvider, path, builder.build());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebTarget webTarget = (WebTarget) o;

        if (channelProvider != null ? !channelProvider.equals(webTarget.channelProvider) : webTarget.channelProvider != null) {
            return false;
        }
        if (path != null ? !path.equals(webTarget.path) : webTarget.path != null) {
            return false;
        }
        return queryParams != null ? queryParams.equals(webTarget.queryParams) : webTarget.queryParams == null;
    }

    @Override
    public int hashCode() {
        int result = channelProvider != null ? channelProvider.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (queryParams != null ? queryParams.hashCode() : 0);
        return result;
    }
}
