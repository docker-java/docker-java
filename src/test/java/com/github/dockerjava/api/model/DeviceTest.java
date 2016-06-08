package com.github.dockerjava.api.model;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Kanstantsin Shautsou
 */
public class DeviceTest {

    public static List<String> validPaths = Arrays.asList(
            "/home",
            "/home:/home",
            "/home:/something/else",
            "/with space",
            "/home:/with space",
            "relative:/absolute-path",
            "hostPath:/containerPath:r",
            "/hostPath:/containerPath:rw",
            "/hostPath:/containerPath:mrw"
    );

    public static HashMap<String, String> badPaths = new LinkedHashMap<String, String>() {{
        put("", "bad format for path: ");
        // TODO implement ValidatePath
//        put("./", "./ is not an absolute path");
//        put("../", "../ is not an absolute path");
//        put("/:../", "../ is not an absolute path");
//        put("/:path", "path is not an absolute path");
//        put(":", "bad format for path: :");
//        put("/tmp:", " is not an absolute path");
//        put(":test", "bad format for path: :test");
//        put(":/test", "bad format for path: :/test");
//        put("tmp:", " is not an absolute path");
//        put(":test:", "bad format for path: :test:");
//        put("::", "bad format for path: ::");
//        put(":::", "bad format for path: :::");
//        put("/tmp:::", "bad format for path: /tmp:::");
//        put(":/tmp::", "bad format for path: :/tmp::");
//        put("path:ro", "ro is not an absolute path");
//        put("path:rr", "rr is not an absolute path");
        put("a:/b:ro", "bad mode specified: ro");
        put("a:/b:rr", "bad mode specified: rr");
    }};

    @Test
    public void testParse() throws Exception {
        assertThat(Device.parse("/dev/sda:/dev/xvdc:r"),
                equalTo(new Device("r", "/dev/xvdc", "/dev/sda")));

        assertThat(Device.parse("/dev/snd:rw"),
                equalTo(new Device("rw", "/dev/snd", "/dev/snd")));

        assertThat(Device.parse("/dev/snd:/something"),
                equalTo(new Device("rwm", "/something", "/dev/snd")));

        assertThat(Device.parse("/dev/snd:/something:rw"),
                equalTo(new Device("rw", "/something", "/dev/snd")));

    }

    @Test
    public void testParseBadPaths() {
        for (Map.Entry<String, String> entry : badPaths.entrySet()) {
            final String deviceStr = entry.getKey();
            try {
                Device.parse(deviceStr);
                fail("Should fail because: " + entry.getValue() + " '" + deviceStr + "'");
            } catch (IllegalArgumentException ex) {
                assertThat(ex.getMessage(), containsString("Invalid device specification:"));
            }
        }
    }

    @Test
    public void testParseValidPaths() {
        for (String path : validPaths) {
            Device.parse(path);
        }
    }
}
