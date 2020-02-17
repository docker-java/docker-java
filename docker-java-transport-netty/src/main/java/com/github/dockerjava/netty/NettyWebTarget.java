package com.github.dockerjava.netty;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.WebTarget;
import com.google.common.collect.ImmutableSet;
import io.netty.handler.codec.http.HttpConstants;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * This class is basically a replacement of {@link javax.ws.rs.client.WebTarget} to allow simpler migration of JAX-RS code to a netty based
 * implementation.
 *
 * @author Marcus Linke
 */
public class NettyWebTarget implements WebTarget {

    private final ChannelProvider channelProvider;

    private final String host;

    private final ImmutableList<String> path;

    private final ImmutableMap<String, String> queryParams;

    /**
     * Multiple values for the same name param.
     */
    private final ImmutableMap<String, Set<String>> queryParamsSet;

    private static final String PATH_SEPARATOR = "/";

    private final ObjectMapper objectMapper;

    @Deprecated
    public NettyWebTarget(ChannelProvider channelProvider, String host) {
        this(
                DefaultDockerClientConfig.createDefaultConfigBuilder().build().getObjectMapper(),
                channelProvider,
                host,
                ImmutableList.of(),
                ImmutableMap.of(),
                ImmutableMap.of()
        );
    }

    public NettyWebTarget(ObjectMapper objectMapper, ChannelProvider channelProvider, String host) {
        this(
                objectMapper,
                channelProvider,
                host,
                ImmutableList.of(),
                ImmutableMap.of(),
                ImmutableMap.of()
        );
    }

    private NettyWebTarget(
            ObjectMapper objectMapper,
            ChannelProvider channelProvider,
            String host,
            ImmutableList<String> path,
            ImmutableMap<String, String> queryParams,
            ImmutableMap<String, Set<String>> queryParamsSet
    ) {
        this.objectMapper = objectMapper;
        this.channelProvider = channelProvider;
        this.host = host;
        this.path = path;
        this.queryParams = queryParams;
        this.queryParamsSet = queryParamsSet;
    }

    public NettyWebTarget path(String... components) {
        ImmutableList.Builder<String> newPath = ImmutableList.<String>builder().addAll(this.path);

        for (String component : components) {
            newPath.addAll(Arrays.asList(StringUtils.split(component, PATH_SEPARATOR)));
        }

        return new NettyWebTarget(objectMapper, channelProvider, host, newPath.build(), queryParams, queryParamsSet);
    }

    public NettyInvocationBuilder request() {
        String resource = PATH_SEPARATOR + StringUtils.join(path, PATH_SEPARATOR);

        List<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.add(entry.getKey() + "=" + encodeComponent(entry.getValue(), HttpConstants.DEFAULT_CHARSET));
        }

        for (Map.Entry<String, Set<String>> entry : queryParamsSet.entrySet()) {
            for (String entryValueValue : entry.getValue()) {
                params.add(entry.getKey() + "=" + encodeComponent(entryValueValue, HttpConstants.DEFAULT_CHARSET));
            }
        }

        if (!params.isEmpty()) {
            resource = resource + "?" + StringUtils.join(params, "&");
        }

        return new NettyInvocationBuilder(objectMapper, channelProvider, resource)
            .header("Host", host);
    }

    /**
     * @see io.netty.handler.codec.http.QueryStringEncoder
     */
    private static String encodeComponent(String s, Charset charset) {
        // TODO: Optimize me.
        try {
            return URLEncoder.encode(s, charset.name()).replace("+", "%20");
        } catch (UnsupportedEncodingException ignored) {
            throw new UnsupportedCharsetException(charset.name());
        }
    }

    public NettyWebTarget resolveTemplate(String name, Object value) {
        ImmutableList.Builder<String> newPath = ImmutableList.builder();
        for (String component : path) {
            component = component.replaceAll("\\{" + name + "\\}", value.toString());
            newPath.add(component);
        }
        return new NettyWebTarget(objectMapper, channelProvider, host, newPath.build(), queryParams, queryParamsSet);
    }

    public NettyWebTarget queryParam(String name, Object value) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder().putAll(queryParams);
        if (value != null) {
            builder.put(name, value.toString());
        }
        return new NettyWebTarget(objectMapper, channelProvider, host, path, builder.build(), queryParamsSet);
    }

    public NettyWebTarget queryParamsSet(String name, Set<?> values) {
        ImmutableMap.Builder<String, Set<String>> builder = ImmutableMap.<String, Set<String>>builder().putAll(queryParamsSet);
        if (values != null) {
            ImmutableSet.Builder<String> valueBuilder = ImmutableSet.builder();
            for (Object value : values) {
                valueBuilder.add(value.toString());
            }
            builder.put(name, valueBuilder.build());
        }
        return new NettyWebTarget(objectMapper, channelProvider, host, path, queryParams, builder.build());
    }

    public NettyWebTarget queryParamsJsonMap(String name, Map<String, String> values) {
        if (values != null && !values.isEmpty()) {
            try {
                // when param value is JSON string
                return queryParam(name, objectMapper.writeValueAsString(values));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NettyWebTarget webTarget = (NettyWebTarget) o;

        if (!Objects.equals(channelProvider, webTarget.channelProvider)) {
            return false;
        }
        if (!Objects.equals(path, webTarget.path)) {
            return false;
        }
        if (!Objects.equals(queryParams, webTarget.queryParams)) {
            return false;
        }
        if (!Objects.equals(queryParamsSet, webTarget.queryParamsSet)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = channelProvider != null ? channelProvider.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (queryParams != null ? queryParams.hashCode() : 0);
        result = 31 * result + (queryParamsSet != null ? queryParamsSet.hashCode() : 0);
        return result;
    }
}
