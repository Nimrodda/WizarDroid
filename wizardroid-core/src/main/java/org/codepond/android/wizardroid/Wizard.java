package org.codepond.android.wizardroid;

import android.support.v4.app.FragmentManager;
import org.codepond.android.wizardroid.persistence.ContextManager;

/**
 * The engine of the Wizard. This class is in charge of
 * the wizard's flow progression. It is used directly by
 * {@link WizardActivity} to manage the wizard.
 */
class Wizard {
	private static final String TAG = "Wizard";
	private int currentStep;
	private final FragmentManager fragmentManager;
	private final WizardFlow flow;
	private final int fragmentContainerId;
    private final ContextManager contextManager;


    /**
	 * Constructor for Wizard
	 * @param wizardFlow WizardFlow instance. See WizardFlow.Builder for more information on creating WizardFlow objects.
	 */
	Wizard(WizardFlow wizardFlow, ContextManager contextManager) {
		this.flow = wizardFlow;
        this.contextManager = contextManager;
		this.fragmentContainerId = wizardFlow.getFragmentContainerId();
		this.fragmentManager = flow.getFragmentManager();

		String currentStepTag = WizardFlow.getTagForWizardStep(currentStep, getCurrentStep().getClass());
		WizardStep step = (WizardStep) fragmentManager.findFragmentByTag(currentStepTag);
		if (step == null) {
			fragmentManager.beginTransaction().add(fragmentContainerId, getCurrentStep(), currentStepTag).commit();
			getCurrentStep().setState(WizardStep.STATE_RUNNING);
		}

	}

	/**
	 * Advance the wizard to the next step
	 */
	void next() {
        contextManager.persistStepContext(getCurrentStep());
		currentStep++;
        contextManager.loadStepContext(getCurrentStep());
		String currentStepTag = WizardFlow.getTagForWizardStep(currentStep, getCurrentStep().getClass());
		fragmentManager.beginTransaction().replace(fragmentContainerId, getCurrentStep(), currentStepTag)
				.addToBackStack(null).commit();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}

    /**
	 * Takes the wizard one step back
	 */
	void back() {
		getCurrentStep().setState(WizardStep.STATE_PENDING);
		currentStep--;
		fragmentManager.popBackStack();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}
	
	/**
	 * Sets the current step of the wizard
	 * @param stepId the position of the step within the WizardFlow
	 */
	void setCurrentStep(int stepId) {
		currentStep = stepId;
	}
	
	/**
	 * Gets the current step position
	 * @return integer representing the position of the step in the WizardFlow
	 */
	int getCurrentStepPosition() {
		return currentStep;
	}
	
	/**
	 * Gets the current step
	 * @return WizardStep the current WizardStep instance
	 */
	WizardStep getCurrentStep() {
		return flow.getSteps().get(currentStep);
	}
	
	/**
	 * Gets the step at specific position
	 * @param position the position of the step within the WizardFlow
	 * @return WizardStep the instance of WizardStep in the required position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	WizardStep getStepAtPosition(int position) throws ArrayIndexOutOfBoundsException {
		return flow.getSteps().get(position);
	}
	
	/**
	 * Checks if the current step is the last step in the Wizard
	 * @return boolean representing the result of the check
	 */
	boolean isLastStep() {
		return currentStep == flow.getSteps().size() - 1;
	}
	
	/**
	 * Checks if the step is the first step in the Wizard
	 * @return boolean representing the result of the check
	 */
	boolean isFirstStep() {
		return currentStep == 0;
	}
}
