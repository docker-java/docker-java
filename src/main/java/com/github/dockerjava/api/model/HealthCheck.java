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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cdancy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Integer startPeriod;

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

    public Integer getStartPeriod() {
        return startPeriod;
    }

    public HealthCheck withStartPeriod(Integer startPeriod) {
        this.startPeriod = startPeriod;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
