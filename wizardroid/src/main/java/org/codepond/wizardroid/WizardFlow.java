package org.codepond.wizardroid;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * WizardFlow holds information regarding the wizard's steps and flow.
 * Use {@link WizardFlow.Builder} to create an instance of WizardFlow.
 */
public class WizardFlow {
    /**
     * This class wraps WizardStep to provide additional meta data which is persisted separately
     * as part of the wizard flow.
     */
    public static class StepMetaData {
        private boolean mCompleted;
        private boolean mRequired;

        private Class<? extends WizardStep> stepClass;

        private StepMetaData(boolean isRequired, Class<? extends WizardStep> stepClass) {
            this.mRequired = isRequired;
            this.stepClass = stepClass;
        }

        public boolean isRequired() {
            return mRequired;
        }

        public boolean isCompleted() {
            return mCompleted;
        }

        public void setCompleted(boolean completed) {
            this.mCompleted = completed;
        }

        public Class<? extends WizardStep> getStepClass() {
            return stepClass;
        }

    }

    private final List<StepMetaData> mSteps;

	private WizardFlow(List<StepMetaData> steps) {
		this.mSteps = steps;
	}

    /**
	 * Get the list of wizard flow steps which is cut off at the last step which is required and incomplete
     * and the first step which doesn't allow to go back and is incomplete.
     * This method is designed to work directly with ViewPager.
	 */
	public List<Class<? extends WizardStep>> getSteps() {
        List<Class<? extends WizardStep>> cutOffFlow = new ArrayList<Class<? extends WizardStep>>();

        //Calculate the cut off step by finding the last step which is required and incomplete
        for (StepMetaData stepMetaData : this.mSteps) {
            cutOffFlow.add(stepMetaData.getStepClass());
            if (!stepMetaData.isCompleted() && stepMetaData.isRequired()) break;
        }
        return cutOffFlow;
	}

    /**
     * Check if the specified step is required
     * @param stepPosition the position of the step to be checked
     */
    public boolean isStepRequired(int stepPosition) {
        StepMetaData meta = mSteps.get(stepPosition);
        return meta.isRequired();
    }

    /**
     * Check if the specified step is completed or incomplete
     * @param stepPosition the position of the step to be checked
     */
    public boolean isStepCompleted(int stepPosition) {
        StepMetaData meta = mSteps.get(stepPosition);
        return meta.isCompleted();
    }

    /**
     * Get the total amount of steps in the flow
     */
    public int getStepsCount() {
        return this.mSteps.size();
    }

    /**
     * Set a step completed or incomplete
     * @param stepPosition the position of the step to be set
     * @param stepCompleted true for complete, false for incomplete
     */
    public void setStepCompleted(int stepPosition, boolean stepCompleted) {
        mSteps.get(stepPosition).setCompleted(stepCompleted);
    }

    final void persistFlow(Bundle state) {
        for (StepMetaData stepMetaData : mSteps) {
            state.putBoolean(stepMetaData.getStepClass().getSimpleName() + mSteps.indexOf(stepMetaData), stepMetaData.isCompleted());
        }
    }

    final void loadFlow(Bundle state) {
        for (StepMetaData stepMetaData : mSteps) {
            stepMetaData.setCompleted(
                    state.getBoolean(stepMetaData.getStepClass().getSimpleName() + mSteps.indexOf(stepMetaData),
                            stepMetaData.isCompleted()));
        }
    }
	/**
	 * Builder for {@link WizardFlow}. Use this class to build an instance of WizardFlow.
     * You need to use this class in your wizard's {@link WizardFragment#onSetup()} to return an instance of WizardFlow.
     * Call {@link #addStep(Class)} to add steps to the flow, keeping in mind that the order you the steps
     * will be the order the wizard will display them. Eventually call {@link WizardFlow.Builder#create()} to create the instance.
	 */
	public static class Builder {

        private List<StepMetaData> mWizardSteps;

        /**
		 * Construct a WizardFlow.Builder
		 */
		public Builder() {
			mWizardSteps = new ArrayList<StepMetaData>();
		}
		
		/**
		 * Add a step to the WizardFlow. Note that the wizard flow is determined by the order of added steps.
		 * @param stepClass
		 *            The class of {@link WizardStep} to create (if necessary)
		 * @return Builder for creating a wizard flow
		 */
		public Builder addStep(Class<? extends WizardStep> stepClass) {
			return addStep(stepClass, false);
		}

        /**
         * Add a step to the WizardFlow. Note that the wizard flow is determined by the order of added steps.
         * @param stepClass
         *            The class of {@link WizardStep} to create (if necessary)
         * @param isRequired
         *            Determine if the step is required before advancing to the next step
         * @return Builder for creating a wizard flow
         */
        public Builder addStep(Class<? extends WizardStep> stepClass, boolean isRequired) {
            mWizardSteps.add(new StepMetaData(isRequired, stepClass));
            return this;
        }

		/**
		 * Create a new {@link WizardFlow} object.
		 * @return WizardFlow Instance of WizardFlow
		 */
		public WizardFlow create() {
			if (mWizardSteps.size() > 0) {
				return new WizardFlow(mWizardSteps);
			}
			else {
				throw new RuntimeException("Cannot create WizardFlow. No step has been added! Call Builder#addStep(stepClass) to add steps to the wizard flow.");
			}
		}
	}
}