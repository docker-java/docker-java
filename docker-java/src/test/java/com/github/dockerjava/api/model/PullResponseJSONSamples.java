/*
 * Copyright 2015 Zach Marshall.
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
package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONResourceRef;

/**
 * Enumeration for the available pull response statuses.
 *
 * @author Zach Marshall
 */
public enum PullResponseJSONSamples implements JSONResourceRef {
    pullImageResponse_legacy, pullImageResponse_error, pullImageResponse_newerImage, pullImageResponse_upToDate;

    @Override
    public String getFileName() {
        return this + ".json";
    }

    @Override
    public Class<?> getResourceClass() {
        return PullResponseJSONSamples.class;
    }
}
