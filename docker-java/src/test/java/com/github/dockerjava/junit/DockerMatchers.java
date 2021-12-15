package com.github.dockerjava.junit;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerRule;
import com.github.dockerjava.core.RemoteApiVersion;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static com.github.dockerjava.utils.TestUtils.getVersion;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerMatchers {
    private DockerMatchers() {
    }

    public static MountedVolumes mountedVolumes(Matcher<? super List<Volume>> subMatcher) {
        return new MountedVolumes(subMatcher, "Mounted volumes", "mountedVolumes");
    }

    public static class MountedVolumes extends FeatureMatcher<InspectContainerResponse, List<Volume>> {
        public MountedVolumes(Matcher<? super List<Volume>> subMatcher, String featureDescription, String featureName) {
            super(subMatcher, featureDescription, featureName);
        }

        @Override
        public List<Volume> featureValueOf(InspectContainerResponse item) {
            List<Volume> volumes = new ArrayList<>();
            for (InspectContainerResponse.Mount mount : item.getMounts()) {
                volumes.add(mount.getDestination());
            }
            return volumes;
        }
    }


    public static Matcher<DockerRule> apiVersionGreater(final RemoteApiVersion version) {
        return new CustomTypeSafeMatcher<DockerRule>("is greater") {
            public boolean matchesSafely(DockerRule dockerRule) {
                return getVersion(dockerRule.getClient()).isGreater(version);
            }
        };
    }

    public static Matcher<DockerRule> isGreaterOrEqual(final RemoteApiVersion version) {
        return new CustomTypeSafeMatcher<DockerRule>("is greater or equal") {
            public boolean matchesSafely(DockerRule dockerRule) {
                return getVersion(dockerRule.getClient()).isGreaterOrEqual(version);
            }

            @Override
            protected void describeMismatchSafely(DockerRule rule, Description mismatchDescription) {
                mismatchDescription
                        .appendText(" was ")
                        .appendText(getVersion(rule.getClient()).toString());
            }
        };
    }
}
