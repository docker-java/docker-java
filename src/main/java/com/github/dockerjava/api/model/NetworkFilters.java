package com.github.dockerjava.api.model;

/**
 * Created by andrewk on 1/5/16.
 */
public class NetworkFilters extends Filters {

    public Filters withNames(String... names) {
        return withFilter("name", names);
    }

    public Filters withIds(String... ids) {
        return withFilter("id", ids);
    }
}
