/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dockerjava.core.util.guava;

public class Guava {

    static final String URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS =
            "-._~" +        // Unreserved characters.
            "!$'()*,;&=" +  // The subdelim characters (excluding '+').
            "@:";           // The gendelim characters permitted in paths.

    public static PercentEscaper urlPathSegmentEscaper() {
        return URL_PATH_SEGMENT_ESCAPER;
      }

    private static final PercentEscaper URL_PATH_SEGMENT_ESCAPER =
            new PercentEscaper(URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS + "+", false);

    public static String join(String[] joins, String sep, boolean skipNulls) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < joins.length; i++) {
           if(skipNulls && joins[i] == null) {
               continue;
           }
           sb.append(joins[i]);
           if(i < joins.length -1) {
               sb.append(sep);
           }
        }
        return sb.toString();
    }
}
