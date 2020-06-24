package com.github.dockerjava.core.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CertificateUtilsTest {
    private static final String baseDir = CertificateUtilsTest.class.getResource(
            CertificateUtilsTest.class.getSimpleName() + "/").getFile();

    @BeforeClass
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @AfterClass
    public static void tearDown() {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
    }

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
        String capem = readFileAsString("caTest/single_ca.pem");
        KeyStore keyStore = CertificateUtils.createTrustStore(capem);
        assertThat(keyStore.size(), is(1));
        assertThat(keyStore.isCertificateEntry("ca-1"), is(true));
    }

    @Test
    public void readMultipleCaCerts() throws Exception {
        String capem = readFileAsString("caTest/multiple_ca.pem");
        KeyStore keyStore = CertificateUtils.createTrustStore(capem);
        assertThat(keyStore.size(), is(2));
        assertThat(keyStore.isCertificateEntry("ca-1"), is(true));
        assertThat(keyStore.isCertificateEntry("ca-2"), is(true));
    }

    @Test
    public void readCert() throws Exception {
        String certpem = readFileAsString("caTest/single_ca.pem");
        List<Certificate> certs = CertificateUtils.loadCertificates(certpem);
        assertThat(certs.size(), is(1));
    }

    @Test
    public void readMultipleCerts() throws Exception {
        String certpem = readFileAsString("caTest/multiple_ca.pem");
        List<Certificate> certs = CertificateUtils.loadCertificates(certpem);
        assertThat(certs.size(), is(2));
    }

    private String readFileAsString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(new File(baseDir + path).getPath())));
    }
}
