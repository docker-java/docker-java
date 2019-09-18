package com.github.dockerjava.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.util.CertificateUtils;

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

                Security.addProvider(new BouncyCastleProvider());

                String caPemPath = dockerCertPath + File.separator + "ca.pem";
                String keyPemPath = dockerCertPath + File.separator + "key.pem";
                String certPemPath = dockerCertPath + File.separator + "cert.pem";

                String keypem = new String(Files.readAllBytes(Paths.get(keyPemPath)));
                String certpem = new String(Files.readAllBytes(Paths.get(certPemPath)));
                String capem = new String(Files.readAllBytes(Paths.get(caPemPath)));

                String kmfAlgorithm = AccessController.doPrivileged(getSystemProperty("ssl.keyManagerFactory.algorithm",
                    KeyManagerFactory.getDefaultAlgorithm()));
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(kmfAlgorithm);
                keyManagerFactory.init(CertificateUtils.createKeyStore(keypem, certpem), "docker".toCharArray());

                String tmfAlgorithm = AccessController.doPrivileged(getSystemProperty("ssl.trustManagerFactory.algorithm",
                    TrustManagerFactory.getDefaultAlgorithm()));
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
                trustManagerFactory.init(CertificateUtils.createTrustStore(capem));

                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

                return sslContext;

            } catch (Exception e) {
                throw new DockerClientException(e.getMessage(), e);
            }

        }

        return null;

    }

    private PrivilegedAction<String> getSystemProperty(final String name, final String def) {
        return new PrivilegedAction<String>() {
            @Override
            public String run() {
                return System.getProperty(name, def);
            }
        };
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
