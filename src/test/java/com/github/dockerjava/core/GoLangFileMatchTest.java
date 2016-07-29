/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.dockerjava.core.exception.GoLangFileMatchException;

import junit.framework.Assert;

public class GoLangFileMatchTest {

    @Test(dataProvider = "getTestData")
    public void testMatch(MatchTestCase testCase) throws IOException {
        String pattern = testCase.pattern;
        String s = testCase.s;
        if (GoLangFileMatch.IS_WINDOWS) {
            if (pattern.indexOf('\\') >= 0) {
                // no escape allowed on windows.
                return;
            }
            pattern = FilenameUtils.normalize(pattern);
            s = FilenameUtils.normalize(s);
        }
        try {
            Boolean matched = GoLangFileMatch.match(pattern, s);
            if (testCase.expectException) {
                Assert.fail("Expected GoFileMatchException");
            }
            Assert.assertEquals(testCase.toString(), testCase.matches, matched);
        } catch (GoLangFileMatchException e) {
            if (!testCase.expectException) {
                throw e;
            }
        }
    }

    @DataProvider
    public Object[][] getTestData() {
        return new Object[][] {new Object[] {new MatchTestCase("", "abc", false, false)},
                new Object[] {new MatchTestCase("abc", "abc", true, false)},
                new Object[] {new MatchTestCase("*", "abc", true, false)},
                new Object[] {new MatchTestCase("*c", "abc", true, false)},
                new Object[] {new MatchTestCase("a*", "a", true, false)},
                new Object[] {new MatchTestCase("a*", "abc", true, false)},
                new Object[] {new MatchTestCase("a*", "ab/c", true, false)},
                new Object[] {new MatchTestCase("a*/b", "abc/b", true, false)},
                new Object[] {new MatchTestCase("a*/b", "a/c/b", false, false)},
                new Object[] {new MatchTestCase("a*b*c*d*e*/f", "axbxcxdxe/f", true, false)},
                new Object[] {new MatchTestCase("a*b*c*d*e*/f", "axbxcxdxexxx/f", true, false)},
                new Object[] {new MatchTestCase("a*b*c*d*e*/f", "axbxcxdxe/xxx/f", false, false)},
                new Object[] {new MatchTestCase("a*b*c*d*e*/f", "axbxcxdxexxx/fff", false, false)},
                new Object[] {new MatchTestCase("a*b?c*x", "abxbbxdbxebxczzx", true, false)},
                new Object[] {new MatchTestCase("a*b?c*x", "abxbbxdbxebxczzy", false, false)},
                new Object[] {new MatchTestCase("ab[c]", "abc", true, false)},
                new Object[] {new MatchTestCase("ab[b-d]", "abc", true, false)},
                new Object[] {new MatchTestCase("ab[e-g]", "abc", false, false)},
                new Object[] {new MatchTestCase("ab[^c]", "abc", false, false)},
                new Object[] {new MatchTestCase("ab[^b-d]", "abc", false, false)},
                new Object[] {new MatchTestCase("ab[^e-g]", "abc", true, false)},
                new Object[] {new MatchTestCase("a\\*b", "a*b", true, false)},
                new Object[] {new MatchTestCase("a\\*b", "ab", false, false)},
                new Object[] {new MatchTestCase("a?b", "a☺b", true, false)},
                new Object[] {new MatchTestCase("a[^a]b", "a☺b", true, false)},
                new Object[] {new MatchTestCase("a???b", "a☺b", false, false)},
                new Object[] {new MatchTestCase("a[^a][^a][^a]b", "a☺b", false, false)},
                new Object[] {new MatchTestCase("[a-ζ]*", "α", true, false)},
                new Object[] {new MatchTestCase("*[a-ζ]", "A", false, false)},
                new Object[] {new MatchTestCase("a?b", "a/b", false, false)},
                new Object[] {new MatchTestCase("a*b", "a/b", false, false)},
                new Object[] {new MatchTestCase("[\\]a]", "]", true, false)},
                new Object[] {new MatchTestCase("[\\-]", "-", true, false)},
                new Object[] {new MatchTestCase("[x\\-]", "x", true, false)},
                new Object[] {new MatchTestCase("[x\\-]", "-", true, false)},
                new Object[] {new MatchTestCase("[x\\-]", "z", false, false)},
                new Object[] {new MatchTestCase("[\\-x]", "x", true, false)},
                new Object[] {new MatchTestCase("[\\-x]", "-", true, false)},
                new Object[] {new MatchTestCase("[\\-x]", "a", false, false)},
                new Object[] {new MatchTestCase("[]a]", "]", false, true)},
                new Object[] {new MatchTestCase("[-]", "-", false, true)},
                new Object[] {new MatchTestCase("[x-]", "x", false, true)},
                new Object[] {new MatchTestCase("[x-]", "-", false, true)},
                new Object[] {new MatchTestCase("[x-]", "z", false, true)},
                new Object[] {new MatchTestCase("[-x]", "x", false, true)},
                new Object[] {new MatchTestCase("[-x]", "-", false, true)},
                new Object[] {new MatchTestCase("[-x]", "a", false, true)},
                new Object[] {new MatchTestCase("\\", "a", false, true)},
                new Object[] {new MatchTestCase("[a-b-c]", "a", false, true)},
                new Object[] {new MatchTestCase("[", "a", false, true)},
                new Object[] {new MatchTestCase("[^", "a", false, true)},
                new Object[] {new MatchTestCase("[^bc", "a", false, true)},
                new Object[] {new MatchTestCase("a[", "a", false, true)},
                new Object[] {new MatchTestCase("a[", "ab", false, true)},
                new Object[] {new MatchTestCase("*x", "xxx", true, false)},
                new Object[] {new MatchTestCase("a", "a/b/c", true, false)},
                new Object[] {new MatchTestCase("*/b", "a/b/c", true, false)},
                new Object[] {new MatchTestCase("**/b/*/d", "a/b/c/d", true, false)},
                new Object[] {new MatchTestCase("**/c", "a/b/c", true, false)}};
    }

    private final class MatchTestCase {
        private final String pattern;

        private final String s;

        private final Boolean matches;

        private final Boolean expectException;

        public MatchTestCase(String pattern, String s, Boolean matches, Boolean expectException) {
            super();
            this.pattern = pattern;
            this.s = s;
            this.matches = matches;
            this.expectException = expectException;
        }

        @Override
        public String toString() {
            return "MatchTestCase [pattern=" + pattern + ", s=" + s + ", matches=" + matches + ", expectException="
                    + expectException + "]";
        }
    }

}
