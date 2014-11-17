package com.github.dockerjava.core;

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

import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

public class CertificateUtils {
    
    public static boolean verifyCertificatesExist(String dockerCertPath) {
        String[] files = {"ca.pem", "cert.pem", "key.pem"};
        for (String file : files) {
            File path = new File(dockerCertPath, file);
            boolean exists = path.exists();
            if(!exists) {
                return false;
            }
        }
        
        return true;
    }
    
    public static KeyStore createKeyStore(final String dockerCertPath) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, CertificateException, KeyStoreException {
        KeyPair keyPair = loadPrivateKey(dockerCertPath);
        Certificate privateCertificate = loadCertificate(dockerCertPath);

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null);

        keyStore.setKeyEntry("docker", keyPair.getPrivate(), "docker".toCharArray(), new Certificate[]{privateCertificate});
        return keyStore;
    }
    
    public static KeyStore createTrustStore(final String dockerCertPath) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        File caPath = new File(dockerCertPath, "ca.pem");
        BufferedReader reader = new BufferedReader(new FileReader(caPath));
        PEMParser pemParser = null;
        
        try {
            pemParser = new PEMParser(reader);
            X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();
            Certificate caCertificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certificateHolder);
            
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(null);
            trustStore.setCertificateEntry("ca", caCertificate);
            return trustStore;
            
        }
        finally {
            if(pemParser != null) {
                IOUtils.closeQuietly(pemParser);
            }

            if(reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }
        
    }
    
    private static Certificate loadCertificate(final String dockerCertPath) throws IOException, CertificateException {
        File certificate = new File(dockerCertPath, "cert.pem");
        BufferedReader reader = new BufferedReader(new FileReader(certificate));
        PEMParser pemParser = null;
        
        try {
           pemParser = new PEMParser(reader);
           X509CertificateHolder certificateHolder = (X509CertificateHolder) pemParser.readObject();
           return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certificateHolder);
        }
        finally {
            if(pemParser != null) {
                IOUtils.closeQuietly(pemParser);
            }
            
            if(reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }
        
    }
    
    private static KeyPair loadPrivateKey(final String dockerCertPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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
        
        }
        finally {
            if(pemParser != null) {
                IOUtils.closeQuietly(pemParser);
            }
            
            if(reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }

        
    }

}
