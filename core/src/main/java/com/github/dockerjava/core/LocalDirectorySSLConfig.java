package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.SSLConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLContext;
import java.io.Serializable;
import java.security.Security;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * SSL Config from local files.
 */
public class LocalDirectorySSLConfig implements SSLConfig, Serializable {

    private static final String DEFAULT_KEYSTORE_PASSWORD = "docker";

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
                final String httpProtocols = System.getProperty("https.protocols");
                System.setProperty("https.protocols", "TLSv1");

                if (httpProtocols != null) {
                    System.setProperty("https.protocols", httpProtocols);
                }

                // todo: fix
                return SSLContext.getDefault();

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
