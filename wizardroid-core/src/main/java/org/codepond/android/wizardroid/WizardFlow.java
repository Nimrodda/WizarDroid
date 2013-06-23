package org.codepond.android.wizardroid;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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

	public static String getTagForWizardStep(int stepPosition, Class<? extends WizardStep> stepClass) {
		return stepClass.getName() + "#" + stepPosition;
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
            //Valid if the activity was already set.
			valid = activity != null;
			return this;
		}

		/**
		 * Set the WizardActivity
		 * @param activity the WizardActivity which will run the wizard
		 * @return Builder for chaining set methods
		 */
		public Builder setActivity(WizardActivity activity) {
			this.activity = activity;
			//Valid if the container was already set
            valid = containerId != -1;
			return this;
		}

		/**
		 * Add a step to the WizardFlow. Note that the wizard flow is determined by the order of added steps. You must
		 * call {@link WizardFlow.Builder#setActivity} before adding a step to the flow.
		 * 
		 * @param stepClass
		 *            The class of {@link WizardStep} to create (if necessary)
		 * @return Builder for chaining set methods
		 */
		public Builder addStep(Class<? extends WizardStep> stepClass) {
			return addStep(stepClass, null);
		}

		/**
		 * Add a step to the WizardFlow. Note that the wizard flow is determined by the order of added steps. You must
		 * call {@link WizardFlow.Builder#setActivity} before adding a step to the flow.
		 * 
		 * @param stepClass
		 *            The class of {@link WizardStep} to create (if necessary)
         * @param arguments Initial arguments for this step. The arguments are then reachable by calling {@link WizardStep#getArguments()} from within the step itself.
		 * @return Builder for chaining set methods
		 */
		public Builder addStep(Class<? extends WizardStep> stepClass, Bundle arguments) {
			if (activity == null) {
				throw new RuntimeException("You must call WizardFlow.Builder#setActivity before adding a step");
			}

			// Check if fragment is present, if not, create it.
			String tag = WizardFlow.getTagForWizardStep(wizardSteps.size(), stepClass);
			WizardStep step = (WizardStep) activity.getSupportFragmentManager().findFragmentByTag(tag);

			if (step == null) {
				try {
					step = stepClass.newInstance();
					if (arguments != null) step.setArguments(arguments);
				} catch (InstantiationException e) {
					valid = false;
					throw new RuntimeException(String.format("Failed to add step: %s to the WizardFlow while attempting to instantiate the step", stepClass.getName()));
				} catch (IllegalAccessException e) {
					valid = false;
					throw new RuntimeException(String.format("Failed to add step: %s to the WizardFlow while attempting to instantiate the step", stepClass.getName()));
				}
			}
            step.setOnStepChangedListener(activity);
			wizardSteps.add(step);
			valid = true;
			return this;
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