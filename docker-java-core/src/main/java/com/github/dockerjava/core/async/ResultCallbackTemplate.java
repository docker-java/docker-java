/*
 * Created on 16.06.2015
 */
package com.github.dockerjava.core.async;

import com.github.dockerjava.api.async.ResultCallback;

/**
 * Abstract template implementation of {@link ResultCallback}
 *
 * @author Marcus Linke
 * @deprecated use {@link com.github.dockerjava.api.async.ResultCallbackTemplate}
 *
 */
@Deprecated
public abstract class ResultCallbackTemplate<RC_T extends ResultCallback<A_RES_T>, A_RES_T>
        extends com.github.dockerjava.api.async.ResultCallbackTemplate<RC_T, A_RES_T> {

}
