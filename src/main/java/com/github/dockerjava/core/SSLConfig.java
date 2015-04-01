package com.github.dockerjava.core;

import java.security.*;

import javax.net.ssl.SSLContext;

/**
 * Get an SSL Config. Allows for various different implementations.
 */
public interface SSLConfig {

  /**
   * Get the SSL Context, from wherever it comes (file, keystore).
   * @return an SSL context.
   */
  SSLContext getSSLContext()
      throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException,
             KeyStoreException;
}
