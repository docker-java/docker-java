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

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class WebTarget {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ChannelProvider channelProvider;

    private final ImmutableList<String> path;

    private final ImmutableMap<String, String> queryParams;
    /**
     * Multiple values for the same name param.
     */
    private final ImmutableMap<String, List<String>> queryParamsList;

    private static final String PATH_SEPARATOR = "/";

    public WebTarget(ChannelProvider channelProvider) {
        this(channelProvider, ImmutableList.<String>of(), ImmutableMap.<String, String>of(),
                ImmutableMap.<String, List<String>>of());
    }

    private WebTarget(ChannelProvider channelProvider,
                      ImmutableList<String> path,
                      ImmutableMap<String, String> queryParams,
                      ImmutableMap<String, List<String>> queryParamsList) {
        this.channelProvider = channelProvider;
        this.path = path;
        this.queryParams = queryParams;
        this.queryParamsList = queryParamsList;
    }

    public WebTarget path(String... components) {
        ImmutableList.Builder<String> newPath = ImmutableList.<String>builder().addAll(this.path);

        for (String component : components) {
            newPath.addAll(Arrays.asList(StringUtils.split(component, PATH_SEPARATOR)));
        }

        return new WebTarget(channelProvider, newPath.build(), queryParams, queryParamsList);
    }

    public InvocationBuilder request() {
        String resource = PATH_SEPARATOR + StringUtils.join(path, PATH_SEPARATOR);

        List<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            params.add(entry.getKey() + "=" + encodeComponent(entry.getValue(), HttpConstants.DEFAULT_CHARSET));
        }

        for (Map.Entry<String, List<String>> entry : queryParamsList.entrySet()) {
            for (String entryValueValue : entry.getValue()) {
                params.add(entry.getKey() + "=" + encodeComponent(entryValueValue, HttpConstants.DEFAULT_CHARSET));
            }
        }

        if (!params.isEmpty()) {
            resource = resource + "?" + StringUtils.join(params, "&");
        }

        return new InvocationBuilder(channelProvider, resource);
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

    public WebTarget resolveTemplate(String name, Object value) {
        ImmutableList.Builder<String> newPath = ImmutableList.builder();
        for (String component : path) {
            component = component.replaceAll("\\{" + name + "\\}", value.toString());
            newPath.add(component);
        }
        return new WebTarget(channelProvider, newPath.build(), queryParams, queryParamsList);
    }

    public WebTarget queryParam(String name, Object value) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.<String, String>builder().putAll(queryParams);
        if (value != null) {
            builder.put(name, value.toString());
        }
        return new WebTarget(channelProvider, path, builder.build(), queryParamsList);
    }

    public WebTarget queryParamsList(String name, List<?> values) {
        ImmutableMap.Builder<String, List<String>> builder = ImmutableMap.<String, List<String>>builder().putAll(queryParamsList);
        if (values != null) {
            ImmutableList.Builder<String> valueBuilder = ImmutableList.builder();
            for (Object value : values) {
                valueBuilder.add(value.toString());
            }
            builder.put(name, valueBuilder.build());
        }
        return new WebTarget(channelProvider, path, queryParams, builder.build());
    }

    public WebTarget queryParamsJsonMap(String name, Map<String, String> values) {
        if (values != null && !values.isEmpty()) {
            try {
                // when param value is JSON string
                return queryParam(name, MAPPER.writeValueAsString(values));
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

        WebTarget webTarget = (WebTarget) o;

        if (channelProvider != null ? !channelProvider.equals(webTarget.channelProvider) : webTarget.channelProvider != null) {
            return false;
        }
        if (path != null ? !path.equals(webTarget.path) : webTarget.path != null) {
            return false;
        }
        if (queryParams != null ? !queryParams.equals(webTarget.queryParams) : webTarget.queryParams != null) {
            return false;
        }
        if (queryParamsList != null ? !queryParamsList.equals(webTarget.queryParamsList) : webTarget.queryParamsList != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = channelProvider != null ? channelProvider.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (queryParams != null ? queryParams.hashCode() : 0);
        result = 31 * result + (queryParamsList != null ? queryParamsList.hashCode() : 0);
        return result;
    }
}
