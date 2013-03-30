package org.codepond.android.wizardroid;

import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Class to control the wizard flow. Normally you will want to use WizardActivity 
 * instead of using this class directly for a more managed and simple usage.
 * Use this class to enhance your WizardActivity or to compose your
 * own WizardActivity. 
 */
public class Wizard {
	private static final String TAG = "Wizard";
	private int currentStep;
	private final FragmentManager fragmentManager;
	private final WizardFlow flow;
	private final int fragmentContainerId;
	
	/**
	 * Constructor for Wizard
	 * @param wizardFlow WizardFlow instance. See WizardFlow.Builder for more information on creating WizardFlow objects.
	 */
	public Wizard(WizardFlow wizardFlow) {
		this.flow = wizardFlow;
		this.fragmentContainerId = wizardFlow.getFragmentContainerId();
		this.fragmentManager = flow.getContext().getSupportFragmentManager();
		WizardStep step = (WizardStep) fragmentManager.findFragmentById(fragmentContainerId);
		if (step == null) {
			fragmentManager.beginTransaction().add(fragmentContainerId, getCurrentStep()).commit();
			getCurrentStep().setState(WizardStep.STATE_RUNNING);
		}
		
	}
	
	/**
	 * Advance the wizard to the next step
	 */
	public void next() {
		currentStep++;
		fragmentManager.beginTransaction().replace(fragmentContainerId, getCurrentStep()).addToBackStack(null).commit();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}
	
	/**
	 * Takes the wizard one step back
	 */
	public void back() {
		getCurrentStep().setState(WizardStep.STATE_PENDING);
		currentStep--;
		fragmentManager.popBackStack();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}
	
	/**
	 * Sets the current step of the wizard
	 * @param stepId the position of the step within the WizardFlow
	 */
	public void setCurrentStep(int stepId) {
		currentStep = stepId;
	}
	
	/**
	 * Gets the current step ID
	 * @return integer representing the position of the step in the WizardFlow
	 */
	public int getCurrentStepId() {
		return currentStep;
	}
	
	/**
	 * Gets the current step
	 * @return WizardStep the current WizardStep instance
	 */
	public WizardStep getCurrentStep() {
		return flow.getSteps().get(currentStep);
	}
	
	/**
	 * Gets the step at specific ID
	 * @param id the position of the step within the WizardFlow
	 * @return WizardStep the instance of WizardStep in the required position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public WizardStep getStepAtId(int id) throws ArrayIndexOutOfBoundsException {
		return flow.getSteps().get(id);
	}
	
	/**
	 * Checks if the current step is the last step in the Wizard
	 * @return boolean representing the result of the check
	 */
	public boolean isLastStep() {
		return currentStep == flow.getSteps().size() - 1;
	}
	
	/**
	 * Checks if the step is the first step in the Wizard
	 * @return boolean representing the result of the check
	 */
	public boolean isFirstStep() {
		return currentStep == 0;
	}
}
