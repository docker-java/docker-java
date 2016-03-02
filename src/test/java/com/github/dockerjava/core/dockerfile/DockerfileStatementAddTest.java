package com.github.dockerjava.core.dockerfile;

import com.github.dockerjava.api.exception.DockerClientException;
import com.google.common.base.Optional;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class DockerfileStatementAddTest {

    private static final Logger LOG = LoggerFactory.getLogger(DockerfileStatementAddTest.class);

    @DataProvider(name = "valid scenarios")
    public Object[][] validScenarios() {
        return new Object[][] { {"ADD src dest", contains("src"), "dest"},
                {"ADD \"src file\" \"dest\"", contains("src file"), "dest"},
                {"ADD src\"file dest", contains("src\"file"), "dest"},
                {"ADD src1 src2 dest", containsInAnyOrder("src1", "src2"), "dest"},
                {"COPY src dest", contains("src"), "dest"},
                {"COPY \"src file\" \"dest\"", contains("src file"), "dest"},
                {"COPY src\"file dest", contains("src\"file"), "dest"},
                {"COPY src1 src2 dest", containsInAnyOrder("src1", "src2"), "dest"}};
    }

    @Test(dataProvider = "valid scenarios")
    public void testAddOrCopyPattern(String command, Matcher matchesExpectation, String expectedDest) {
        Optional<DockerfileStatement.Add> optionalAdd = DockerfileStatement.Add.create(command);
        assertThat(optionalAdd.isPresent(), is(true));
        assertThat(optionalAdd.get().sources, matchesExpectation);
        assertThat(optionalAdd.get().destination, is(expectedDest));
    }

    @Test(expectedExceptions = {DockerClientException.class})
    public void shouldThrowExceptionIfDestNotSpecified() {
        DockerfileStatement.Add.create("ADD src");
    }
}
