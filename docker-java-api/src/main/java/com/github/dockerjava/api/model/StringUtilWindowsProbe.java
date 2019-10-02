package com.github.dockerjava.api.model;

import org.apache.commons.lang.SystemUtils;

public class StringUtilWindowsProbe implements WindowsProbe {
    @Override
    public boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}
