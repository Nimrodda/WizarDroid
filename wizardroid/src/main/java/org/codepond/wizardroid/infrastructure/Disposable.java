package org.codepond.wizardroid.infrastructure;

/**
 * Defines a method to release allocated resources.
 */
public interface Disposable {
    /**
     * Performs application-defined tasks associated with freeing, releasing, or resetting unmanaged resources.
     */
    void dispose();
}
