/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marcus
 *
 */
public class AttachContainerResultCallback extends ResultCallbackTemplate<AttachContainerResultCallback, Frame> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AttachContainerResultCallback.class);

    @Override
    public void onNext(Frame item) {
        LOGGER.debug(item.toString());
    }
}
