package com.github.n34t0.ipw.impl.services;

import com.github.n34t0.ipw.impl.GTDData;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

public interface EditorService extends Service {

    void clearFileAndEditor();

    void cancelExecutions();

    List<LookupElement> getCodeCompletion(VirtualFile file, int caretOffset);

    GTDData gotoDeclaration(VirtualFile file, int caretOffset);
}
