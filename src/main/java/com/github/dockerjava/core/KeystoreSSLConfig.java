package com.github.dockerjava.core;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * An SSL Config that is based on an pre-existing or pre-loaded KeyStore.
 */
public class KeystoreSSLConfig implements SSLConfig, Serializable {

  private final KeyStore keystore;
  private final String keystorePassword;

  /**
   * @param keystore a KeyStore
   * @param keystorePassword key password
   */
  public KeystoreSSLConfig(KeyStore keystore, String keystorePassword) {
    this.keystorePassword = keystorePassword;
    checkNotNull(keystore);
    this.keystore = keystore;
  }

  /**
   *
   * @param pfxFile a PKCS12 file
   * @param password Password for the keystore
   * @throws KeyStoreException
   * @throws IOException
   * @throws CertificateException
   * @throws NoSuchAlgorithmException
   */
  public KeystoreSSLConfig(File pfxFile, String password)
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
    checkNotNull(pfxFile);
    checkNotNull(password);
    keystore = KeyStore.getInstance("pkcs12");
    keystore.load(new FileInputStream(pfxFile), password.toCharArray());
    keystorePassword = password;
  }


  /**
   * Get the SSL Context out of the keystore.
   * @return java SSLContext
   * @throws KeyManagementException
   * @throws UnrecoverableKeyException
   * @throws NoSuchAlgorithmException
   * @throws KeyStoreException
   */
  @Override
  public SSLContext getSSLContext()
      throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException,
             KeyStoreException {

    final SSLContext context = SSLContext.getInstance("TLS");

    String httpProtocols = System.getProperty("https.protocols");
    System.setProperty("https.protocols", "TLSv1");

    if (httpProtocols != null)
      System.setProperty("https.protocols", httpProtocols);

    final KeyManagerFactory
        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keystore, keystorePassword.toCharArray());
    context.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{
        new X509TrustManager() {
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
          }

          @Override
          public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) {

          }

          @Override
          public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) {

          }
        }
    }, new SecureRandom());

    return context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    KeystoreSSLConfig that = (KeystoreSSLConfig) o;

    return keystore.equals(that.keystore);

  }

  @Override
  public int hashCode() {
    return keystore.hashCode();
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append(this.getClass().getSimpleName()).append("{")
            .append("keystore=").append(keystore)
        .append("}")
        .toString();
  }
}
