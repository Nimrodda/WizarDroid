package org.codepond.wizardroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.codepond.wizardroid.layouts.CustomViewPager;
import android.util.Log;
import android.view.ViewGroup;

import org.codepond.wizardroid.infrastructure.Bus;
import org.codepond.wizardroid.infrastructure.Subscriber;
import org.codepond.wizardroid.infrastructure.events.StepCompletedEvent;
import org.codepond.wizardroid.persistence.ContextManager;

import java.io.Closeable;

/**
 * The engine of the Wizard. This class controls the flow of the wizard
 * and is using {@link ViewPager} under the hood. You would normally want to
 * extend {@link WizardFragment} instead of using this class directly and make calls to the wizard API
 * via {@link org.codepond.wizardroid.WizardFragment#wizard} field. Use this
 * class only if you wish to create a custom WizardFragment to control the wizard.
 */
public class Wizard implements Closeable, Subscriber {
	/**
     * Interface for key wizard events. Implement this interface if you wish to create
     * a custom WizardFragment.
     */
    public interface WizardCallbacks {
        /**
         * Event called when the wizard is completed
         */
        void onWizardComplete();

        /**
         * Event called after a step was changed
         */
        void onStepChanged();
    }

	private static final boolean DEBUG = false;
    private static final String TAG = Wizard.class.getSimpleName();
	private WizardFlow mWizardFlow;
    private ContextManager mContextManager;
    private WizardCallbacks mCallbacks;
    private CustomViewPager mPager;
    private FragmentManager mFragmentManager;
    private int backStackEntryCount;
	private WizardStep mPreviousStep;
	private int mPreviousPosition;

	/**
     * @deprecated Please use {@link #Wizard(WizardFlow, ContextManager, WizardCallbacks, FragmentManager)} instead.
     * Constructor for Wizard
     * @param wizardFlow WizardFlow instance. See WizardFlow.Builder for more information on creating WizardFlow objects.
     * @param contextManager ContextManager instance would normally be {@link org.codepond.wizardroid.persistence.ContextManagerImpl}
     * @param callbacks implementation of WizardCallbacks
     * @param activity the hosting activity
     */
	@Deprecated
    public Wizard(final WizardFlow wizardFlow,
                  final ContextManager contextManager,
                  final WizardCallbacks callbacks,
                  final FragmentActivity activity) {
		this.mWizardFlow = wizardFlow;
        this.mContextManager = contextManager;
        this.mCallbacks = callbacks;
        this.mPager = (CustomViewPager) activity.findViewById(R.id.step_container);
        if (mPager == null) {
            throw new RuntimeException("Cannot initialize Wizard. View with ID: step_container not found!" +
                    " The hosting Activity/Fragment must have a ViewPager in its layout with ID: step_container");
        }

        this.mFragmentManager = activity.getSupportFragmentManager();

        init();
        setViewPager(mPager);
	}

    /**
     * Constructor for Wizard
     * @param wizardFlow WizardFlow instance. See WizardFlow.Builder for more information on creating WizardFlow objects.
     * @param contextManager ContextManager instance would normally be {@link org.codepond.wizardroid.persistence.ContextManagerImpl}
     * @param callbacks implementation of WizardCallbacks
     * @param fragmentManager instance of {@link FragmentManager}
     */
    public Wizard(WizardFlow wizardFlow,
                  ContextManager contextManager,
                  WizardCallbacks callbacks,
                  FragmentManager fragmentManager) {
        this.mWizardFlow = wizardFlow;
        this.mContextManager = contextManager;
        this.mCallbacks = callbacks;
        this.mFragmentManager = fragmentManager;

        init();
    }

