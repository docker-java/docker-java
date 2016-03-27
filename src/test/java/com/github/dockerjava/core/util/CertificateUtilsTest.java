package com.github.dockerjava.core.util;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CertificateUtilsTest {
    private static final String baseDir = CertificateUtilsTest.class.getResource(
            CertificateUtilsTest.class.getSimpleName() + "/").getFile();

    @Test
    public void allFilesExist() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "allFilesExist"), is(true));
    }

    @Test
    public void caAndCertAndKeyMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "caAndCertAndKeyMissing"), is(false));
    }

    @Test
    public void caAndCertMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "caAndCertMissing"), is(false));
    }

    @Test
    public void caAndKeyMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "caAndKeyMissing"), is(false));
    }

    @Test
    public void caMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "caMissing"), is(false));
    }

    @Test
    public void certAndKeyMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "certAndKeyMissing"), is(false));
    }

    @Test
    public void certMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "certMissing"), is(false));
    }

    @Test
    public void keyMissing() {
        assertThat(CertificateUtils.verifyCertificatesExist(baseDir + "keyMissing"), is(false));
    }
}
