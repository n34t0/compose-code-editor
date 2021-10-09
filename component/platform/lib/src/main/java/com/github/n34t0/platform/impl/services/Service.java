package com.github.n34t0.platform.impl.services;

import com.intellij.openapi.Disposable;

public interface Service extends Disposable {

    void init(Disposable parentDisposable);

}
