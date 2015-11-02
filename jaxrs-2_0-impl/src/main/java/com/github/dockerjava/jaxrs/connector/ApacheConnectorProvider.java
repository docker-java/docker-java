package com.github.dockerjava.jaxrs.connector;

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

import org.apache.http.client.HttpClient;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.LocalizationMessages;
import org.glassfish.jersey.client.Initializable;
import org.glassfish.jersey.client.spi.Connector;
import org.glassfish.jersey.client.spi.ConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Configuration;

/**
 * Connector provider for Jersey {@link Connector connectors} that utilize Apache HTTP Client to send and receive HTTP
 * request and responses.
 * <p>
 * The following connector configuration properties are supported:
 * <ul>
 * <li>{@link ApacheClientProperties#CONNECTION_MANAGER}</li>
 * <li>{@link ApacheClientProperties#REQUEST_CONFIG}</li>
 * <li>{@link ApacheClientProperties#CREDENTIALS_PROVIDER}</li>
 * <li>{@link ApacheClientProperties#DISABLE_COOKIES}</li>
 * <li>{@link org.glassfish.jersey.client.ClientProperties#PROXY_URI}</li>
 * <li>{@link org.glassfish.jersey.client.ClientProperties#PROXY_USERNAME}</li>
 * <li>{@link org.glassfish.jersey.client.ClientProperties#PROXY_PASSWORD}</li>
 * <li>{@link org.glassfish.jersey.client.ClientProperties#REQUEST_ENTITY_PROCESSING} - default value is
 * {@link org.glassfish.jersey.client.RequestEntityProcessing#CHUNKED}</li>
 * <li>{@link ApacheClientProperties#PREEMPTIVE_BASIC_AUTHENTICATION}</li>
 * <li>{@link ApacheClientProperties#SSL_CONFIG}</li>
 * </ul>
 * </p>
 * <p>
 * Connector instances created via this connector provider use
 * {@link org.glassfish.jersey.client.RequestEntityProcessing#CHUNKED chunked encoding} as a default setting. This can
 * be overridden by the {@link org.glassfish.jersey.client.ClientProperties#REQUEST_ENTITY_PROCESSING}. By default the
 * {@link org.glassfish.jersey.client.ClientProperties#CHUNKED_ENCODING_SIZE} property is only supported when using the
 * default {@code org.apache.http.conn.HttpClientConnectionManager} instance. If custom connection manager is used, then
 * chunked encoding size can be set by providing a custom {@code org.apache.http.HttpClientConnection} (via custom
 * {@code org.apache.http.impl.conn.ManagedHttpClientConnectionFactory}) and overriding it's {@code createOutputStream}
 * method.
 * </p>
 * <p>
 * Use of authorization by the AHC-based connectors is dependent on the chunk encoding setting. If the entity buffering
 * is enabled, the entity is buffered and authorization can be performed automatically in response to a 401 by sending
 * the request again. When entity buffering is disabled (chunked encoding is used) then the property
 * {@link org.glassfish.jersey.apache.connector.ApacheClientProperties#PREEMPTIVE_BASIC_AUTHENTICATION} must be set to
 * {@code true}.
 * </p>
 * <p>
 * If a {@link org.glassfish.jersey.client.ClientResponse} is obtained and an entity is not read from the response then
 * {@link org.glassfish.jersey.client.ClientResponse#close()} MUST be called after processing the response to release
 * connection-based resources.
 * </p>
 * <p>
 * If a response entity is obtained that is an instance of {@link java.io.Closeable} then the instance MUST be closed
 * after processing the entity to release connection-based resources.
 * <p/>
 * <p>
 * The following methods are currently supported: HEAD, GET, POST, PUT, DELETE, OPTIONS, PATCH and TRACE.
 * <p/>
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 * @author Arul Dhesiaseelan (aruld at acm.org)
 * @author jorgeluisw at mac.com
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @author Paul Sandoz (paul.sandoz at oracle.com)
 * @since 2.5
 */
public class ApacheConnectorProvider implements ConnectorProvider {

    @Override
    public Connector getConnector(Client client, Configuration runtimeConfig) {
        return new ApacheConnector(runtimeConfig);
    }

    /**
     * Retrieve the underlying Apache {@link HttpClient} instance from {@link org.glassfish.jersey.client.JerseyClient}
     * or {@link org.glassfish.jersey.client.JerseyWebTarget} configured to use {@code ApacheConnectorProvider}.
     *
     * @param component
     *            {@code JerseyClient} or {@code JerseyWebTarget} instance that is configured to use
     *            {@code ApacheConnectorProvider}.
     * @return underlying Apache {@code HttpClient} instance.
     *
     * @throws java.lang.IllegalArgumentException
     *             in case the {@code component} is neither {@code JerseyClient} nor {@code JerseyWebTarget} instance or
     *             in case the component is not configured to use a {@code ApacheConnectorProvider}.
     * @since 2.8
     */
    public static HttpClient getHttpClient(Configurable<?> component) {
        if (!(component instanceof Initializable)) {
            throw new IllegalArgumentException(LocalizationMessages.INVALID_CONFIGURABLE_COMPONENT_TYPE(component
                    .getClass().getName()));
        }

        final Initializable<?> initializable = (Initializable<?>) component;
        Connector connector = initializable.getConfiguration().getConnector();
        if (connector == null) {
            initializable.preInitialize();
            connector = initializable.getConfiguration().getConnector();
        }

        if (connector instanceof ApacheConnector) {
            return ((ApacheConnector) connector).getHttpClient();
        }

        throw new IllegalArgumentException(LocalizationMessages.EXPECTED_CONNECTOR_PROVIDER_NOT_USED());
    }
}
