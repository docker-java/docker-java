/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

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
    	String relativePath = file.getAbsolutePath().replaceFirst(base.getAbsolutePath() + File.separatorChar, "");
    	
    	boolean match = GoLangFileMatch.match(patterns, relativePath);
        return !match;
    }


}
