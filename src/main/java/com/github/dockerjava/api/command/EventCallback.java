package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Event;

/**
 * Event callback
 */
public interface EventCallback {
    public void onEvent(Event event);
    public void onException(Throwable throwable);
    public void onCompletion(int numEvents);
    public boolean isReceiving();
}
