package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A repository or image name.
 */
@EqualsAndHashCode
@ToString
public class Repository implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String name;

    /**
     * Name may be eg. 'busybox' or '10.0.0.1:5000/fred'
     *
     * @param name
     *            Repository name
     */
    public Repository(String name) {
        this.name = name;
    }

    /**
     * Return the URL portion (repository). Note that this might not actually BE a repository location.
     *
     * @return
     * @throws java.net.MalformedURLException
     */
    public URL getURL() throws MalformedURLException {
        return new URL("http://" + name);
    }

    public String getPath() {
        if (!name.contains("/")) {
            return name;
        }

        return name.substring(name.indexOf("/") + 1);
    }
}
