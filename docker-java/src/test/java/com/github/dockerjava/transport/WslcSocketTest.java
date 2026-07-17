package com.github.dockerjava.transport;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WslcSocketTest {

    @Test
    public void resolveExecutableDefaultsConsistentlyForEmptyOrRootPath() {
        // wslc:// -> null/empty path, wslc:/// -> "/" path: all resolve identically (to WSLC_EXECUTABLE
        // if set, otherwise "wslc.exe"). Assert consistency unconditionally and the default only when
        // the environment does not override it, so the test does not depend on WSLC_EXECUTABLE.
        String fromNull = WslcSocket.resolveExecutable(null);
        assertEquals(fromNull, WslcSocket.resolveExecutable(""));
        assertEquals(fromNull, WslcSocket.resolveExecutable("/"));
        if (System.getenv("WSLC_EXECUTABLE") == null) {
            assertEquals("wslc.exe", fromNull);
        }
    }

    @Test
    public void resolveExecutableStripsLeadingSlashFromUrlPath() {
        // wslc:///wslc.exe -> path "/wslc.exe"
        assertEquals("wslc.exe", WslcSocket.resolveExecutable("/wslc.exe"));
    }

    @Test
    public void resolveExecutableKeepsAbsoluteWindowsPath() {
        assertEquals("C:\\Program Files\\WSL\\wslc.exe",
            WslcSocket.resolveExecutable("C:\\Program Files\\WSL\\wslc.exe"));
    }

    @Test
    public void connectWithMissingExecutableThrowsIoException() throws Exception {
        WslcSocket socket = new WslcSocket("no-such-wslc-binary-xyz");
        try {
            socket.connect(null, 0);
            fail("expected IOException for a missing bridge executable");
        } catch (IOException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("no-such-wslc-binary-xyz"));
        } finally {
            socket.close();
        }
    }
}
