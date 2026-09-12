/*
 * Copyright 2026
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
package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.PullResponseJSONSamples;
import org.junit.Test;

import java.io.IOException;

import static com.github.dockerjava.test.serdes.JSONTestHelper.testRoundTrip;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class PullImageResultCallbackTest {

    @Test
    public void onNextWithErrorAndNullStatusDoesNotThrowNullPointerException() throws IOException {
        PullResponseItem item = testRoundTrip(PullResponseJSONSamples.pullImageResponse_error, PullResponseItem.class);
        assertNull(item.getStatus());

        PullImageResultCallback callback = new PullImageResultCallback();
        callback.onNext(item);
    }

    @Test
    public void awaitCompletionSurfacesPullErrorWhenStatusIsNull() throws Exception {
        PullResponseItem item = testRoundTrip(PullResponseJSONSamples.pullImageResponse_error, PullResponseItem.class);

        PullImageResultCallback callback = new PullImageResultCallback();
        callback.onNext(item);
        callback.onComplete();

        try {
            callback.awaitCompletion();
            fail("Expected DockerClientException");
        } catch (DockerClientException e) {
            assertThat(e.getMessage(), containsString("cccxxx/xxxccc:latest not found"));
        }
    }
}
