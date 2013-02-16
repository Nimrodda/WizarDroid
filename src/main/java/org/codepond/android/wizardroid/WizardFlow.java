
package org.codepond.android.wizardroid;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentActivity;

public class WizardFlow {
	private final List<WizardStep> steps;
	private final int fragmentContainerId;
	private final FragmentActivity context;
	
	public WizardFlow(List<WizardStep> steps, int fragmentContainerId, 
			FragmentActivity activity) {
		this.steps = steps;
		this.fragmentContainerId = fragmentContainerId;
		this.context = activity;
	}

	public List<WizardStep> getSteps() {
		return steps;
	}

	public int getFragmentContainerId() {
		return fragmentContainerId;
	}

	public FragmentActivity getContext() {
		return context;
	}

	public static class Builder {

		private List<WizardStep> wizardSteps;
		private int containerId;
		private WizardActivity activity;
		private boolean valid;
		
		public Builder() {
			wizardSteps = new ArrayList<WizardStep>();
			valid = false;
			activity = null;
			containerId = -1;
		}
		
		public Builder setContainerId(int fragmentContainerId) {
			this.containerId = fragmentContainerId;
			valid = true;
			return this;
		}

		public Builder setActivity(WizardActivity activity) {
			this.activity = activity;
			valid = true;
			return this;
		}

		public Builder addStep(WizardStep step) {
			if (activity == null) {
				valid = false;
				throw new RuntimeException("You must call Wizard.Builder#setActivity before adding a step");
			}
			wizardSteps.add(step.setOnStepDoneListener(activity));
			valid = true;
			return this;
		}
		
		public <T> Builder addStep(WizardStep step, T model) {
			return addStep(step.saveModel(model));
		}
		
		public WizardFlow create() {
			if (isValid()) {
				return new WizardFlow(wizardSteps, containerId, activity);
			}
			else {
				throw new RuntimeException("Builder not ready. You must call Wizard.Builder#setActivity, Wizard.Builder#setContainerId " +
						"and add at least one step by calling Wizard.Builder#addStep before creating the Wizard");
			}
		}
		
		private boolean isValid() {
			return valid;
		}
	}
}