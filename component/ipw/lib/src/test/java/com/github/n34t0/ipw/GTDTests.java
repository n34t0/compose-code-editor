package com.github.n34t0.ipw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.n34t0.ipw.WrapperExtension.assertGotoDeclaration;
import static com.github.n34t0.ipw.WrapperExtension.assertLocalGotoDeclaration;
import static com.github.n34t0.ipw.WrapperExtension.ipw;
import static com.github.n34t0.ipw.WrapperExtension.tempDir;

@ExtendWith(WrapperExtension.class)
class GTDTests {

    @Test
    void localJavaTest() {
        var file = tempDir + "/files/GTDTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            assertLocalGotoDeclaration(project, file);
            project.closeProject();
        });
    }

    @Test
    void javaTest() {
        var projectDir = tempDir + "/projects/GTDJavaProject";
        var file = "org/example/Main.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openProject(projectDir);
            assertGotoDeclaration(project, file, projectDir);
            project.closeProject();
        });
    }

    @Test
    void localKotlinTest() {
        var file = tempDir + "/files/GTDTest.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            assertLocalGotoDeclaration(project, file);
            project.closeProject();
        });
    }

    @Test
    void kotlinTest() {
        var projectDir = tempDir + "/projects/GTDKotlinProject";
        var file = "org/example/main.kt";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openProject(projectDir);
            assertGotoDeclaration(project, file, projectDir);
            project.closeProject();
        });
    }

}
