/*
 * Created on 16.02.2016
 */
package com.github.dockerjava.core.async;

import static org.testng.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PullResponseItem;


/**
 *
 * @author Marcus Linke
 *
 */
public class JsonStreamProcessorTest {

    @Test
    public void processEmptyJson() throws Exception {

        InputStream response = new ByteArrayInputStream("{}".getBytes());

        JsonStreamProcessor<PullResponseItem> jsonStreamProcessor = new JsonStreamProcessor<PullResponseItem>(PullResponseItem.class);

        final List<Boolean> completed = new ArrayList<Boolean>();

        jsonStreamProcessor.processResponseStream(response, new ResultCallback<PullResponseItem>() {

            @Override
            public void close() throws IOException {
            }

            @Override
            public void onStart(Closeable closeable) {
            }

            @Override
            public void onNext(PullResponseItem object) {
                assertFalse(true, "onNext called for empty json");
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                completed.add(true);
            }
        });

        assertFalse(completed.isEmpty(), "Stream processing not completed");
    }

}
