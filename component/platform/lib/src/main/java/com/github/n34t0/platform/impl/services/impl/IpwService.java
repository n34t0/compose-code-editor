package com.github.n34t0.platform.impl.services.impl;

import com.github.n34t0.platform.impl.services.Service;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;

public abstract class IpwService implements Service {

    private volatile boolean isInitialized;
    private final Object lockObject = new Object();

    @Override
    public void init(Disposable parentDisposable) {
        if (isInitialized) return;
        synchronized (lockObject) {
            if (isInitialized) return;
            Disposer.register(parentDisposable, this);
            doInit();
            isInitialized = true;
        }
    }

    protected abstract void doInit();

}
