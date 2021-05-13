package com.github.dockerjava.jsch;

import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.SSLConfig;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.OpenSSHConfig;
import com.jcraft.jsch.Session;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class JschDockerHttpClient implements DockerHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JschDockerHttpClient.class);

    public static final class Builder {

        private URI dockerHost = null;

        private SSLConfig sslConfig = null;

        private Integer readTimeout = null;

        private Integer connectTimeout = null;

        private Boolean retryOnConnectionFailure = null;

        final JschDockerConfig jschDockerConfig = new JschDockerConfig();

        public Builder dockerHost(URI value) {
            this.dockerHost = Objects.requireNonNull(value, "dockerHost");
            return this;
        }

        public Builder sslConfig(SSLConfig sslConfig) {
            this.sslConfig = sslConfig;
            return this;
        }

        public Builder readTimeout(Integer value) {
            this.readTimeout = value;
            return this;
        }

        public Builder connectTimeout(Integer value) {
            this.connectTimeout = value;
            return this;
        }

        Builder retryOnConnectionFailure(Boolean value) {
            this.retryOnConnectionFailure = value;
            return this;
        }

        /**
         * use socket and overwrite default socket path {@link JschDockerConfig#VAR_RUN_DOCKER_SOCK}
         *
         * @param socketPath
         * @return
         */
        public Builder useSocket(String socketPath) {
            this.jschDockerConfig.setUseSocket(true);
            this.jschDockerConfig.setSocketPath(socketPath);
            return this;
        }

        /**
         * pass {@link Session} if already connected
         *
         * @param session
         * @return
         */
        public Builder sshSession(Session session) {
            this.jschDockerConfig.setSession(session);
            return this;
        }

        /**
         * set identityFile for public key authentication
         *
         * @param identityFile
         * @return
         */
        public Builder identityFile(File identityFile) {
            this.jschDockerConfig.setIdentityFile(identityFile);
            return this;
        }

        /**
         * set identityFile from ~/.ssh/ folder for public key authentication
         *
         * @param privateKey private key filename
         * @return
         */
        public Builder identity(String privateKey) {
            return identityFile(new File(System.getProperty("user.home") + File.separator + ".ssh" + File.separator + privateKey));
        }

        /**
         * pass config which is used for {@link Session#setConfig(Hashtable)}
         *
         * @param jschConfig
         * @return
         */
        public Builder jschConfig(Hashtable jschConfig) {
            this.jschDockerConfig.setJschConfig(jschConfig);
            return this;
        }

        public Builder interceptor(Interceptor interceptor) {
            this.jschDockerConfig.setInterceptor(interceptor);
            return this;
        }

        public Builder useSocket() {
            this.jschDockerConfig.setUseSocket(true);
            return this;
        }

        public Builder useSocat() {
            this.jschDockerConfig.setUseSocat(true);
            return this;
        }

        /**
         * allows to set additional flags for the socat call, i.e. -v
         *
         * @param socatFlags
         * @return
         */
        public Builder socatFlags(String socatFlags) {
            this.jschDockerConfig.setSocatFlags(socatFlags);
            return this;
        }

        public Builder useTcp() {
            this.jschDockerConfig.setUseTcp(true);
            return this;
        }

        public Builder useTcp(int port) {
            this.jschDockerConfig.setUseTcp(true);
            this.jschDockerConfig.setTcpPort(port);
            return this;
        }

        public JschDockerHttpClient build() throws IOException, JSchException {
            Objects.requireNonNull(dockerHost, "dockerHost not provided");
            return new JschDockerHttpClient(
                dockerHost,
                sslConfig,
                readTimeout,
                connectTimeout,
                retryOnConnectionFailure,
                jschDockerConfig);
        }
    }

    private static final String SOCKET_SUFFIX = ".socket";

    final OkHttpClient client;

    final OkHttpClient streamingClient;

    private HttpUrl baseUrl;

    private Session session;
    private boolean externalSession = false;

    private JschDockerHttpClient(
        URI dockerHostUri,
        SSLConfig sslConfig,
        Integer readTimeout,
        Integer connectTimeout,
        Boolean retryOnConnectionFailure,
        JschDockerConfig jschDockerConfig) throws IOException, JSchException {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HijackingInterceptor())
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true);

        if (jschDockerConfig.getInterceptor() != null) {
            clientBuilder.addInterceptor(jschDockerConfig.getInterceptor());
        }

        if (readTimeout != null) {
            clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        }

        if (connectTimeout != null) {
            clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        }

        if (retryOnConnectionFailure != null) {
            clientBuilder.retryOnConnectionFailure(retryOnConnectionFailure);
        }

        this.session = jschDockerConfig.getSession();
        this.externalSession = jschDockerConfig.getSession() != null;

        if ("ssh".equals(dockerHostUri.getScheme())) {

            this.session = connectSSH(dockerHostUri, connectTimeout != null ? connectTimeout : 0, jschDockerConfig);

            final JSchSocketFactory socketFactory = new JSchSocketFactory(session, jschDockerConfig);

            clientBuilder.socketFactory(socketFactory);

            clientBuilder
                .connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
                .dns(hostname -> {
                    if (hostname.endsWith(SOCKET_SUFFIX)) {
                        return Collections.singletonList(InetAddress.getByAddress(hostname, new byte[]{0, 0, 0, 0}));
                    } else {
                        return Dns.SYSTEM.lookup(hostname);
                    }
                });
        } else {
            throw new IllegalArgumentException("this implementation only supports ssh connection scheme.");
        }

        if (sslConfig != null) {
            try {
                SSLContext sslContext = sslConfig.getSSLContext();
                if (sslContext != null) {
                    clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), new TrustAllX509TrustManager());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        client = clientBuilder.build();

        streamingClient = client.newBuilder().build();

        // we always use socketFactory, therefore we only need:
        baseUrl = new HttpUrl.Builder().scheme("http").host("127.0.0.1").build();
    }

    private RequestBody toRequestBody(Request request) {
        InputStream body = request.body();
        if (body != null) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    sink.writeAll(Okio.source(body));
                }
            };
        }
        if ("POST".equals(request.method())) {
            return RequestBody.create(null, "");
        }
        return null;
    }

    @Override
    public Response execute(Request request) {

        String url = baseUrl.toString();
        if (url.endsWith("/") && request.path().startsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
            .url(url + request.path())
            .tag(Request.class, request)
            .method(request.method(), toRequestBody(request));

        request.headers().forEach(requestBuilder::header);

        final OkHttpClient clientToUse;

        if (request.hijackedInput() == null) {
            clientToUse = client;
        } else {
            clientToUse = streamingClient;
        }

        Call call = clientToUse.newCall(requestBuilder.build());
        try {
            return new OkResponse(call);
        } catch (IOException e) {
            call.cancel();
            throw new UncheckedIOException("Error while executing " + request, e);
        }
    }

    @Override
    public void close() {
        try {
            disconnectSSH();
        } finally {
            for (OkHttpClient clientToClose : new OkHttpClient[]{client, streamingClient}) {
                clientToClose.dispatcher().cancelAll();
                clientToClose.dispatcher().executorService().shutdown();
                clientToClose.connectionPool().evictAll();
            }
        }
    }

    static class OkResponse implements Response {

        static final ThreadLocal<Boolean> CLOSING = ThreadLocal.withInitial(() -> false);

        private final Call call;

        private final okhttp3.Response response;

        OkResponse(Call call) throws IOException {
            this.call = call;
            this.response = call.execute();
        }

        @Override
        public int getStatusCode() {
            return response.code();
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return response.headers().toMultimap();
        }

        @Override
        public InputStream getBody() {
            ResponseBody body = response.body();
            if (body == null) {
                return null;
            }

            return body.source().inputStream();
        }

        @Override
        public void close() {
            boolean previous = CLOSING.get();
            CLOSING.set(true);
            try {
                call.cancel();
                response.close();
            } catch (Exception | AssertionError e) {
                LOGGER.debug("Failed to close the response", e);
            } finally {
                CLOSING.set(previous);
            }
        }
    }

    static class TrustAllX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private Session connectSSH(URI connectionString, int connectTimeout, JschDockerConfig jschDockerConfig)
        throws IOException, JSchException {

        if (session != null && session.isConnected()) {
            return session;
        }

        final JSch jSch = new JSch();
        JSch.setLogger(new JschLogger());

        final String configFile = System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "config";
        final File file = new File(configFile);
        if (file.exists()) {
            final OpenSSHConfig openSSHConfig = OpenSSHConfig.parseFile(file.getAbsolutePath());
            jSch.setConfigRepository(openSSHConfig);
        }

        final int port = connectionString.getPort() > 0 ? connectionString.getPort() : 22;
        final Session newSession = jSch.getSession(connectionString.getUserInfo(), connectionString.getHost(), port);

        // https://stackoverflow.com/questions/10881981/sftp-connection-through-java-asking-for-weird-authentication
        newSession.setConfig("PreferredAuthentications", "publickey");

        if (jschDockerConfig.getJschConfig() != null) {
            newSession.setConfig(jschDockerConfig.getJschConfig());
        }

        if (jschDockerConfig.getIdentityFile() != null) {
            jSch.addIdentity(jschDockerConfig.getIdentityFile().getAbsolutePath());
        }

        newSession.connect(connectTimeout);

        return newSession;
    }

    private void disconnectSSH() {
        if (!externalSession) {
            session.disconnect();
        }
    }
}
