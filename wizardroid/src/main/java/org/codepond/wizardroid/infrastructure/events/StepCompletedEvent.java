package org.codepond.wizardroid.infrastructure.events;

/**
 * Otto event triggered when a wizard step is either set as completed or incomplete
 */
public class StepCompletedEvent {
    private boolean isStepCompleted;
    public StepCompletedEvent(boolean isStepComplete) {
        this.isStepCompleted = isStepComplete;
    }

    /**
     * Returns true if the step was set complete, false if incomplete
     */
    public boolean isStepCompleted() {
        return isStepCompleted;
    }
}
