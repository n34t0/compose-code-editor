package com.github.n34t0.platform.impl.services;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.projectRoots.Sdk;

public interface ApplicationService extends Service {

    Application getApplication();

    Sdk getSdk();

    void setDataProvider(DataProvider provider);

    void dispatchAllInvocationEvents();
}
