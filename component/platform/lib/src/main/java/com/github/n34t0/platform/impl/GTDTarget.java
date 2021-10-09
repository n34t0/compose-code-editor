package com.github.n34t0.platform.impl;

import com.github.n34t0.platform.GotoDeclarationTarget;

public class GTDTarget implements GotoDeclarationTarget {
    private final String path;
    private final int offset;

    public GTDTarget(String path, int offset) {
        this.path = path;
        this.offset = offset;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "GTDTarget{" +
            "path='" + path + '\'' +
            ", offset=" + offset +
            '}';
    }
}
