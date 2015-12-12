/*
 * Created on 17.08.2015
 */
package com.github.dockerjava.core;

import com.github.dockerjava.core.exception.InvalidRepositoryNameException;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.NameParser.HostnameReposName;
import com.github.dockerjava.core.NameParser.ReposTag;

/**
 *
 *
 * @author Marcus Linke
 *
 */
public class NameParserTest {

    @Test
    public void testValidateRepoName() throws Exception {
        NameParser.validateRepoName("repository");
        NameParser.validateRepoName("namespace/repository");
        NameParser.validateRepoName("namespace-with-dashes/repository");
        NameParser.validateRepoName("namespace/repository-with-dashes");
        NameParser.validateRepoName("namespace.with.dots/repository");
        NameParser.validateRepoName("namespace/repository.with.dots");
        NameParser.validateRepoName("namespace_with_underscores/repository");
        NameParser.validateRepoName("namespace/repository_with_underscores");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEmpty() throws Exception {
        NameParser.validateRepoName("");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameExceedsMaxLength() throws Exception {
        NameParser.validateRepoName(StringUtils.repeat("repository", 255));
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEndWithDash() throws Exception {
        NameParser.validateRepoName("repository-");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameStartWithDash() throws Exception {
        NameParser.validateRepoName("-repository");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEndWithDot() throws Exception {
        NameParser.validateRepoName("repository.");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameStartWithDot() throws Exception {
        NameParser.validateRepoName(".repository");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameEndWithUnderscore() throws Exception {
        NameParser.validateRepoName("repository_");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameStartWithUnderscore() throws Exception {
        NameParser.validateRepoName("_repository");
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testValidateRepoNameWithColon() throws Exception {
        NameParser.validateRepoName("repository:with:colon");
    }

    @Test
    public void testResolveSimpleRepositoryName() throws Exception {
        HostnameReposName resolved = NameParser.resolveRepositoryName("repository");
        assertEquals(resolved, new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, "repository"));
    }

    @Test
    public void testResolveRepositoryNameWithNamespace() throws Exception {
        HostnameReposName resolved = NameParser.resolveRepositoryName("namespace/repository");
        assertEquals(resolved, new HostnameReposName(AuthConfig.DEFAULT_SERVER_ADDRESS, "namespace/repository"));
    }

    @Test
    public void testResolveRepositoryNameWithNamespaceAndHostname() throws Exception {
        HostnameReposName resolved = NameParser.resolveRepositoryName("localhost:5000/namespace/repository");
        assertEquals(resolved, new HostnameReposName("localhost:5000", "namespace/repository"));
    }

    @Test(expectedExceptions = InvalidRepositoryNameException.class)
    public void testResolveRepositoryNameWithIndex() throws Exception {
        NameParser.resolveRepositoryName("index.docker.io/repository");
    }

    @Test
    public void testResolveReposTagWithoutTagSimple() throws Exception {
        ReposTag resolved = NameParser.parseRepositoryTag("repository");
        assertEquals(resolved, new ReposTag("repository", ""));

        resolved = NameParser.parseRepositoryTag("namespace/repository");
        assertEquals(resolved, new ReposTag("namespace/repository", ""));

        resolved = NameParser.parseRepositoryTag("localhost:5000/namespace/repository");
        assertEquals(resolved, new ReposTag("localhost:5000/namespace/repository", ""));
    }

    @Test
    public void testResolveReposTagWithTag() throws Exception {
        ReposTag resolved = NameParser.parseRepositoryTag("repository:tag");
        assertEquals(resolved, new ReposTag("repository", "tag"));

        resolved = NameParser.parseRepositoryTag("namespace/repository:tag");
        assertEquals(resolved, new ReposTag("namespace/repository", "tag"));

        resolved = NameParser.parseRepositoryTag("localhost:5000/namespace/repository:tag");
        assertEquals(resolved, new ReposTag("localhost:5000/namespace/repository", "tag"));
    }
}
