package org.codepond.wizardroid.infrastructure.events;

import android.os.Bundle;

/**
 * Otto event triggered when the wizard is pausing
 */
public class SaveInstanceStateEvent {
    private Bundle state;

    public SaveInstanceStateEvent(Bundle state) {
        this.state = state;
    }

    /**
     * Get the wizard fragment's onSaveInstanceState
     */
    public Bundle getState() {
        return state;
    }
}
