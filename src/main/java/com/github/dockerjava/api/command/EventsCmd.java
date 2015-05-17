package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.stream.ObjectStreamCallback;


/**
 * Get events
 *
 * @param since - Show all events created since timestamp
 * @param until - Stream events until this timestamp
 */
public interface EventsCmd extends DockerCmd<Void> {
    public EventsCmd withSince(String since);

    public EventsCmd withUntil(String until);

    public String getSince();

    public String getUntil();

    public EventStreamCallback getEventCallback();
    
    public EventsCmd withEventCallback(EventStreamCallback eventCallback);

    public static interface Exec extends DockerCmdExec<EventsCmd, Void> {
    }
    
    /**
     * {@link Event} stream callback
     */
    public interface EventStreamCallback extends ObjectStreamCallback<Event>{
    	
        
    }

}
