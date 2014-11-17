/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.AbstractFileFilter;

public class GoLangMatchFileFilter extends AbstractFileFilter {

    private final List<String> patterns;


    public GoLangMatchFileFilter(List<String> patterns) {
        super();
        this.patterns = patterns;
    }

    @Override
    public boolean accept(File file) {
        return !GoLangFileMatch.match(patterns, file);
    }


}
