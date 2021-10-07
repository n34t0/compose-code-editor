package com.github.n34t0.ipw;

public interface GotoDeclarationTarget {

    /**
     * @return path to the file containing the target element
     */
    String getPath();

    /**
     * @return offset in the file to the target element
     */
    int getOffset();

}
