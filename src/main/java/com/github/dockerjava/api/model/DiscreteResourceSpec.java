package com.github.dockerjava.api.model;

import java.io.Serializable;

/**
 *
 * since 1.24 generic type is String
 * since 1.39 generic type is Integer
 */
public class DiscreteResourceSpec extends GenericResource<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
}
