package org.codepond.wizardroid.infrastructure.events;

import org.codepond.wizardroid.WizardStep;

/**
 * Otto event triggered when a wizard step is either set as completed or incomplete
 */
public class StepCompletedEvent {
    private boolean mIsStepCompleted;
	private WizardStep mWizardStep;
    public StepCompletedEvent(boolean isStepComplete, WizardStep step) {
        this.mIsStepCompleted = isStepComplete;
		this.mWizardStep = step;
    }

    /**
     * Returns true if the step was set complete, false if incomplete
     */
    public boolean isStepCompleted() {
        return mIsStepCompleted;
    }

	public WizardStep getStep() {
		return mWizardStep;
	}
}
