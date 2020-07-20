package com.github.dockerjava.jsch;

import com.jcraft.jsch.Session;
import okhttp3.Interceptor;

import java.io.File;
import java.util.Hashtable;

class JschDockerConfig {

    static final String VAR_RUN_DOCKER_SOCK = "/var/run/docker.sock";

    private String socketPath = VAR_RUN_DOCKER_SOCK;
    private Session session;
    private File identityFile;
    private Interceptor interceptor;
    private boolean useSocat;
    private boolean useTcp;
    private boolean useSocket;
    private Integer tcpPort;
    private Hashtable jschConfig;
    private String socatFlags;

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getSocketPath() {
        return socketPath;
    }

    public void setSocketPath(String socketPath) {
        this.socketPath = socketPath;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public File getIdentityFile() {
        return identityFile;
    }

    public void setIdentityFile(File identityFile) {
        this.identityFile = identityFile;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public boolean isUseSocat() {
        return useSocat;
    }

    public void setUseSocat(boolean useSocat) {
        this.useSocat = useSocat;
    }

    public void setUseTcp(boolean useTcp) {
        this.useTcp = useTcp;
    }

    public boolean isUseTcp() {
        return useTcp;
    }

    public boolean isUseSocket() {
        return useSocket;
    }

    public void setUseSocket(boolean useSocket) {
        this.useSocket = useSocket;
    }

    public void setJschConfig(Hashtable jschConfig) {
        this.jschConfig = jschConfig;
    }

    public Hashtable getJschConfig() {
        return jschConfig;
    }

    public String getSocatFlags() {
        return socatFlags != null ? socatFlags : "";
    }

    public void setSocatFlags(String socatFlags) {
        this.socatFlags = socatFlags;
    }
}
