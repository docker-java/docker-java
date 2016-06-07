package com.github.dockerjava.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;

import javax.net.ssl.SSLContext;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.jersey.SslConfigurator;

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

                // properties acrobatics not needed for java > 1.6
                String httpProtocols = System.getProperty("https.protocols");
                System.setProperty("https.protocols", "TLSv1");
                SslConfigurator sslConfig = SslConfigurator.newInstance(true);
                if (httpProtocols != null) {
                    System.setProperty("https.protocols", httpProtocols);
                }

                String caPemPath = dockerCertPath + File.separator + "ca.pem";
                String keyPemPath = dockerCertPath + File.separator + "key.pem";
                String certPemPath = dockerCertPath + File.separator + "cert.pem";

                String keypem = new String(Files.readAllBytes(Paths.get(keyPemPath)));
                String certpem = new String(Files.readAllBytes(Paths.get(certPemPath)));
                String capem = new String(Files.readAllBytes(Paths.get(caPemPath)));

                sslConfig.keyStore(CertificateUtils.createKeyStore(keypem, certpem));
                sslConfig.keyStorePassword("docker");
                sslConfig.trustStore(CertificateUtils.createTrustStore(capem));

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
