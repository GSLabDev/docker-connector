/**
 * Copyright (c) 2003-2017, Great Software Laboratory Pvt. Ltd. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mule.api.callback.SourceCallback;

import com.github.dockerjava.core.async.ResultCallbackTemplate;

public class SourceCallBack<T> extends ResultCallbackTemplate<SourceCallBack<T>, T> {

    private static final Logger logger = LogManager.getLogger(SourceCallBack.class.getName());
    private final SourceCallback callback;

    public SourceCallBack(SourceCallback sourceCallback) {
        this.callback = sourceCallback;
    }

    @Override
    public void onNext(T t) {

        try {
            this.callback.process(t);
        } catch (Exception e) {
            logger.error(e);
        }

    }
}
