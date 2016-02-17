package com.github.dockerjava.test.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

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
        ObjectMapper mapper = new ObjectMapper();
        final TClass tObject = mapper.readValue(getSampleContent(version, context), type);
        return testRoundTrip(tObject, type);
    }

    /**
     * Same as {@link JSONTestHelper#testRoundTrip(java.lang.Object, java.lang.Class)}
     * but via {@link TypeReference}
     */
    public static <TClass> TClass testRoundTrip(TClass item, JavaType type)
            throws IOException, AssertionError {
        ObjectMapper mapper = new ObjectMapper();

        String serialized1 = mapper.writeValueAsString(item);
        JsonNode json1 = mapper.readTree(serialized1);

        TClass deserialized1 = mapper.readValue(serialized1, type);
        String serialized2 = mapper.writeValueAsString(deserialized1);

        JsonNode json2 = mapper.readTree(serialized2);
        TClass deserialized2 = mapper.readValue(serialized2, type);

        assertEquals(json2, json1, "JSONs must be equal after the second roundtrip");
        assertEquals(deserialized2, deserialized2, "Objects must be equal after the second roundtrip");
        assertNotSame(deserialized2, deserialized1, "Objects must be not the same");

        return deserialized2;
    }
}