    private void init() {
        backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        mFragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                backStackEntryCount = mFragmentManager.getBackStackEntryCount();

                //onBackPressed
                if (backStackEntryCount < getCurrentStepPosition()){
                    mPager.setCurrentItem(getCurrentStepPosition() - 1);
                }
            }
        });
        Bus.getInstance().register(this, StepCompletedEvent.class);
    }

    /**
     * Set the ViewPager in which the Wizard will load the steps into
     * @param viewPager instance of ViewPager
     */
    public void setViewPager(CustomViewPager viewPager) {
        mPager = viewPager;
        mPager.setAdapter(new WizardPagerAdapter(mFragmentManager));

        //Implementation of OnPageChangeListener to handle wizard control via user finger slides
        mPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
			private int mPreviousState = CustomViewPager.SCROLL_STATE_IDLE;

			@Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (backStackEntryCount < position){
                    mFragmentManager.beginTransaction().addToBackStack(null).commit();
                }
                else if (backStackEntryCount > position){
                    mFragmentManager.popBackStack();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
				if (DEBUG) Log.v(TAG, "onPageScrollStateChanged " + state);
				switch (state) {
					case CustomViewPager.SCROLL_STATE_DRAGGING:
						mPreviousPosition = getCurrentStepPosition();
						mPreviousStep = getCurrentStep();
						break;
					case CustomViewPager.SCROLL_STATE_SETTLING:
						mCallbacks.onStepChanged();
						break;
					case CustomViewPager.SCROLL_STATE_IDLE:
						if (mPreviousState == CustomViewPager.SCROLL_STATE_SETTLING) {
							if (getCurrentStepPosition() > mPreviousPosition) {
								if (DEBUG) Log.v(TAG, "goNext");
								processStepBeforeChange(mPreviousStep, mPreviousPosition);
								mPager.getAdapter().notifyDataSetChanged();
							}
							else {
								if (DEBUG) Log.v(TAG, "goBack");
								mPreviousStep.onExit(WizardStep.EXIT_PREVIOUS);
							}
						}
						break;
				}
				mPreviousState = state;
            }
        });
    }

    @Override
    public void close() {
        Bus.getInstance().unregister(this);
        mPager = null;
        mWizardFlow = null;
        mFragmentManager = null;
        mContextManager = null;
        mCallbacks = null;
    }

    @Override
    public void receive(Object event) {
        StepCompletedEvent stepCompletedEvent = (StepCompletedEvent) event;
        onStepCompleted(stepCompletedEvent.isStepCompleted(), stepCompletedEvent.getStep());
    }

    private void onStepCompleted(boolean isComplete, WizardStep step) {
        if (step != getCurrentStep()) return;
		int stepPosition = getCurrentStepPosition();


        // Check that the step is not already in this state to avoid spamming the viewpager
        if (mWizardFlow.isStepCompleted(stepPosition) != isComplete) {
            mWizardFlow.setStepCompleted(stepPosition, isComplete);
            mPager.getAdapter().notifyDataSetChanged();
            //Refresh the UI
            mCallbacks.onStepChanged();
        }
    }

	private void processStepBeforeChange(WizardStep step, int position) {
		step.onExit(WizardStep.EXIT_NEXT);
		mWizardFlow.setStepCompleted(position, true);
		mContextManager.persistStepContext(step);
	}

    /**
	 * Advance the wizard to the next step
	 */
	public void goNext() {
		if (canGoNext()) {
			if (isLastStep()) {
				processStepBeforeChange(getCurrentStep(), getCurrentStepPosition());
				mCallbacks.onWizardComplete();
			}
			else {
				mPreviousPosition = getCurrentStepPosition();
				mPreviousStep = getCurrentStep();
				setCurrentStep(mPager.getCurrentItem() + 1);
			}
		}
    }

    /**
	 * Takes the wizard one step back
	 */
	public void goBack() {
		if (!isFirstStep()) {
			mPreviousPosition = getCurrentStepPosition();
			mPreviousStep = getCurrentStep();
			setCurrentStep(mPager.getCurrentItem() - 1);
		}
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
        return ((WizardPagerAdapter)mPager.getAdapter()).getPrimaryItem();
	}
	
	/**
	 * Checks if the current step is the last step in the Wizard
	 * @return boolean representing the result of the check
	 */
    public boolean isLastStep() {
		return mPager.getCurrentItem() == mWizardFlow.getStepsCount() - 1;
	}
	
	/**
	 * Checks if the step is the first step in the Wizard
	 * @return boolean representing the result of the check
	 */
	public boolean isFirstStep() {
		return mPager.getCurrentItem() == 0;
	}

    /**
     * Check if the wizard can proceed to the next step by verifying that the current step
     * is completed
     */
    public boolean canGoNext() {
        int stepPosition = getCurrentStepPosition();
        if (mWizardFlow.isStepRequired(stepPosition)) {
            return mWizardFlow.isStepCompleted(stepPosition);
        }
        return true;
    }

    /**
     * Custom adapter for the ViewPager
     */
    public class WizardPagerAdapter extends FragmentStatePagerAdapter {

        private Fragment mPrimaryItem;

        public WizardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            try {
                WizardStep step = mWizardFlow.getSteps().get(i).newInstance();
                mContextManager.loadStepContext(step);
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
            if (object.equals(mPreviousStep)) {
                return POSITION_UNCHANGED;
            }
            else {
                return POSITION_NONE;
            }
        }

        @Override
        public int getCount() {
            return mWizardFlow.getSteps().size();
        }

        public WizardStep getPrimaryItem() {
            return (WizardStep) mPrimaryItem;
        }
    }
}

