package com.github.dockerjava.jaxrs1;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.AuthCmd;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.ContainerDiffCmd;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.KillContainerCmd;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PauseContainerCmd;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.TagImageCmd;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.UnpauseContainerCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs1.util.JsonClientFilter;
import com.github.dockerjava.jaxrs1.util.SelectiveLoggingFilter;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;

public class DockerCmdExecFactoryImpl implements DockerCmdExecFactory {
	
	private Client client;

	private WebResource baseResource;
	
	private static final Logger LOGGER = Logger.getLogger(DockerCmdExecFactoryImpl.class.getName());
    
	
	@Override
	public void init(DockerClientConfig dockerClientConfig) {
		Preconditions.checkNotNull(dockerClientConfig, "config was not specified");

        HttpClient httpClient = getPoolingHttpClient(dockerClientConfig);
        ClientConfig clientConfig = new DefaultClientConfig();
        client = new ApacheHttpClient4(new ApacheHttpClient4Handler(httpClient,
                null, false), clientConfig);
    
        if(dockerClientConfig.getReadTimeout() != null) {
            client.setReadTimeout(dockerClientConfig.getReadTimeout());
        }
    
        client.addFilter(new JsonClientFilter());
    
        if (dockerClientConfig.isLoggingFilterEnabled())
            client.addFilter(new SelectiveLoggingFilter());
    
        WebResource webResource = client.resource(dockerClientConfig.getUri());
    
        if(dockerClientConfig.getVersion() != null) {
            baseResource = webResource.path("v" + dockerClientConfig.getVersion());
        } else {
            baseResource = webResource;
        }
    }

	private HttpClient getPoolingHttpClient(DockerClientConfig dockerClientConfig) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", dockerClientConfig.getUri().getPort(),
                PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
                .getSocketFactory()));
    
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        // Increase max total connection
        cm.setMaxTotal(1000);
        // Increase default max connection per route
        cm.setDefaultMaxPerRoute(1000);
    
        return new DefaultHttpClient(cm);
    }

    protected WebResource getBaseResource() {
		Preconditions.checkNotNull(baseResource, "Factory not initialized. You probably forgot to call init()!");
		return baseResource;
	}
	
	@Override
	public AuthCmd.Exec createAuthCmdExec() {
		return new AuthCmdExec(getBaseResource());
	}
	
	@Override
	public InfoCmd.Exec createInfoCmdExec() {
		return new InfoCmdExec(getBaseResource());
	}
	
	@Override
	public PingCmd.Exec createPingCmdExec() {
		return new PingCmdExec(getBaseResource());
	}
	
	@Override
	public VersionCmd.Exec createVersionCmdExec() {
		return new VersionCmdExec(getBaseResource());
	}
	
	@Override
	public PullImageCmd.Exec createPullImageCmdExec() {
		return new PullImageCmdExec(getBaseResource());
	}
	
	@Override
	public PushImageCmd.Exec createPushImageCmdExec() {
		return new PushImageCmdExec(getBaseResource());
	}
	
	@Override
	public CreateImageCmd.Exec createCreateImageCmdExec() {
		return new CreateImageCmdExec(getBaseResource());
	}
	
	@Override
	public SearchImagesCmd.Exec createSearchImagesCmdExec() {
		return new SearchImagesCmdExec(getBaseResource());
	}
	
	@Override
	public RemoveImageCmd.Exec createRemoveImageCmdExec() {
		return new RemoveImageCmdExec(getBaseResource());
	}
	
	@Override
	public ListImagesCmd.Exec createListImagesCmdExec() {
		return new ListImagesCmdExec(getBaseResource());
	}
	
	@Override
	public InspectImageCmd.Exec createInspectImageCmdExec() {
		return new InspectImageCmdExec(getBaseResource());
	}
	
	@Override
	public ListContainersCmd.Exec createListContainersCmdExec() {
		return new ListContainersCmdExec(getBaseResource());
	}
	
	@Override
	public CreateContainerCmd.Exec createCreateContainerCmdExec() {
		return new CreateContainerCmdExec(getBaseResource());
	}
	
	@Override
	public StartContainerCmd.Exec createStartContainerCmdExec() {
		return new StartContainerCmdExec(getBaseResource());
	}
	
	@Override
	public InspectContainerCmd.Exec createInspectContainerCmdExec() {
		return new InspectContainerCmdExec(getBaseResource());
	}
	
	@Override
	public RemoveContainerCmd.Exec createRemoveContainerCmdExec() {
		return new RemoveContainerCmdExec(getBaseResource());
	}
	
	@Override
	public WaitContainerCmd.Exec createWaitContainerCmdExec() {
		return new WaitContainerCmdExec(getBaseResource());
	}
	
	@Override
	public AttachContainerCmd.Exec createAttachContainerCmdExec() {
		return new AttachContainerCmdExec(getBaseResource());
	}
	
	@Override
	public LogContainerCmd.Exec createLogContainerCmdExec() {
		return new LogContainerCmdExec(getBaseResource());
	}
	
	@Override
	public CopyFileFromContainerCmd.Exec createCopyFileFromContainerCmdExec() {
		return new CopyFileFromContainerCmdExec(getBaseResource());
	}
	
	@Override
	public StopContainerCmd.Exec createStopContainerCmdExec() {
		return new StopContainerCmdExec(getBaseResource());
	}
	
	@Override
	public ContainerDiffCmd.Exec createContainerDiffCmdExec() {
		return new ContainerDiffCmdExec(getBaseResource());
	}
	
	@Override
	public KillContainerCmd.Exec createKillContainerCmdExec() {
		return new KillContainerCmdExec(getBaseResource());
	}
	
	@Override
	public RestartContainerCmd.Exec createRestartContainerCmdExec() {
		return new RestartContainerCmdExec(getBaseResource());
	}
	
	@Override
	public CommitCmd.Exec createCommitCmdExec() {
		return new CommitCmdExec(getBaseResource());
	}
	
	@Override
	public BuildImageCmd.Exec createBuildImageCmdExec() {
		return new BuildImageCmdExec(getBaseResource());
	}
	
	@Override
	public TopContainerCmd.Exec createTopContainerCmdExec() {
		return new TopContainerCmdExec(getBaseResource());
	}
	
	@Override
	public TagImageCmd.Exec createTagImageCmdExec() {
		return new TagImageCmdExec(getBaseResource());
	}
	
	@Override
	public PauseContainerCmd.Exec createPauseContainerCmdExec() {
		return new PauseContainerCmdExec(getBaseResource());
	}
	
	@Override
	public UnpauseContainerCmd.Exec createUnpauseContainerCmdExec() {
		return new UnpauseContainerCmdExec(baseResource);
	}

	@Override
	public EventsCmd.Exec createEventsCmdExec() {
		return new EventsCmdExec(getBaseResource());
	}

	@Override
	public void close() throws IOException {
		Preconditions.checkNotNull(client, "Factory not initialized. You probably forgot to call init()!");
		client.destroy();
	}

}
