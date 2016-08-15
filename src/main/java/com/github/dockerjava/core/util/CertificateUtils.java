package com.github.dockerjava.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;

import static java.util.Objects.requireNonNull;

public class CertificateUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CertificateUtils.class);

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

    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public static KeyStore createKeyStore(final String keypem, final String certpem) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException, CertificateException, KeyStoreException {
        PrivateKey privateKey = loadPrivateKey(keypem);
        requireNonNull(privateKey);
        List<Certificate> privateCertificates = loadCertificates(certpem);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null);

        keyStore.setKeyEntry("docker",
                privateKey,
                "docker".toCharArray(),
                privateCertificates.toArray(new Certificate[privateCertificates.size()])
        );

        return keyStore;
    }

    /**
     * from "cert.pem" String
     */
    public static List<Certificate> loadCertificates(final String certpem) throws IOException,
            CertificateException {
        final StringReader certReader = new StringReader(certpem);
        try (BufferedReader reader = new BufferedReader(certReader)) {
            return loadCertificates(reader);
        }
    }

    /**
     * "cert.pem" from reader
     */
    public static List<Certificate> loadCertificates(final Reader reader) throws IOException,
            CertificateException {
        try (PEMParser pemParser = new PEMParser(reader)) {
            List<Certificate> certificates = new ArrayList<>();

            JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider("BC");
            Object certObj = pemParser.readObject();

            if (certObj instanceof X509CertificateHolder) {
                X509CertificateHolder certificateHolder = (X509CertificateHolder) certObj;
                certificates.add(certificateConverter.getCertificate(certificateHolder));
            }

            return certificates;
        }
    }


    /**
     * Return private key ("key.pem") from Reader
     */
    @CheckForNull
    public static PrivateKey loadPrivateKey(final Reader reader) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        try (PEMParser pemParser = new PEMParser(reader)) {
            Object readObject = pemParser.readObject();
            while (readObject != null) {
                if (readObject instanceof PEMKeyPair) {
                    PEMKeyPair pemKeyPair = (PEMKeyPair) readObject;
                    PrivateKey privateKey = guessKey(pemKeyPair.getPrivateKeyInfo().getEncoded());
                    if (privateKey != null) {
                        return privateKey;
                    }
                } else if (readObject instanceof PrivateKeyInfo) {
                    PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) readObject;
                    PrivateKey privateKey = guessKey(privateKeyInfo.getEncoded());
                    if (privateKey != null) {
                        return privateKey;
                    }
                } else if (readObject instanceof ASN1ObjectIdentifier) {
                    // no idea how it can be used
                    final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier) readObject;
                    LOG.trace("Ignoring asn1ObjectIdentifier {}", asn1ObjectIdentifier);
                } else {
                    LOG.warn("Unknown object '{}' from PEMParser", readObject);
                }

                readObject = pemParser.readObject();
            }
        }

        return null;
    }

    @CheckForNull
    public static PrivateKey guessKey(byte[] encodedKey) throws NoSuchAlgorithmException {
        //no way to know, so iterate
        for (String guessFactory : new String[]{"RSA", "ECDSA"}) {
            try {
                KeyFactory factory = KeyFactory.getInstance(guessFactory);

                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
                return factory.generatePrivate(privateKeySpec);
            } catch (InvalidKeySpecException ignore) {
            }
        }

        return null;
    }

    /**
     * Return KeyPair from "key.pem"
     */
    @CheckForNull
    public static PrivateKey loadPrivateKey(final String keypem) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        try (StringReader certReader = new StringReader(keypem);
             BufferedReader reader = new BufferedReader(certReader)) {
            return loadPrivateKey(reader);
        }
    }

    /**
     * "ca.pem" from String
     */
    public static KeyStore createTrustStore(String capem) throws IOException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException {
        try (Reader certReader = new StringReader(capem)) {
            return createTrustStore(certReader);
        }
    }

    /**
     * "ca.pem" from Reader
     */
    public static KeyStore createTrustStore(final Reader certReader) throws IOException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException {
        try (PEMParser pemParser = new PEMParser(certReader)) {
            X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();
            Certificate caCertificate = new JcaX509CertificateConverter()
                    .setProvider("BC")
                    .getCertificate(certificateHolder);

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(null);
            trustStore.setCertificateEntry("ca", caCertificate);

            return trustStore;
        }
    }

}
