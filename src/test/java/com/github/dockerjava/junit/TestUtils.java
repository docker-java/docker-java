package com.github.dockerjava.junit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author Kanstantsin Shautsou
 */
public class TestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
    }

    public static String asString(InputStream response) {
        return consumeAsString(response);
    }

    public static String consumeAsString(InputStream response) {

        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(response, "UTF-8");

            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + (itr.hasNext() ? "\n" : ""));
                LOG.info("line: " + line);
            }
            response.close();

            return logwriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

}
