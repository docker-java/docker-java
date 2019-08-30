/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.github.dockerjava.core.exception.GoLangFileMatchException;

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
 AuthConfigTest *  path separator.
 * </pre>
 *
 * @author tedo
 *
 */
public class GoLangFileMatch {
    private GoLangFileMatch() {
    }

    public static final boolean IS_WINDOWS = File.separatorChar == '\\';

    private static final String PATTERN_CHARS_TO_ESCAPE = "\\.[]{}()*+-?^$|";

    public static boolean match(List<String> patterns, File file) {
        return !match(patterns, file.getPath()).isEmpty();
    }

    public static boolean match(String pattern, File file) {
        return match(pattern, file.getPath());
    }

    /**
     * Returns the matching patterns for the given string
     */
    public static List<String> match(List<String> patterns, String name) {
        List<String> matches = new ArrayList<String>();
        for (String pattern : patterns) {
            if (match(pattern, name)) {
                matches.add(pattern);
            }
        }
        return matches;
    }

    public static boolean match(String pattern, String name) {
        return buildPattern(pattern).matcher(name).matches();
    }

    private static Pattern buildPattern(String pattern) {
        StringBuilder patternStringBuilder = new StringBuilder("^");
        while (!pattern.isEmpty()) {
            pattern = appendChunkPattern(patternStringBuilder, pattern);

            if (!pattern.isEmpty()) {
                patternStringBuilder.append(quote(File.separatorChar));
            }
        }
        patternStringBuilder.append("(").append(quote(File.separatorChar)).append(".*").append(")?");
        return Pattern.compile(patternStringBuilder.toString());
    }

    private static String quote(char separatorChar) {
        if (StringUtils.contains(PATTERN_CHARS_TO_ESCAPE, separatorChar)) {
            return "\\" + separatorChar;
        } else {
            return String.valueOf(separatorChar);
        }
    }

    private static String appendChunkPattern(StringBuilder patternStringBuilder, String pattern) {
        if (pattern.equals("**") || pattern.startsWith("**" + File.separator)) {
            patternStringBuilder.append("(")
                    .append("[^").append(quote(File.separatorChar)).append("]*")
                    .append("(")
                    .append(quote(File.separatorChar)).append("[^").append(quote(File.separatorChar)).append("]*")
                    .append(")*").append(")?");
            return pattern.substring(pattern.length() == 2 ? 2 : 3);
        }

        boolean inRange = false;
        int rangeFrom = 0;
        RangeParseState rangeParseState = RangeParseState.CHAR_EXPECTED;
        boolean isEsc = false;
        int i;
        for (i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case '/':
                    if (!inRange) {
                        if (!IS_WINDOWS && !isEsc) {
                            // end of chunk
                            return pattern.substring(i + 1);
                        } else {
                            patternStringBuilder.append(quote(c));
                        }
                    } else {
                        rangeParseState = nextStateAfterChar(rangeParseState);
                    }
                    isEsc = false;
                    break;
                case '\\':
                    if (!inRange) {
                        if (!IS_WINDOWS) {
                            if (isEsc) {
                                patternStringBuilder.append(quote(c));
                                isEsc = false;
                            } else {
                                isEsc = true;
                            }
                        } else {
                            // end of chunk
                            return pattern.substring(i + 1);
                        }
                    } else {
                        if (IS_WINDOWS || isEsc) {
                            rangeParseState = nextStateAfterChar(rangeParseState);
                            isEsc = false;
                        } else {
                            isEsc = true;
                        }
                    }
                    break;
                case '[':
                    if (!isEsc) {
                        if (inRange) {
                            throw new GoLangFileMatchException("[ not expected, closing bracket ] not yet reached");
                        }
                        rangeFrom = i;
                        rangeParseState = RangeParseState.CHAR_EXPECTED;
                        inRange = true;
                    } else {
                        if (!inRange) {
                            patternStringBuilder.append(c);
                        } else {
                            rangeParseState = nextStateAfterChar(rangeParseState);
                        }
                    }
                    isEsc = false;
                    break;
                case ']':
                    if (!isEsc) {
                        if (!inRange) {
                            throw new GoLangFileMatchException("] is not expected, [ was not met");
                        }
                        if (rangeParseState == RangeParseState.CHAR_EXPECTED_AFTER_DASH) {
                            throw new GoLangFileMatchException("Character range not finished");
                        }
                        patternStringBuilder.append(pattern.substring(rangeFrom, i + 1));
                        inRange = false;
                    } else {
                        if (!inRange) {
                            patternStringBuilder.append(c);
                        } else {
                            rangeParseState = nextStateAfterChar(rangeParseState);
                        }
                    }
                    isEsc = false;
                    break;
                case '*':
                    if (!inRange) {
                        if (!isEsc) {
                            patternStringBuilder.append("[^").append(quote(File.separatorChar)).append("]*");
                        } else {
                            patternStringBuilder.append(quote(c));
                        }
                    } else {
                        rangeParseState = nextStateAfterChar(rangeParseState);
                    }
                    isEsc = false;
                    break;
                case '?':
                    if (!inRange) {
                        if (!isEsc) {
                            patternStringBuilder.append("[^").append(quote(File.separatorChar)).append("]");
                        } else {
                            patternStringBuilder.append(quote(c));
                        }
                    } else {
                        rangeParseState = nextStateAfterChar(rangeParseState);
                    }
                    isEsc = false;
                    break;
                case '-':
                    if (!inRange) {
                        patternStringBuilder.append(quote(c));
                    } else {
                        if (!isEsc) {
                            if (rangeParseState != RangeParseState.CHAR_OR_DASH_EXPECTED) {
                                throw new GoLangFileMatchException("- character not expected");
                            }
                            rangeParseState = RangeParseState.CHAR_EXPECTED_AFTER_DASH;
                        } else {
                            rangeParseState = nextStateAfterChar(rangeParseState);
                        }
                    }
                    isEsc = false;
                    break;
                default:
                    if (!inRange) {
                        patternStringBuilder.append(quote(c));
                    } else {
                        rangeParseState = nextStateAfterChar(rangeParseState);
                    }
                    isEsc = false;
            }
        }
        if (isEsc) {
            throw new GoLangFileMatchException("Escaped character missing");
        }
        if (inRange) {
            throw new GoLangFileMatchException("Character range not finished");
        }
        return "";
    }

    private static RangeParseState nextStateAfterChar(RangeParseState currentState) {
        if (currentState == RangeParseState.CHAR_EXPECTED_AFTER_DASH) {
            return RangeParseState.CHAR_EXPECTED;
        } else {
            return RangeParseState.CHAR_OR_DASH_EXPECTED;
        }
    }

    private enum RangeParseState {
        CHAR_EXPECTED,
        CHAR_OR_DASH_EXPECTED,
        CHAR_EXPECTED_AFTER_DASH
    }

}
