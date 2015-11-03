/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import org.apache.commons.io.filefilter.AbstractFileFilter;

import java.io.File;
import java.util.List;

public class GoLangMatchFileFilter extends AbstractFileFilter {

    private final File base;

    private final List<String> patterns;

    public GoLangMatchFileFilter(File base, List<String> patterns) {
        super();
        this.base = base;
        this.patterns = patterns;
    }

    @Override
    public boolean accept(File file) {
        String relativePath = FilePathUtil.relativize(base, file);

        return GoLangFileMatch.match(patterns, relativePath).isEmpty();
    }

}
