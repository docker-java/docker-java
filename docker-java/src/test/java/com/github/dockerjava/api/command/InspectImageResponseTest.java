package com.github.dockerjava.api.command;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.api.model.ContainerConfig;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_25;
import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class InspectImageResponseTest {
    @Test
    public void serder1_22Json() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectImageResponse.class);

        final InspectImageResponse inspectImage = testRoundTrip(VERSION_1_22,
                "images/image1/inspect1.json",
                type
        );

        final ContainerConfig config = new ContainerConfig()
                .withAttachStderr(false)
                .withAttachStdin(false)
                .withAttachStdout(false)
                .withCmd(null)
                .withDomainName("")
                .withEntrypoint(null)
                .withEnv(new String[]{"HOME=/", "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"})
                .withExposedPorts(null)
                .withHostName("aee9ba801acc")
                .withImage("511136ea3c5a64f264b78b5433614aec563103b4d4702f3ba7d4d2698e22c158")
                .withLabels(null)
                .withMacAddress(null)
                .withNetworkDisabled(null)
                .withOnBuild(new String[]{})
                .withStdinOpen(false)
                .withPortSpecs(null)
                .withStdInOnce(false)
                .withTty(false)
                .withUser("")
                .withVolumes(null)
                .withWorkingDir("");

        final ContainerConfig containerConfig = new ContainerConfig()
                .withAttachStderr(false)
                .withAttachStdin(false)
                .withAttachStdout(false)
                .withCmd(new String[]{"/bin/sh", "-c", "#(nop) MAINTAINER hack@worldticket.net"})
                .withDomainName("")
                .withEntrypoint(null)
                .withEnv(new String[]{"HOME=/", "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"})
                .withExposedPorts(null)
                .withHostName("aee9ba801acc")
                .withImage("511136ea3c5a64f264b78b5433614aec563103b4d4702f3ba7d4d2698e22c158")
                .withLabels(null)
                .withMacAddress(null)
                .withNetworkDisabled(null)
                .withOnBuild(new String[]{})
                .withStdinOpen(false)
                .withPortSpecs(null)
                .withStdInOnce(false)
                .withTty(false)
                .withUser("")
                .withVolumes(null)
                .withWorkingDir("");

        assertThat(inspectImage, notNullValue());
        assertThat(inspectImage.getArch(), is("amd64"));
        assertThat(inspectImage.getAuthor(), is("hack@worldticket.net"));
        assertThat(inspectImage.getComment(), is(emptyString()));

        assertThat(inspectImage.getConfig(), notNullValue());
        assertThat(inspectImage.getConfig(), equalTo(config));

        assertThat(inspectImage.getCreated(), is("2014-04-29T19:59:10.84997669Z"));
        assertThat(inspectImage.getContainer(), is("aee9ba801acca0e648ffd91df204ba82ae85d97608a4864a019e2004d7e1b133"));

        assertThat(inspectImage.getContainerConfig(), notNullValue());
        assertThat(inspectImage.getContainerConfig(), equalTo(containerConfig));

        assertThat(inspectImage.getDockerVersion(), is("0.8.1"));
        assertThat(inspectImage.getId(), is("sha256:ee45fe0d1fcdf1a0f9c2d1e36c6f4b3202bbb2032f14d7c9312b27bfcf6aee24"));
        assertThat(inspectImage.getOs(), is("linux"));
        assertThat(inspectImage.getParent(), is(emptyString()));
        assertThat(inspectImage.getSize(), is(0L));

        assertThat(inspectImage.getRepoTags(), hasSize(1));
        assertThat(inspectImage.getRepoTags(), hasItem("hackmann/empty:latest"));

        final GraphDriver aufsGraphDriver = new GraphDriver().withName("aufs");
        final GraphDriver graphDriver = inspectImage.getGraphDriver();
        assertThat(graphDriver, notNullValue());
        assertThat(graphDriver, equalTo(aufsGraphDriver));
        assertThat(graphDriver.getName(), is("aufs"));
        assertThat(graphDriver.getData(), nullValue());

        assertThat(inspectImage.getVirtualSize(), is(0L));


        final InspectImageResponse inspectImageResponse = new InspectImageResponse().withArch("amd64")
                .withAuthor("hack@worldticket.net")
                .withComment("")
                .withConfig(config)
                .withContainer("aee9ba801acca0e648ffd91df204ba82ae85d97608a4864a019e2004d7e1b133")
                .withContainerConfig(containerConfig)
                .withCreated("2014-04-29T19:59:10.84997669Z")
                .withDockerVersion("0.8.1")
                .withId("sha256:ee45fe0d1fcdf1a0f9c2d1e36c6f4b3202bbb2032f14d7c9312b27bfcf6aee24")
                .withOs("linux")
                .withParent("")
                .withSize(0L)
                .withRepoTags(Collections.singletonList("hackmann/empty:latest"))
                .withRepoDigests(Collections.<String>emptyList())
                .withVirtualSize(0L)
                .withGraphDriver(aufsGraphDriver);

        assertThat(inspectImage, equalTo(inspectImageResponse));
    }


    @Test
    public void serder1_22_doc() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectImageResponse.class);

        final InspectImageResponse inspectImage = testRoundTrip(VERSION_1_22,
                "images/docImage/doc.json",
                type
        );

        assertThat(inspectImage, notNullValue());

        assertThat(inspectImage.getRepoDigests(), hasSize(1));
        assertThat(inspectImage.getRepoDigests(),
                contains("localhost:5000/test/busybox/example@" +
                        "sha256:cbbf2f9a99b47fc460d422812b6a5adff7dfee951d8fa2e4a98caa0382cfbdbf")
        );

        assertThat(inspectImage.getRepoTags(), hasSize(3));
        assertThat(inspectImage.getRepoTags(), containsInAnyOrder(
                "example:1.0",
                "example:latest",
                "example:stable"
        ));
    }

    @Test
    public void serder1_22_inspect_doc() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectImageResponse.class);

        final InspectImageResponse inspectImage = testRoundTrip(VERSION_1_22,
                "images/docImage/inspect_doc.json",
                type
        );

        GraphData newGraphData = new GraphData()
                .withDeviceId("5")
                .withDeviceName("docker-253:1-2763198-d2cc496561d6d520cbc0236b4ba88c362c446a7619992123f11c809cded25b47")
                .withDeviceSize("171798691840");

        assertThat(inspectImage, notNullValue());
        GraphDriver graphDriver = inspectImage.getGraphDriver();
        assertThat(graphDriver, notNullValue());
        GraphData data = graphDriver.getData();

        assertThat(data, is(newGraphData));

        assertThat(data.getDeviceId(), is("5"));
        assertThat(data.getDeviceName(),
                is("docker-253:1-2763198-d2cc496561d6d520cbc0236b4ba88c362c446a7619992123f11c809cded25b47"));
        assertThat(data.getDeviceSize(),
                is("171798691840"));
    }

    @Test
    public void testOverlayNetworkRootDir() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectImageResponse.class);

        final InspectImageResponse inspectImage = testRoundTrip(VERSION_1_22, "images/overlay/inspectOverlay.json", type);

        final GraphData overlayGraphData = new GraphData()
                .withRootDir("/var/lib/docker/overlay/7e8d362d6b78d47eafe4863fd129cbcada35dbd419d7188cc1dbf1233d505576/root");
        final GraphDriver overlayGraphDriver = new GraphDriver().withName("overlay").withData(overlayGraphData);
        final GraphDriver graphDriver = inspectImage.getGraphDriver();
        assertThat(graphDriver, notNullValue());
        assertThat(graphDriver, equalTo(overlayGraphDriver));
        assertThat(graphDriver.getName(), is("overlay"));
        assertThat(graphDriver.getData(), equalTo(overlayGraphData));
    }

    @Test
    public void inspectWindowsImage() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectImageResponse.class);

        final InspectImageResponse inspectImage = testRoundTrip(VERSION_1_25,
                "images/windowsImage/doc.json",
                type
        );

        assertThat(inspectImage, notNullValue());

        assertThat(inspectImage.getRepoTags(), hasSize(1));
        assertThat(inspectImage.getRepoTags(), contains(
                "microsoft/nanoserver:latest"
        ));

        assertThat(inspectImage.getRepoDigests(), hasSize(1));
        assertThat(inspectImage.getRepoDigests(), contains("microsoft/nanoserver@" +
                "sha256:aee7d4330fe3dc5987c808f647441c16ed2fa1c7d9c6ef49d6498e5c9860b50b")
        );

        assertThat(inspectImage.getConfig(), notNullValue());
        assertThat(inspectImage.getConfig().getCmd(), is(new String[]{"c:\\windows\\system32\\cmd.exe"}));

        assertThat(inspectImage.getOs(), is("windows"));
        assertThat(inspectImage.getOsVersion(), is("10.0.14393"));
        assertThat(inspectImage.getSize(), is(651862727L));
        assertThat(inspectImage.getVirtualSize(), is(651862727L));

        assertThat(inspectImage.getGraphDriver(), notNullValue());
        assertThat(inspectImage.getGraphDriver().getName(), is("windowsfilter"));
        assertThat(inspectImage.getGraphDriver().getData(), notNullValue());
        assertThat(inspectImage.getGraphDriver().getData().getDir(), is("C:\\control\\windowsfilter\\" +
                "6fe6a289b98276a6a5ca0345156ca61d7b38f3da6bb49ef95af1d0f1ac37e5bf"
        ));

        assertThat(inspectImage.getRootFS(), notNullValue());
        assertThat(inspectImage.getRootFS().getType(), is("layers"));
        assertThat(inspectImage.getRootFS().getLayers(), hasSize(1));
        assertThat(inspectImage.getRootFS().getLayers(), contains(
                "sha256:342d4e407550c52261edd20cd901b5ce438f0b1e940336de3978210612365063"
        ));
    }
}
