package com.github.n34t0.platform.impl.services.impl;

import com.github.n34t0.platform.impl.services.ApplicationService;
import com.github.n34t0.platform.impl.services.FileService;
import com.github.n34t0.platform.impl.services.ModuleService;

import java.nio.file.Path;

public class ProjectServiceImpl extends AbstractProjectService {

    private final Path myProjectFolder;

    public ProjectServiceImpl(ApplicationService applicationService, FileService fileService,
                              String projectName, Path projectFolder) {
        super(applicationService, fileService, projectName);
        myProjectFolder = projectFolder;
    }

    @Override
    protected Path getProjectFolder() {
        return myProjectFolder;
    }

    @Override
    protected ModuleService createModuleService() {
        return new ModuleServiceImpl(myApplicationService, myFileService);
    }

}
