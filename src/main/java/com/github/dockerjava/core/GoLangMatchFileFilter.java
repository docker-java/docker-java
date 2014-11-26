/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import static org.apache.commons.lang.StringUtils.stripStart;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.AbstractFileFilter;

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
        String basePath = base.getAbsolutePath() + File.separatorChar;
        String relativePath = stripStart(file.getAbsolutePath(), basePath);
        
        boolean match = GoLangFileMatch.match(patterns, relativePath);
        return !match;
    }


}
