package com.github.dockerjava.core;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import com.github.dockerjava.api.DockerClientException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.jersey.SslConfigurator;

import java.io.Serializable;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.SSLContext;

/**
 * SSL Config from local files.
 */
public class LocalDirectorySSLConfig implements SSLConfig, Serializable {

  private final String dockerCertPath;

  public LocalDirectorySSLConfig(String dockerCertPath) {
    Preconditions.checkNotNull(dockerCertPath);
    this.dockerCertPath = dockerCertPath;
  }

  public String getDockerCertPath() {
    return dockerCertPath;
  }

  @Override
  public SSLContext getSSLContext() {

    boolean certificatesExist = CertificateUtils.verifyCertificatesExist(dockerCertPath);

    if (certificatesExist) {

      try {

        Security.addProvider(new BouncyCastleProvider());

        KeyStore keyStore = CertificateUtils.createKeyStore(dockerCertPath);
        KeyStore trustStore = CertificateUtils.createTrustStore(dockerCertPath);

        // properties acrobatics not needed for java > 1.6
        String httpProtocols = System.getProperty("https.protocols");
        System.setProperty("https.protocols", "TLSv1");
        SslConfigurator sslConfig = SslConfigurator.newInstance(true);
        if (httpProtocols != null) {
          System.setProperty("https.protocols", httpProtocols);
        }

        sslConfig.keyStore(keyStore);
        sslConfig.keyStorePassword("docker");
        sslConfig.trustStore(trustStore);

        return sslConfig.createSSLContext();


      } catch (Exception e) {
        throw new DockerClientException(e.getMessage(), e);
      }

    }

    return null;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocalDirectorySSLConfig that = (LocalDirectorySSLConfig) o;

    if (!dockerCertPath.equals(that.dockerCertPath)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return dockerCertPath.hashCode();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("dockerCertPath", dockerCertPath)
        .toString();
  }
}
