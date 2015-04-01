/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Implementation of golang's file.Match
 *
 * Match returns true if name matches the shell file name pattern. The pattern syntax is:
 *
 * <pre>
 *  pattern:
 *          { term }
 *   term:
 *       '*'         matches any sequence of non-Separator characters
 *      '?'         matches any single non-Separator character
 *       '[' [ '^' ] { character-range } ']'
 *                  character class (must be non-empty)
 *       c           matches character c (c != '*', '?', '\\', '[')
 *       '\\' c      matches character c
 *
 *   character-range:
 *       c           matches character c (c != '\\', '-', ']')
 *       '\\' c      matches character c
 *       lo '-' hi   matches character c for lo <= c <= hi
 *
 *  Match requires pattern to match all of name, not just a substring.
 *  The only possible returned error is ErrBadPattern, when pattern
 *  is malformed.
 *
 * On Windows, escaping is disabled. Instead, '\\' is treated as
 *  path separator.
 * </pre>
 *
 * @author tedo
 *
 */
public class GoLangFileMatch {

    public static final boolean IS_WINDOWS = File.separatorChar == '\\';

    public static boolean match(List<String> patterns, File file) {
        return match(patterns, file.getPath());
    }

    public static boolean match(String pattern, File file) {
        return match(pattern, file.getPath());
    }

    public static boolean match(List<String> patterns, String name) {
        for (String pattern : patterns) {
            if (match(pattern, name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean match(String pattern, String name) {
        Pattern: while (!pattern.isEmpty()) {
            ScanResult scanResult = scanChunk(pattern);
            pattern = scanResult.pattern;
            if (scanResult.star && StringUtils.isEmpty(scanResult.chunk)) {
                // Trailing * matches rest of string unless it has a /.
                return name.indexOf(File.separatorChar) < 0;
            }
            // Look for match at current position.
            String matchResult = matchChunk(scanResult.chunk, name);

            // if we're the last chunk, make sure we've exhausted the name
            // otherwise we'll give a false result even if we could still match
            // using the star
            if (matchResult != null && (matchResult.isEmpty() || !pattern.isEmpty())) {
                name = matchResult;
                continue;
            }
            if (scanResult.star) {
                for (int i = 0; i < name.length() && name.charAt(i) != File.separatorChar; i++) {
                    matchResult = matchChunk(scanResult.chunk, name.substring(i + 1));
                    if (matchResult != null) {
                        // if we're the last chunk, make sure we exhausted the name
                        if (pattern.isEmpty() && !matchResult.isEmpty()) {
                            continue;
                        }
                        name = matchResult;
                        continue Pattern;
                    }
                }
            }
            return false;
        }
        return name.isEmpty();
    }

    static ScanResult scanChunk(String pattern) {
        boolean star = false;
        if (!pattern.isEmpty() && pattern.charAt(0) == '*') {
            pattern = pattern.substring(1);
            star = true;
        }
        boolean inRange = false;
        int i;
        Scan: for (i = 0; i < pattern.length(); i++) {
            switch (pattern.charAt(i)) {
            case '\\': {
                if (!IS_WINDOWS) {
                    // error check handled in matchChunk: bad pattern.
                    if (i + 1 < pattern.length()) {
                        i++;
                    }
                }
                break;
            }
            case '[':
                inRange = true;
                break;
            case ']':
                inRange = false;
                break;
            case '*':
                if (!inRange) {
                    break Scan;
                }
            }
        }
        return new ScanResult(star, pattern.substring(0, i), pattern.substring(i));
    }

    static String matchChunk(String chunk, String s) {
        int chunkLength = chunk.length();
        int chunkOffset = 0;
        int sLength = s.length();
        int sOffset = 0;
        char r;
        while (chunkOffset < chunkLength) {
            if (sOffset == sLength) {
                return null;
            }
            switch (chunk.charAt(chunkOffset)) {
            case '[':
                r = s.charAt(sOffset);
                sOffset++;
                chunkOffset++;
                // We can't end right after '[', we're expecting at least
                // a closing bracket and possibly a caret.
                if (chunkOffset == chunkLength) {
                    throw new GoLangFileMatchException();
                }
                // possibly negated
                boolean negated = chunk.charAt(chunkOffset) == '^';
                if (negated) {
                    chunkOffset++;
                }
                // parse all ranges
                boolean match = false;
                int nrange = 0;
                while (true) {
                    if (chunkOffset < chunkLength && chunk.charAt(chunkOffset) == ']' && nrange > 0) {
                        chunkOffset++;
                        break;
                    }
                    GetEscResult result = getEsc(chunk, chunkOffset, chunkLength);
                    char lo = result.lo;
                    char hi = lo;
                    chunkOffset = result.chunkOffset;
                    if (chunk.charAt(chunkOffset) == '-') {
                        result = getEsc(chunk, ++chunkOffset, chunkLength);
                        chunkOffset = result.chunkOffset;
                        hi = result.lo;
                    }
                    if (lo <= r && r <= hi) {
                        match = true;
                    }
                    nrange++;
                }
                if (match == negated) {
                    return null;
                }
                break;

            case '?':
                if (s.charAt(sOffset) == File.separatorChar) {
                    return null;
                }
                sOffset++;
                chunkOffset++;
                break;
            case '\\':
                if (!IS_WINDOWS) {
                    chunkOffset++;
                    if (chunkOffset == chunkLength) {
                        throw new GoLangFileMatchException();
                    }
                }
                // fallthrough
            default:
                if (chunk.charAt(chunkOffset) != s.charAt(sOffset)) {
                    return null;
                }
                sOffset++;
                chunkOffset++;
            }
        }
        return s.substring(sOffset);
    }

    static GetEscResult getEsc(String chunk, int chunkOffset, int chunkLength) {
        if (chunkOffset == chunkLength) {
            throw new GoLangFileMatchException();
        }
        char r = chunk.charAt(chunkOffset);
        if (r == '-' || r == ']') {
            throw new GoLangFileMatchException();
        }
        if (r == '\\' && !IS_WINDOWS) {
            chunkOffset++;
            if (chunkOffset == chunkLength) {
                throw new GoLangFileMatchException();
            }

        }
        r = chunk.charAt(chunkOffset);
        chunkOffset++;
        if (chunkOffset == chunkLength) {
            throw new GoLangFileMatchException();
        }
        return new GetEscResult(r, chunkOffset);
    }

    private static final class ScanResult {
        public boolean star;
        public String chunk;
        public String pattern;

        public ScanResult(boolean star, String chunk, String pattern) {
            this.star = star;
            this.chunk = chunk;
            this.pattern = pattern;
        }
    }

    private static final class GetEscResult {
        public char lo;
        public int chunkOffset;

        public GetEscResult(char lo, int chunkOffset) {
            this.lo = lo;
            this.chunkOffset = chunkOffset;
        }
    }

}
