package com.github.dockerjava.core.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CertificateUtilsTest {
    @Test
    public void allFilesExist() {
        assertTrue(CertificateUtils.verifyCertificatesExist("src/test/resources/testVerifyCertificatesExist/allFiles"));
    }
    
    @Test
    public void caMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist("src/test/resources/testVerifyCertificatesExist/caMissing"));
    }

    @Test
    public void certMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist("src/test/resources/testVerifyCertificatesExist/certMissing"));
    }

    @Test
    public void keyMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist("src/test/resources/testVerifyCertificatesExist/keyMissing"));
    }
}

