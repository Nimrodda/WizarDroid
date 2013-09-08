package org.codepond.android.wizardroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import org.codepond.android.wizardroid.persistence.ContextManager;

/**
 * The engine of the Wizard. This class controls the flow of the wizard
 * and is using {@link ViewPager} under the hood. You would normally want to
 * extend {@link WizardFragment} instead of using this class directly. Use this
 * class only if you wish to create a custom WizardFragment to control the wizard.
 */
public class Wizard {

    /**
     * Interface for key wizard events. Implement this interface if you wish to create
     * a custom WizardFragment.
     */
    public static interface WizardCallbacks {
        /**
         * Event called when the wizard is completed
         */
        public void onWizardComplete();

        /**
         * Event called after a step was changed
         */
        public void onStepChanged();
    }

    private static final String TAG = Wizard.class.getSimpleName();
	private final WizardFlow wizardFlow;
    private final ContextManager contextManager;
    private final WizardCallbacks callbacks;
    private final ViewPager mPager;

    private WizardStep[] wizardStepInstances;
    private boolean fingerSlide;


    /**
     * Constructor for Wizard
     * @param wizardFlow WizardFlow instance. See WizardFlow.Builder for more information on creating WizardFlow objects.
     * @param contextManager ContextManager instance would normally be {@link org.codepond.android.wizardroid.persistence.ContextManagerImpl}
     * @param callbacks implementation of WizardCallbacks
     * @param activity the hosting activity
     */
	public Wizard(final WizardFlow wizardFlow,
                  final ContextManager contextManager,
                  final WizardCallbacks callbacks,
                  final FragmentActivity activity) {
		this.wizardFlow = wizardFlow;
        this.contextManager = contextManager;
        this.callbacks = callbacks;
        this.mPager = (ViewPager) activity.findViewById(R.id.step_container);
        if (mPager == null) {
            throw new RuntimeException("Cannot initialize Wizard. View with ID: step_container not found!" +
                    " The hosting Activity/Fragment must have a ViewPager in its layout with ID: step_container");
        }
        mPager.setAdapter(new WizardPagerAdapter(activity.getSupportFragmentManager()));

        //Implementation of OnPageChangeListener to handle wizard control via user finger slides
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean initialOffsetIsSet;
            private float initialOffset;
            private boolean consumedPageSelectedEvent;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Check if the page started to be dragged by the user
                //and avoid positionOffset 0
                if (!initialOffsetIsSet && positionOffset > 0) {
                    //Store the initialOffset for later comparison
                    initialOffset = positionOffset;
                    //Signal that initial offset has been set for the current page drag sequence
                    initialOffsetIsSet = true;
                }
                //Check slide direction and decide if to call goNext() or goBack()
                //Once page is "selected" (visible to the user)), skip checking slide direction
                if (!consumedPageSelectedEvent && positionOffset > 0) {
                    if (positionOffset > initialOffset) {
                        //Sliding right
                        goNext();
                        fingerSlide = true;
                    }
                    else if (positionOffset < initialOffset){
                        //Sliding left
                        goBack();
                        fingerSlide = true;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                //Signal that the page is now "selected"
                consumedPageSelectedEvent = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //No animation is on going, reset flags
                    consumedPageSelectedEvent = false;
                    initialOffsetIsSet = false;
                    fingerSlide = false;
                }

            }
        });
	}

	/**
	 * Advance the wizard to the next step
	 */
	public void goNext() {
        Log.v(TAG, "goNext() executed");
        getCurrentStep().onExit(WizardStep.EXIT_NEXT);
        contextManager.persistStepContext(getCurrentStep());
        //Tell the ViewPager to re-create the fragments, causing it to bind step context
        mPager.getAdapter().notifyDataSetChanged();

        if (isLastStep()) {
            callbacks.onWizardComplete();
        }
        else {
            //Check if the user dragged the page or pressed a button.
            //If the page was dragged then the ViewPager will handle the current step.
            //Otherwise, set the current step programmatically.
            if (!fingerSlide) {
                setCurrentStep(mPager.getCurrentItem() + 1);
            }
        }
        //Notify the hosting Fragment/Activity that the step has changed so it might want to update the controls accordingly
        callbacks.onStepChanged();
	}

    /**
	 * Takes the wizard one step back
	 */
	public void goBack() {
        Log.v(TAG, "goBack() executed");
        getCurrentStep().onExit(WizardStep.EXIT_PREVIOUS);
        if (isFirstStep()) {
            throw new RuntimeException("Attempt to go back one step failed. Current step is the first step in the wizard.");
        }
        else {
            //Check if the user dragged the page or pressed a button.
            //If the page was dragged then the ViewPager will handle the current step.
            //Otherwise, set the current step programmatically.
            if (!fingerSlide) {
                setCurrentStep(mPager.getCurrentItem() - 1);
            }
        }

        //Notify the hosting Fragment/Activity that the step has changed so it might want to update the controls accordingly
        callbacks.onStepChanged();
	}
	
	/**
	 * Sets the current step of the wizard
	 * @param stepPosition the position of the step within the WizardFlow
	 */
	public void setCurrentStep(int stepPosition) {
        mPager.setCurrentItem(stepPosition);
	}
	
	/**
	 * Gets the current step position
	 * @return integer representing the position of the step in the WizardFlow
	 */
    public int getCurrentStepPosition() {
		return mPager.getCurrentItem();
	}
	
	/**
	 * Gets the current step
	 * @return WizardStep the current WizardStep instance
	 */
    public WizardStep getCurrentStep() {
        return wizardStepInstances[mPager.getCurrentItem()];
	}
	
	/**
	 * Checks if the current step is the last step in the Wizard
	 * @return boolean representing the result of the check
	 */
    public boolean isLastStep() {
		return mPager.getCurrentItem() == wizardFlow.getSteps().size() - 1;
	}
	
	/**
	 * Checks if the step is the first step in the Wizard
	 * @return boolean representing the result of the check
	 */
	public boolean isFirstStep() {
		return mPager.getCurrentItem() == 0;
	}

    public class WizardPagerAdapter extends FragmentStatePagerAdapter {

        private Fragment mPrimaryItem;

        public WizardPagerAdapter(FragmentManager fm) {
            super(fm);
            wizardStepInstances = new WizardStep[getCount()];
        }

        @Override
        public Fragment getItem(int i) {
            try {
                WizardStep step = wizardFlow.getSteps().get(i).newInstance();
                wizardStepInstances[i] = step;
                contextManager.loadStepContext(step);
                Log.v(TAG, "context loaded for " + step.toString());
                return step;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object.equals(mPrimaryItem)) {
                return POSITION_UNCHANGED;
            }
            else {
                return POSITION_NONE;
            }
        }

        @Override
        public int getCount() {
            return wizardFlow.getSteps().size();
        }
    }
}