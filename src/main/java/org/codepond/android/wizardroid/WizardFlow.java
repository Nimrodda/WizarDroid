
package org.codepond.android.wizardroid;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

/**
 * WizardFlow holds information regarding the wizard's steps and flow.
 * Use {@link WizardFlow.Builder} to create an instance of WizardFlow.
 */
public class WizardFlow {
	private final List<WizardStep> steps;
	private final int fragmentContainerId;
	private final FragmentActivity context;
	
	private WizardFlow(List<WizardStep> steps, int fragmentContainerId, 
			FragmentActivity activity) {
		this.steps = steps;
		this.fragmentContainerId = fragmentContainerId;
		this.context = activity;
	}

	/**
	 * Get the steps from the wizard's flow.
	 * @return {@link WizardStep} List of WizardStep.
	 */
	public List<WizardStep> getSteps() {
		return steps;
	}

	/**
	 * Get the fragment container in which the wizard's step will be presented.
	 * @return integer the ID of the fragment container.
	 */
	public int getFragmentContainerId() {
		return fragmentContainerId;
	}

	/**
	 * Get the wizard activity context.
	 * @return FragmentActivity the context of the wizard.
	 */
	public FragmentActivity getContext() {
		return context;
	}

	/**
	 * Builder for {@link WizardFlow}. Use this class to build an instance of WizardFlow and 
	 * eventually call {@link WizardFlow.Builder#create()} to create the instance.
	 */
	public static class Builder {

		private List<WizardStep> wizardSteps;
		private int containerId;
		private WizardActivity activity;
		private boolean valid;
		
		/**
		 * Construct a WizardFlow.Builder
		 */
		public Builder() {
			wizardSteps = new ArrayList<WizardStep>();
			valid = false;
			activity = null;
			containerId = -1;
		}
		
		/**
		 * Set the fragment container ID
		 * @param fragmentContainerId the ID of the fragment container 
		 * @return Builder for chaining set methods
		 */
		public Builder setContainerId(int fragmentContainerId) {
			this.containerId = fragmentContainerId;
			valid = true && activity != null;
			return this;
		}

		/**
		 * Set the WizardActivity
		 * @param activity the WizardActivity which will run the wizard
		 * @return Builder for chaining set methods
		 */
		public Builder setActivity(WizardActivity activity) {
			this.activity = activity;
			valid = true && containerId != -1;
			return this;
		}

		/**
		 * Add a step to the WizardFlow. Note that the wizard flow is determined by the order of added steps.
		 * You must call {@link Wizard.Builder#setActivity} before adding a step to the flow.
		 * @param step instance of {@link WizardStep}
		 * @return Builder for chaining set methods
		 */
		public Builder addStep(WizardStep step) {
			if (activity == null) {
				valid = false;
				throw new RuntimeException("You must call WizardFlow.Builder#setActivity before adding a step");
			}
			wizardSteps.add(step.setOnStepDoneListener(activity));
			valid = true;
			return this;
		}
		
		/**
		 * Add a step with model to the WizardFlow. Note that the wizard flow is determined by the order of added steps.
		 * You must call {@link Wizard.Builder#setActivity} before adding a step to the flow.
		 * @param step instance of {@link WizardStep}
		 * @param model instance of {@link Parcelable} that will be associated to this step
		 * @return Builder for chaining set methods
		 */
		public Builder addStep(WizardStep step, Parcelable model) {
			return addStep(step.saveModel(model));
		}
		
		/**
		 * Create a new {@link WizardFlow} object.
		 * @return WizardFlow Instance of WizardFlow
		 */
		public WizardFlow create() {
			if (isValid()) {
				return new WizardFlow(wizardSteps, containerId, activity);
			}
			else {
				throw new RuntimeException("Builder not ready. You must call WizardFlow.Builder#setActivity, WizardFlow.Builder#setContainerId " +
						"and add at least one step by calling Wizard.Builder#addStep before creating the Wizard");
			}
		}
		
		private boolean isValid() {
			return valid;
		}
	}
}