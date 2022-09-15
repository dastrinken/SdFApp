package de.sdf.companion.model;

public interface ConnectionInterface {
    default void onLoginProcessed(boolean success) {}
}
