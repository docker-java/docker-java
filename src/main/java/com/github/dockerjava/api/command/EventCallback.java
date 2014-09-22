package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Event;

/**
 * Event callback
 */
public interface EventCallback {
    public void onEvent(Event event);
}
