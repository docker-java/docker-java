package com.github.dockerjava.core;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.security.Security;

import javax.net.ssl.SSLContext;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.jersey.SslConfigurator;

import com.github.dockerjava.api.DockerClientException;


/**
 * SSL Config from local files.
 */
public class LocalDirectorySSLConfig implements SSLConfig, Serializable {

  private final String dockerCertPath;

  public LocalDirectorySSLConfig(String dockerCertPath) {
    checkNotNull(dockerCertPath);
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

        // properties acrobatics not needed for java > 1.6
        String httpProtocols = System.getProperty("https.protocols");
        System.setProperty("https.protocols", "TLSv1");
        SslConfigurator sslConfig = SslConfigurator.newInstance(true);
        if (httpProtocols != null) {
          System.setProperty("https.protocols", httpProtocols);
        }

        sslConfig.keyStore(CertificateUtils.createKeyStore(dockerCertPath));
        sslConfig.keyStorePassword("docker");
        sslConfig.trustStore(CertificateUtils.createTrustStore(dockerCertPath));

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
    return new StringBuilder()
        .append(this.getClass().getSimpleName()).append("{")
            .append("dockerCertPath=").append(dockerCertPath)
        .append("}")
        .toString();
  }
}
