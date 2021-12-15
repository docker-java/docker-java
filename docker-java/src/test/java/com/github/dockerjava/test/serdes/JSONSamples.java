package com.github.dockerjava.test.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Samples helper
 *
 * @author Kanstantsin Shautsou
 */
public class JSONSamples {

    /**
     * Access to samples storage.
     *
     * @param version docker version of json sample
     * @param context path to file for interested dump
     * @return Content of JSON sample
     * @throws IOException
     */
    public static String getSampleContent(RemoteApiVersion version, String context) throws IOException {
        File resource = new File("src/test/resources/samples/" + version.getVersion() + "/" + context);
        return FileUtils.readFileToString(resource, "UTF-8");
    }

    public static <TClass> TClass testRoundTrip(RemoteApiVersion version, String context,
                                                JavaType type) throws IOException {
        final TClass tObject = JSONTestHelper.getMapper().readValue(getSampleContent(version, context), type);
        return testRoundTrip(tObject, type);
    }

    /**
     * Same as {@link JSONTestHelper#testRoundTrip(java.lang.Object, java.lang.Class)}
     * but via {@link TypeReference}
     */
    public static <TClass> TClass testRoundTrip(TClass item, JavaType type)
            throws IOException, AssertionError {
        String serialized1 = JSONTestHelper.getMapper().writeValueAsString(item);
        JsonNode json1 = JSONTestHelper.getMapper().readTree(serialized1);

        TClass deserialized1 = JSONTestHelper.getMapper().readValue(serialized1, type);
        String serialized2 = JSONTestHelper.getMapper().writeValueAsString(deserialized1);

        JsonNode json2 = JSONTestHelper.getMapper().readTree(serialized2);
        TClass deserialized2 = JSONTestHelper.getMapper().readValue(serialized2, type);

        assertEquals("JSONs must be equal after the second roundtrip", json2, json1);
        assertEquals("Objects must be equal after the second roundtrip", deserialized2, deserialized2);
        assertNotSame("Objects must be not the same", deserialized2, deserialized1);

        return deserialized2;
    }
}
