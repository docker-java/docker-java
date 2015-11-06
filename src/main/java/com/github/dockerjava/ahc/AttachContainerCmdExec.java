package com.github.dockerjava.ahc;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.concurrent.Future;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

import org.asynchttpclient.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.WebTarget;

public class AttachContainerCmdExec implements AttachContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachContainerCmdExec.class);

    private URL baseResource;

    private DockerClientConfig dockerClientConfig;

    public AttachContainerCmdExec(URL baseResource, DockerClientConfig dockerClientConfig) {
        this.baseResource = baseResource;
        this.dockerClientConfig = dockerClientConfig;
    }

    // @Override
    // protected AbstractCallbackNotifier<Frame> callbackNotifier(AttachContainerCmd command,
    // ResultCallback<Frame> resultCallback) {
    //
    // WebTarget webTarget = getBaseResource().path("/containers/{id}/attach").resolveTemplate("id",
    // command.getContainerId());
    //
    // webTarget = booleanQueryParam(webTarget, "logs", command.hasLogsEnabled());
    // webTarget = booleanQueryParam(webTarget, "stdout", command.hasStdoutEnabled());
    // // webTarget = booleanQueryParam(webTarget, "stdin", command.hasStdinEnabled());
    // webTarget = booleanQueryParam(webTarget, "stderr", command.hasStderrEnabled());
    // webTarget = booleanQueryParam(webTarget, "stream", command.hasFollowStreamEnabled());
    //
    // LOGGER.trace("POST: {}", webTarget);
    //
    // return new POSTCallbackNotifier<Frame>(new FrameStreamProcessor(), resultCallback, webTarget.request(), null);
    // }

    @Override
    public Void exec(AttachContainerCmd command, ResultCallback<Frame> resultCallback) {
        AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().build();

        AsyncHttpClient c = new DefaultAsyncHttpClient(config);
        c.prepareGet("http://www.ning.com/").execute(new AsyncHandler<String>() {
            private ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            @Override
            public State onStatusReceived(HttpResponseStatus status) throws Exception {
                int statusCode = status.getStatusCode();
                // The Status have been read
                // If you don't want to read the headers,body or stop processing the response
                if (statusCode >= 500) {
                    return State.ABORT;
                }

                return State.CONTINUE;
            }

            @Override
            public State onHeadersReceived(HttpResponseHeaders h) throws Exception {

                // The headers have been read
                // If you don't want to read the body, or stop processing the response
                return State.CONTINUE;
            }

            @Override
            public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                bytes.write(bodyPart.getBodyPartBytes());
                return State.CONTINUE;
            }

            @Override
            public String onCompleted() throws Exception {
                // Will be invoked once the response has been fully read or a ResponseComplete exception
                // has been thrown.
                // NOTE: should probably use Content-Encoding from headers
                return bytes.toString("UTF-8");
            }

            @Override
            public void onThrowable(Throwable t) {
            }
        });

        return null;
    }
}
