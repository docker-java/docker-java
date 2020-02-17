/*
 * Copyright 2017 cdancy.
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
package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cdancy
 */
@EqualsAndHashCode
@ToString
public class HealthCheck implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Interval")
    private Long interval;

    @JsonProperty("Timeout")
    private Long timeout;

    /**
     * @since 1.26
     */
    @JsonProperty("Test")
    private List<String> test;

    /**
     * @since 1.26
     */
    @JsonProperty("Retries")
    private Integer retries;

    /**
     * @since 1.26
     */
    @JsonProperty("StartPeriod")
    private Long startPeriod;

    public Long getInterval() {
        return interval;
    }

    public Long getTimeout() {
        return timeout;
    }

    public HealthCheck withInterval(Long interval) {
        this.interval = interval;
        return this;
    }

    public HealthCheck withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public List<String> getTest() {
        return test;
    }

    public HealthCheck withTest(List<String> test) {
        this.test = test;
        return this;
    }

    public Integer getRetries() {
        return retries;
    }

    public HealthCheck withRetries(Integer retries) {
        this.retries = retries;
        return this;
    }

    public Long getStartPeriod() {
        return startPeriod;
    }

    public HealthCheck withStartPeriod(Long startPeriod) {
        this.startPeriod = startPeriod;
        return this;
    }
}
