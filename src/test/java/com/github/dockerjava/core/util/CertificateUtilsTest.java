package com.github.dockerjava.core.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public void readCaCert() throws Exception {
        Path capemPath = getFile("caTest/single_ca.pem");
        KeyStore keyStore = CertificateUtils.createTrustStore(capemPath);
        assertThat(keyStore.size(), is(1));
        assertThat(keyStore.isCertificateEntry("ca-1"), is(true));
    }

    @Test
    public void readMultipleCaCerts() throws Exception {
        Path capemPath = getFile("caTest/multiple_ca.pem");
        KeyStore keyStore = CertificateUtils.createTrustStore(capemPath);
        assertThat(keyStore.size(), is(2));
        assertThat(keyStore.isCertificateEntry("ca-1"), is(true));
        assertThat(keyStore.isCertificateEntry("ca-2"), is(true));
    }

    @Test(expectedExceptions = InvalidKeySpecException.class)
    public void readEmptyPrivateKey() throws Exception {
        Path keyPath = getFile("keys/empty.pem");
        CertificateUtils.readPrivateKey(keyPath);
    }

    @Test
    public void readPrivateKeyPKCS1() throws Exception {
        Path keyPath = getFile("keys/pkcs1.pem");
        PrivateKey privateKey = CertificateUtils.readPrivateKey(keyPath);
        assertNotNull(privateKey);
    }

    @Test
    public void readPrivateKeyPKCS8() throws Exception {
        Path keyPath = getFile("keys/pkcs8.pem");
        PrivateKey privateKey = CertificateUtils.readPrivateKey(keyPath);
        assertNotNull(privateKey);
    }

    private Path getFile(String path) throws IOException {
        return Paths.get(baseDir, path);
    }
}
