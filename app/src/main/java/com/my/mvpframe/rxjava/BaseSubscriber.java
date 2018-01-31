package com.my.mvpframe.rxjava;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by ZJ register 2018/1/30 0030.
 */

public abstract class BaseSubscriber<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        _onError(t.getMessage());
    }

    @Override
    public void onComplete() {

    }
    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
