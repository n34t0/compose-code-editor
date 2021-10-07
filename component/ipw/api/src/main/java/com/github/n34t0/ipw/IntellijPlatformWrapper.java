package com.github.n34t0.ipw;

/**
 * Interface wrapper to work with the intellij platform.
 *
 * Only one initialized instance of this interface can exist at runtime.
 * The intellij platform cannot be re-initialized after stopping.
 */
public interface IntellijPlatformWrapper {

    /**
     * Initializes the intellij platform. Must be called first.
     */
    void init();

    /**
     * Stops the intellij platform, closes projects, and all open resources.
     */
    void stop();

    /**
     * Creates a new project.
     * @param rootFolder - the root folder of the project, which contains all the source files.
     */
    IpwProject openProject(String rootFolder);

    /**
     * Creates a temporary project for only one file.
     * Allows to use code completion for this file.
     */
    IpwProject openFile(String filePath);
}
