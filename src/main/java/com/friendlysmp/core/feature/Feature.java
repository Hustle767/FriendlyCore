package com.friendlysmp.core.feature;

public interface Feature {
    String id();
    void enable();
    void disable();
    void reload();
}