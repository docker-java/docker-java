/*
 * Copyright 2015 Oleg Nenashev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dockerjava.test.serdes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides helper methods for serialization-deserialization tests.
 *
 * @author Oleg Nenashev
 */
public class JSONTestHelper {

    /**
     * Reads JSON String from the specified resource
     * 
     * @param resource
     *            JSON File
     * @return JSON String
     * @throws IOException
     *             JSON Conversion error
     */
    public static String readString(JSONResourceRef resource) throws IOException {
        try (InputStream istream = resource.getResourceClass().getResourceAsStream(resource.getFileName())) {
            if (istream == null) {
                throw new IOException("Cannot retrieve resource " + resource.getFileName());
            }
            return IOUtils.toString(istream, "UTF-8");
        }
    }

    /**
     * Reads item from the resource.
     * 
     * @param <TClass>
     *            Data class to be read
     * @param resource
     *            Resource reference
     * @param tclass
     *            Class entry
     * @return Item
     * @throws IOException
     *             JSON conversion error
     */
    public static <TClass> TClass readObject(JSONResourceRef resource, Class<TClass> tclass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String str = readString(resource);
        return mapper.readValue(str, tclass);
    }

    /**
     * Basic serialization-deserialization consistency test for the resource.
     * 
     * @param <TClass>
     *            Data class
     * @param resource
     *            Resource reference
     * @param tclass
     *            Class entry
     * @throws IOException
     *             JSON conversion error
     * @throws AssertionError
     *             Validation error
     * @return Deserialized object after the roundtrip
     */
    public static <TClass> TClass testRoundTrip(JSONResourceRef resource, Class<TClass> tclass) throws IOException,
            AssertionError {
        TClass item = readObject(resource, tclass);
        assertNotNull(item);
        return testRoundTrip(item, tclass);
    }

    /**
     * Performs roundtrip test for the specified class.
     * 
     * @param <TClass>
     *            Item class
     * @param item
     *            Item to be checked
     * @return Deserialized object after the roundtrip
     * @throws IOException
     *             JSON Conversion error
     * @throws AssertionError
     *             Validation error
     */
    @SuppressWarnings("unchecked")
    public static <TClass> TClass testRoundTrip(TClass item) throws IOException, AssertionError {
        return testRoundTrip(item, (Class<TClass>) item.getClass());
    }

    /**
     * Performs roundtrip test for the specified class.
     * 
     * @param <TClass>
     *            Item class
     * @param item
     *            Item to be checked
     * @param asclass
     *            Class to be used during conversions
     * @return Deserialized object after the roundtrip
     * @throws IOException
     *             JSON Conversion error
     * @throws AssertionError
     *             Validation error
     */
    public static <TClass> TClass testRoundTrip(TClass item, Class<TClass> asclass) throws IOException, AssertionError {
        ObjectMapper mapper = new ObjectMapper();

        String serialized1 = mapper.writeValueAsString(item);
        JsonNode json1 = mapper.readTree(serialized1);
        TClass deserialized1 = mapper.readValue(serialized1, asclass);
        String serialized2 = mapper.writeValueAsString(deserialized1);
        JsonNode json2 = mapper.readTree(serialized2);
        TClass deserialized2 = mapper.readValue(serialized2, asclass);

        assertEquals(json2, json1, "JSONs must be equal after the second roundtrip");
        return deserialized2;
    }
}
