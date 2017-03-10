package com.github.dockerjava.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static KeyStore createKeyStore(final Path keyPemPath, final Path certPemPath) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException, CertificateException, KeyStoreException {
        KeyStore keyStore = newKeyStore();
        PrivateKey privateKey = readPrivateKey(keyPemPath);
        List<Certificate> clientCerts = readCertificates(certPemPath);

        keyStore.setKeyEntry("docker", privateKey, "docker".toCharArray(),
                clientCerts.toArray(new Certificate[clientCerts.size()]));

        return keyStore;
    }

    public static KeyStore createTrustStore(final Path capemPath) throws IOException, CertificateException,
            KeyStoreException, NoSuchAlgorithmException {
        KeyStore trustStore = newKeyStore();
        List<Certificate> certs = readCertificates(capemPath);

        int index = 1;
        for (Certificate cert : certs) {
            String alias = "ca-" + index;
            trustStore.setCertificateEntry(alias, cert);
            index++;
        }

        return trustStore;
    }

    private static KeyStore newKeyStore() throws CertificateException, NoSuchAlgorithmException, IOException,
            KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        return keyStore;
    }

    public static List<Certificate> readCertificates(final Path file) throws IOException,
            CertificateException {
        try (InputStream is = Files.newInputStream(file)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
            return new ArrayList<>(certificateFactory.generateCertificates(is));
        }
    }

    public static PrivateKey readPrivateKey(final Path file) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        try (Reader reader = Files.newBufferedReader(file, Charset.defaultCharset());
             PEMParser pemParser = new PEMParser(reader)) {
            Object readObject = pemParser.readObject();
            while (readObject != null) {
                if (readObject instanceof PEMKeyPair) {
                    PEMKeyPair pemKeyPair = (PEMKeyPair) readObject;
                    return generatePrivateKey(pemKeyPair.getPrivateKeyInfo());
                } else if (readObject instanceof PrivateKeyInfo) {
                    return generatePrivateKey((PrivateKeyInfo) readObject);
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
        throw new InvalidKeySpecException("Can not generate private key from file: " + file.toString());
    }

    private static PrivateKey generatePrivateKey(PrivateKeyInfo privateKeyInfo) throws InvalidKeySpecException,
            IOException {
        //no way to know, so iterate
        for (String guessFactory : new String[]{"RSA", "ECDSA"}) {
            try {
                KeyFactory factory = KeyFactory.getInstance(guessFactory);
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
                return factory.generatePrivate(privateKeySpec);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                // skip
            }
        }
        throw new InvalidKeySpecException("Can not generate private key with algorithm: "
                + privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm().getId());
    }

}
