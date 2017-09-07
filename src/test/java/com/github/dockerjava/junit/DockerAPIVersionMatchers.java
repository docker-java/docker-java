package com.github.dockerjava.junit;

import com.github.dockerjava.core.RemoteApiVersion;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.github.dockerjava.utils.TestUtils.getVersion;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerAPIVersionMatchers {
    private DockerAPIVersionMatchers() {
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
