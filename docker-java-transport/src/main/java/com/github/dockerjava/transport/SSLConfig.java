package com.github.dockerjava.transport;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Get an SSL Config. Allows for various different implementations.
 */
public interface SSLConfig {

    /**
     * Get the SSL Context, from wherever it comes (file, keystore).
     *
     * @return an SSL context.
     */
    SSLContext getSSLContext() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException;
}
