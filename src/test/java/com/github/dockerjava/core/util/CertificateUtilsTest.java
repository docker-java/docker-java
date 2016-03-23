package com.github.dockerjava.core.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CertificateUtilsTest {
    @Test
    public void allFilesExist() {
        assertTrue(CertificateUtils.verifyCertificatesExist(getPathToTestResources("allFilesExist")));
    }
    
    @Test
    public void caAndCertAndKeyMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("caAndCertAndKeyMissing")));
    }
    
    @Test
    public void caAndCertMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("caAndCertMissing")));
    }
    
    @Test
    public void caAndKeyMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("caAndKeyMissing")));
    }
    
    @Test
    public void caMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("caMissing")));
    }
    
    @Test
    public void certAndKeyMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("certAndKeyMissing")));
    }
    
    @Test
    public void certMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("certMissing")));
    }

    @Test
    public void keyMissing() {
        assertFalse(CertificateUtils.verifyCertificatesExist(getPathToTestResources("keyMissing")));
    }
    
    private String getPathToTestResources(String methodName) {
        Path basePath = Paths.get("src", "test", "resources");
        Path testClassPath = basePath.resolve(CertificateUtilsTest.class.getName().replace('.', File.separatorChar));
        Path testMethodPath = testClassPath.resolve(methodName);
        
        return testMethodPath.toString();
    }
}
