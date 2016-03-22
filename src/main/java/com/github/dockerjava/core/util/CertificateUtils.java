package com.github.dockerjava.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

public class CertificateUtils {
    private CertificateUtils() {
        // utility class
    }

    public static boolean verifyCertificatesExist(String dockerCertPath) {
        String[] files = {"ca.pem", "cert.pem", "key.pem"};
        boolean result = true;
        for (String file : files) {
            File path = new File(dockerCertPath, file);
            result &= path.exists();
        }

        return result;
    }

    public static KeyStore createKeyStore(final String dockerCertPath) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException, CertificateException, KeyStoreException {
        KeyPair keyPair = loadPrivateKey(dockerCertPath);
        List<Certificate> privateCertificates = loadCertificates(dockerCertPath);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null);

        keyStore.setKeyEntry("docker", keyPair.getPrivate(), "docker".toCharArray(),
                privateCertificates.toArray(new Certificate[privateCertificates.size()]));
        return keyStore;
    }

    public static KeyStore createTrustStore(final String dockerCertPath) throws IOException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException {
        File caPath = new File(dockerCertPath, "ca.pem");
        BufferedReader reader = new BufferedReader(new FileReader(caPath));
        PEMParser pemParser = null;

        try {
            pemParser = new PEMParser(reader);
            X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();
            Certificate caCertificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(
                    certificateHolder);

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(null);
            trustStore.setCertificateEntry("ca", caCertificate);
            return trustStore;

        } finally {
            if (pemParser != null) {
                IOUtils.closeQuietly(pemParser);
            }

            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }

    }

    private static List<Certificate> loadCertificates(final String dockerCertPath) throws IOException,
            CertificateException {
        File certificate = new File(dockerCertPath, "cert.pem");
        BufferedReader reader = new BufferedReader(new FileReader(certificate));
        PEMParser pemParser = null;

        try {
            List<Certificate> certificates = new ArrayList<>();
            pemParser = new PEMParser(reader);
            JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider("BC");
            Object certObj = pemParser.readObject();

            while (certObj != null) {
                X509CertificateHolder certificateHolder = (X509CertificateHolder) certObj;
                certificates.add(certificateConverter.getCertificate(certificateHolder));

                certObj = pemParser.readObject();
            }

            return certificates;
        } finally {
            if (pemParser != null) {
                IOUtils.closeQuietly(pemParser);
            }

            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }

    }

    private static KeyPair loadPrivateKey(final String dockerCertPath) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        File certificate = new File(dockerCertPath, "key.pem");
        BufferedReader reader = new BufferedReader(new FileReader(certificate));

        PEMParser pemParser = null;

        try {
            pemParser = new PEMParser(reader);

            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();

            byte[] pemPrivateKeyEncoded = pemKeyPair.getPrivateKeyInfo().getEncoded();
            byte[] pemPublicKeyEncoded = pemKeyPair.getPublicKeyInfo().getEncoded();

            KeyFactory factory = KeyFactory.getInstance("RSA");

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pemPublicKeyEncoded);
            PublicKey publicKey = factory.generatePublic(publicKeySpec);

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pemPrivateKeyEncoded);
            PrivateKey privateKey = factory.generatePrivate(privateKeySpec);

            return new KeyPair(publicKey, privateKey);

        } finally {
            if (pemParser != null) {
                IOUtils.closeQuietly(pemParser);
            }

            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }

    }

}
