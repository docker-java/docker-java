/*
 * Created on 16.02.2016
 */
package com.github.dockerjava.core.async;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;


/**
 *
 * @author Marcus Linke
 *
 */
public class JsonStreamProcessorTest {

    @Test
    public void processEmptyJson() throws Exception {

        InputStream response = new ByteArrayInputStream("{}".getBytes());

        JsonStreamProcessor<PullResponseItem> jsonStreamProcessor = new JsonStreamProcessor<>(
                JSONTestHelper.getMapper(), PullResponseItem.class);

        final List<Boolean> completed = new ArrayList<>();

        jsonStreamProcessor.processResponseStream(response, new ResultCallback<PullResponseItem>() {

            @Override
            public void close() throws IOException {
            }

            @Override
            public void onStart(Closeable closeable) {
            }

            @Override
            public void onNext(PullResponseItem object) {
                assertFalse("onNext called for empty json", true);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                completed.add(true);
            }
        });

        assertFalse("Stream processing not completed", completed.isEmpty());
    }

}
