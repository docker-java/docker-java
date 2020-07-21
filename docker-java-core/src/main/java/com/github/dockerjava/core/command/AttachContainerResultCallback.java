/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

/**
 *
 * @author Marcus Linke
 *
 * @deprecated use {@link com.github.dockerjava.api.async.ResultCallback.Adapter}
 */
@Deprecated
public class AttachContainerResultCallback extends ResultCallbackTemplate<AttachContainerResultCallback, Frame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachContainerResultCallback.class);

    @Override
    public void onNext(Frame item) {
        LOGGER.debug(item.toString());
    }
}
