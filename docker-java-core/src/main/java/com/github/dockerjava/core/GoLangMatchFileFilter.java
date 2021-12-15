/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.AbstractFileFilter;

import com.github.dockerjava.core.util.FilePathUtil;

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
