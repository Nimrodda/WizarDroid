
package org.codepond.android.wizardroid;

import android.support.v4.app.FragmentManager;
import android.util.Log;

public class Wizard {
	private static final String TAG = "Wizard";
	private int currentStep;
	private final FragmentManager fragmentManager;
	private final WizardFlow flow;
	private final int fragmentContainerId;
	
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
	public void next() {
		currentStep++;
		fragmentManager.beginTransaction().replace(fragmentContainerId, getCurrentStep()).addToBackStack(null).commit();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}
	
	public void back() {
		currentStep--;
		fragmentManager.popBackStack();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}
	
	public void setCurrentStep(int stepId) {
		currentStep = stepId;
	}
	
	public int getCurrentStepId() {
		return currentStep;
	}
	
	public WizardStep getCurrentStep() {
		return flow.getSteps().get(currentStep);
	}
	
	public WizardStep getStepAtId(int id) throws ArrayIndexOutOfBoundsException {
		return flow.getSteps().get(id);
	}
	
	public boolean isLastStep() {
		return currentStep == flow.getSteps().size() - 1;
	}
	
	public boolean isFirstStep() {
		Log.v(TAG, String.format("currentStep no.: %s", currentStep));
		return currentStep == 0;
	}
}
