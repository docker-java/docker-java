package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.LoadResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadImageCallback extends ResultCallbackTemplate<LoadImageCallback, LoadResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCallback.class);

    @Override
    public void onNext(LoadResponseItem item) {
        LOGGER.debug(item.toString());
    }
}
