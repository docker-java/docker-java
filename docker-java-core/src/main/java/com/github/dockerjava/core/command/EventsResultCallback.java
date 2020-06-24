/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marcus Linke
 *
 * @deprecated use {@link com.github.dockerjava.api.async.ResultCallback.Adapter}
 */
@Deprecated
public class EventsResultCallback extends ResultCallbackTemplate<EventsResultCallback, Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsResultCallback.class);

    @Override
    public void onNext(Event item) {
        LOGGER.debug(item.toString());
    }
}
