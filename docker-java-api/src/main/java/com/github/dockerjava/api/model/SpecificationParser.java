package com.github.dockerjava.api.model;

class SpecificationParser {

    private final String[] parts;
    private final WindowsProbe windowsProbe;

    private SpecificationParser(String[] parts, WindowsProbe windowsProbe) {
        this.windowsProbe = windowsProbe;
        if (hasInvalidLength(parts)) {
            throw new IllegalArgumentException();
        }
        this.parts = parts;
    }

    static SpecificationParser parse(String serialized, WindowsProbe windowsProbe) {
        try {
            String[] split = serialized.split(":");
            return new SpecificationParser(split, windowsProbe);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing Bind '" + serialized + "'", e);
        }
    }

    private boolean hasInvalidLength(String[] split) {
        if (isWindows()) {
            return split.length < 3 || split.length > 5;
        } else {
            return split.length < 2 || split.length > 3;
        }
    }

    boolean hasExtraParams() {
        if (isWindows()) {
            return parts.length == 5;
        } else {
            return parts.length == 3;
        }
    }

    String getPath() {
        if (isWindows()) {
            return parts[0] + ":" + parts[1];
        } else {
            return parts[0];
        }
    }

    String getVolumePath() {
        if (isWindows()) {
            return parts[2] + ":" + parts[3];
        } else {
            return parts[1];
        }
    }

    String getFlags() {
        if (isWindows()) {
            return parts[4];
        } else {
            return parts[2];
        }
    }

    private boolean isWindows() {
        return windowsProbe.isWindows();
    }
}
