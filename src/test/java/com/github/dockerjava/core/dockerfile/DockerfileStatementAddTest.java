package com.github.dockerjava.core.dockerfile;

import com.github.dockerjava.api.exception.DockerClientException;
import com.google.common.base.Optional;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class DockerfileStatementAddTest {

    @RunWith(Parameterized.class)
    public static final class ParamTests {

        private static final Logger LOG = LoggerFactory.getLogger(DockerfileStatementAddTest.class);

        @Parameterized.Parameters(name = "{0} {1} {2}")
        public static Object[][] data() {
            return new Object[][]{{"ADD src dest", contains("src"), "dest"},
                    {"ADD \"src file\" \"dest\"", contains("src file"), "dest"},
                    {"ADD src\"file dest", contains("src\"file"), "dest"},
                    {"ADD src1 src2 dest", containsInAnyOrder("src1", "src2"), "dest"},
                    {"COPY src dest", contains("src"), "dest"},
                    {"COPY \"src file\" \"dest\"", contains("src file"), "dest"},
                    {"COPY src\"file dest", contains("src\"file"), "dest"},
                    {"COPY src1 src2 dest", containsInAnyOrder("src1", "src2"), "dest"}};
        }

        @Parameterized.Parameter
        public String command;

        @Parameterized.Parameter(1)
        public Matcher matchesExpectation;

        @Parameterized.Parameter(2)
        public String expectedDest;

        @Test
        public void testAddOrCopyPattern() {
            Optional<DockerfileStatement.Add> optionalAdd = DockerfileStatement.Add.create(command);
            assertThat(optionalAdd.isPresent(), is(true));
            assertThat(optionalAdd.get().sources, matchesExpectation);
            assertThat(optionalAdd.get().destination, is(expectedDest));
        }
    }

    public static final class Tests {
        @Test(expected = DockerClientException.class)
        public void shouldThrowExceptionIfDestNotSpecified() {
            DockerfileStatement.Add.create("ADD src");
        }
    }
}
