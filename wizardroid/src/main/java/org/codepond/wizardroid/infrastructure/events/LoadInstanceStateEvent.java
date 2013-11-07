package org.codepond.wizardroid.infrastructure.events;

import android.os.Bundle;

/**
 * Otto Event which is triggered when the wizard is resuming
 */
public class LoadInstanceStateEvent {
    private Bundle state;

    public LoadInstanceStateEvent(Bundle state) {
        this.state = state;
    }

    /**
     * Get the wizard fragment's saved instance state
     */
    public Bundle getState() {
        return state;
    }
}
