package com.base.common.viewmodel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 异步观察者
 *
 * @param <T>
 */
public abstract class BaseRxObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
