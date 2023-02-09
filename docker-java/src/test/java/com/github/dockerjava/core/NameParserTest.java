/*
 * Created on 17.08.2015
 */
package com.github.dockerjava.core;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.NameParser.HostnameReposName;
import com.github.dockerjava.core.NameParser.ReposTag;
import com.github.dockerjava.core.exception.InvalidRepositoryNameException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 *
 * @author Marcus Linke
 *
 */
public class NameParserTest {

    @Test
    public void testValidateRepoName() {
        NameParser.validateRepoName("repository");
        NameParser.validateRepoName("namespace/repository");
        NameParser.validateRepoName("namespace-with-dashes/repository");
        NameParser.validateRepoName("namespace/repository-with-dashes");
        NameParser.validateRepoName("namespace.with.dots/repository");
        NameParser.validateRepoName("namespace/repository.with.dots");
        NameParser.validateRepoName("namespace_with_underscores/repository");
        NameParser.validateRepoName("namespace/repository_with_underscores");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEmpty() {
        NameParser.validateRepoName("");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameExceedsMaxLength() {
        NameParser.validateRepoName(StringUtils.repeat("repository", 255));
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEndWithDash() {
        NameParser.validateRepoName("repository-");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameStartWithDash() {
        NameParser.validateRepoName("-repository");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEndWithDot() {
        NameParser.validateRepoName("repository.");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameStartWithDot() {
        NameParser.validateRepoName(".repository");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEndWithUnderscore() {
        NameParser.validateRepoName("repository_");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameStartWithUnderscore() {
        NameParser.validateRepoName("_repository");
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testValidateRepoNameWithColon() {
        NameParser.validateRepoName("repository:with:colon");
    }

    @Test
    public void testResolveSimpleRepositoryName() {
        HostnameReposName resolved = NameParser.resolveRepositoryName("repository");
        assertEquals(new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, "repository"), resolved);
    }

    @Test
    public void testResolveRepositoryNameWithNamespace() {
        HostnameReposName resolved = NameParser.resolveRepositoryName("namespace/repository");
        assertEquals(new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, "namespace/repository"), resolved);
    }

    @Test
    public void testResolveRepositoryNameWithNamespaceAndSHA256() {
        HostnameReposName resolved = NameParser.resolveRepositoryName("namespace/repository@sha256:sha256");
        assertEquals(new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, "namespace/repository@sha256:sha256"), resolved);
    }

    @Test
    public void testResolveRepositoryNameWithNamespaceAndHostname() {
        HostnameReposName resolved = NameParser.resolveRepositoryName("localhost:5000/namespace/repository");
        assertEquals(new HostnameReposName("localhost:5000", "namespace/repository"), resolved);
    }

    @Test
    public void testResolveRepositoryNameWithNamespaceAndHostnameAndSHA256() {
        HostnameReposName resolved = NameParser.resolveRepositoryName("localhost:5000/namespace/repository@sha256:sha256");
        assertEquals(new HostnameReposName("localhost:5000", "namespace/repository"), resolved);
    }

    @Test(expected = InvalidRepositoryNameException.class)
    public void testResolveRepositoryNameWithIndex() {
        NameParser.resolveRepositoryName("index.docker.io/repository");
    }

    @Test
    public void testResolveReposTagWithoutTagSimple() {
        ReposTag resolved = NameParser.parseRepositoryTag("repository");
        assertEquals(new ReposTag("repository", ""), resolved);

        resolved = NameParser.parseRepositoryTag("namespace/repository");
        assertEquals(new ReposTag("namespace/repository", ""), resolved);

        resolved = NameParser.parseRepositoryTag("localhost:5000/namespace/repository");
        assertEquals(new ReposTag("localhost:5000/namespace/repository", ""), resolved);
    }

    @Test
    public void testResolveReposTagWithTag() {
        ReposTag resolved = NameParser.parseRepositoryTag("repository:tag");
        assertEquals(new ReposTag("repository", "tag"), resolved);

        resolved = NameParser.parseRepositoryTag("namespace/repository:tag");
        assertEquals(new ReposTag("namespace/repository", "tag"), resolved);

        resolved = NameParser.parseRepositoryTag("localhost:5000/namespace/repository:tag");
        assertEquals(new ReposTag("localhost:5000/namespace/repository", "tag"), resolved);
    }

    @Test
    public void testResolveReposTagWithSHA256() {
        ReposTag resolved = NameParser.parseRepositoryTag("repository@sha256:sha256");
        assertEquals(new ReposTag("repository@sha256:sha256", ""), resolved);

        resolved = NameParser.parseRepositoryTag("namespace/repository@sha256:sha256");
        assertEquals(new ReposTag("namespace/repository@sha256:sha256", ""), resolved);

        resolved = NameParser.parseRepositoryTag("localhost:5000/namespace/repository@sha256:sha256");
        assertEquals(new ReposTag("localhost:5000/namespace/repository@sha256:sha256", ""), resolved);
    }
}
