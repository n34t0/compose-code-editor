package com.github.n34t0.ipw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static com.github.n34t0.ipw.WrapperExtension.assertCodeCompletion;
import static com.github.n34t0.ipw.WrapperExtension.ipw;
import static com.github.n34t0.ipw.WrapperExtension.tempDir;

@ExtendWith(WrapperExtension.class)
class AddLibFileTests {

    @Test
    void severalPathsInOneCallTest() {
        var file = tempDir + "/files/LibTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            project.addLibraries(tempDir + "/lib/api.jar", tempDir + "/lib/implA.jar");
            assertCodeCompletion(project, file, List.of("ClassA"), List.of("ClassB", "ClassC"));
            project.closeProject();
        });
    }

    @Test
    void severalCallsTest() {
        var file = tempDir + "/files/LibTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            project.addLibraries(tempDir + "/lib/api.jar");
            project.addLibraries(tempDir + "/lib/implA.jar");
            assertCodeCompletion(project, file, List.of("ClassA"), List.of("ClassB", "ClassC"));
            project.closeProject();
        });
    }

    @Test
    void oneFileAndOneDirInOneCallTest() {
        var file = tempDir + "/files/LibTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            project.addLibraries(tempDir + "/lib/api.jar", tempDir + "/lib/sub");
            assertCodeCompletion(project, file, List.of("ClassB", "ClassC"), List.of("ClassA"));
            project.closeProject();
        });
    }

    @Test
    void rootDirTest() {
        var file = tempDir + "/files/LibTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            project.addLibraries(tempDir + "/lib");
            assertCodeCompletion(project, file, List.of("ClassA", "ClassB", "ClassC"));
            project.closeProject();
        });
    }

    @Test
    void onlyApiTest() {
        var file = tempDir + "/files/LibTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);
            project.addLibraries(tempDir + "/lib/api.jar");
            assertCodeCompletion(project, file, List.of(), List.of("ClassA", "ClassB", "ClassC"));
            project.closeProject();
        });
    }

    @Test
    void addLibsBetweenCodeCompletionCallsTest() {
        var file = tempDir + "/files/LibTest.java";
        Assertions.assertDoesNotThrow(() -> {
            var project = ipw.openFile(file);

            project.addLibraries(tempDir + "/lib/api.jar");
            assertCodeCompletion(project, file, List.of(), List.of("ClassA", "ClassB", "ClassC"));

            project.addLibraries(tempDir + "/lib/implA.jar");
            assertCodeCompletion(project, file, List.of("ClassA"), List.of("ClassB", "ClassC"));

            project.addLibraries(tempDir + "/lib");
            assertCodeCompletion(project, file, List.of("ClassA", "ClassB", "ClassC"));

            project.closeProject();
        });
    }

}
