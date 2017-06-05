package com.github.dockerjava.core;

import javax.net.ssl.SSLContext;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.util.CertificateUtils;
import org.glassfish.jersey.SslConfigurator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * SSL Config from local files.
 */
public class LocalDirectorySSLConfig implements SSLConfig, Serializable {

    private static final long serialVersionUID = -4736328026418377358L;

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

                Path basePath = Paths.get(dockerCertPath);

                Path caPemPath = basePath.resolve("ca.pem");
                Path keyPemPath = basePath.resolve("key.pem");
                Path certPemPath = basePath.resolve("cert.pem");

                SslConfigurator sslConfig = SslConfigurator.newInstance(true);
                sslConfig.securityProtocol("TLSv1.2");
                sslConfig.keyStore(CertificateUtils.createKeyStore(keyPemPath, certPemPath));
                sslConfig.keyStorePassword("docker");
                sslConfig.trustStore(CertificateUtils.createTrustStore(caPemPath));

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
        return new StringBuilder().append(this.getClass().getSimpleName()).append("{").append("dockerCertPath=")
                .append(dockerCertPath).append("}").toString();
    }
}
