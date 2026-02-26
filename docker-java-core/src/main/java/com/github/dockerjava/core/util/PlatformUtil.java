package com.github.dockerjava.core.util;

import java.util.HashMap;
import java.util.Map;

public class PlatformUtil {

    private PlatformUtil() {
    }

    public static Map<String, String> platformMap(String platform) {

        HashMap<String, String> platformMap = new HashMap<>();
        if (platform == null) {
            return platformMap;
        }
        String[] r = platform.split("/");
        if (r.length > 0) {
            platformMap.put("os", r[0]);
        }
        if (r.length > 1) {
            platformMap.put("architecture", r[1]);
        }
        if (r.length > 2) {
            platformMap.put("variant", r[2]);
        }

        return platformMap;

    }

}
