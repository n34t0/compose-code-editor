package com.github.n34t0.ipw.impl;

import com.github.n34t0.ipw.CodeCompletionElement;
import com.github.n34t0.ipw.GotoDeclarationData;
import com.github.n34t0.ipw.IpwProject;
import com.github.n34t0.ipw.impl.services.ApplicationService;
import com.github.n34t0.ipw.impl.services.FileService;
import com.github.n34t0.ipw.impl.services.ProjectService;
import com.github.n34t0.ipw.impl.services.impl.ProjectServiceImpl;
import com.github.n34t0.ipw.impl.services.impl.TempProjectServiceImpl;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Project implements IpwProject, Disposable {

    private ProjectService projectService;
    private boolean isDisposed;

    Project(Path path, Disposable rootDisposable, ApplicationService applicationService, FileService fileService) {
        if (Files.isDirectory(path)) {
            String projectName = path.getFileName().toString();
            projectService = new ProjectServiceImpl(applicationService, fileService, projectName, path);
        } else {
            String projectName = FileUtil.getNameWithoutExtension(path.getFileName().toString());
            projectService = new TempProjectServiceImpl(applicationService, fileService, projectName);
        }
        projectService.init(this);
        projectService.createProject();
        Disposer.register(rootDisposable, this);
    }

    @Override
    public void dispose() {
        isDisposed = true;
        projectService = null;
    }

    @Override
    public void addLibraries(List<String> paths) {
        projectService.addLibraries(paths);
    }

    @Override
    public void synchronizeProjectDirectory() {
        projectService.synchronizeProjectDir();
    }

    @Override
    public List<CodeCompletionElement> getCodeCompletion(String path, int caretOffset) {
        return mapLookupElements(projectService.getCodeCompletion(path, caretOffset));
    }

    @Override
    public GotoDeclarationData gotoDeclaration(String path, int caretOffset) {
        return projectService.gotoDeclaration(path, caretOffset);
    }

    private List<CodeCompletionElement> mapLookupElements(List<LookupElement> elements) {
        return ReadAction.compute(() -> elements.stream()
                                                .map(CcElement::new)
                                                .collect(Collectors.toList()));
    }

    @Override
    public void closeProject() {
        if (isDisposed) {
            throw new IllegalStateException("The project is already closed");
        }
        Disposer.dispose(this);
    }
}
