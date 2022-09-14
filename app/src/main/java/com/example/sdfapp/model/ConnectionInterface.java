package com.example.sdfapp.model;

public interface ConnectionInterface {
    default void onLoginProcessed(boolean success) {}
}
