package org.codepond.android.wizardroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Base class for activities that want to implement step-by-step wizard functionality. 
 * When using this class you must override {@link WizardActivity#onSetup(WizardFlow)} to set up the wizard's flow
 * and optionally {@link WizardActivity#onWizardDone()} to handle wizard's finish event.
 */
public abstract class WizardActivity extends FragmentActivity implements WizardStep.OnStepStateChangedListener {
	private static final String TAG = "WizardActivity";
	private static final String STATE_WIZARD_LAST_STEP = WizardActivity.class.getName() + "#STATE_WIZARD_LAST_STEP";
    private static final String STATE_WIZARD_CONTEXT = "ContextVariable";
    private WizardFlow flow;
	private Wizard wizard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "Loading wizard data");
        onSetup(flow);
		if (flow == null) {
			throw new IllegalArgumentException("Error setting up the Wizard's flow. You must override WizardActivity#onSetup " + "" +
                    "and use WizardFlow.Builder to create the Wizard's flow followed by WizardActivity#super.onSetup(flow)");
		}
		wizard = new Wizard(flow);
		if (savedInstanceState != null) {
            wizard.setContext(savedInstanceState.getBundle(STATE_WIZARD_CONTEXT));
			wizard.setCurrentStep(savedInstanceState.getInt(STATE_WIZARD_LAST_STEP));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v(TAG, "Persisting current wizard step ID");
        outState.putInt(STATE_WIZARD_LAST_STEP, wizard.getCurrentStepPosition());
        outState.putBundle(STATE_WIZARD_CONTEXT, wizard.getContext());
	}

	//Handler for Back key pressed
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.v(TAG, "Going back one step");
			wizard.getCurrentStep().abort();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Fire when a step's state was changed.
	 */
	@Override
	public final void onStepStateChanged(WizardStep step) {
        Log.v(TAG, "Step state changed");
		switch (step.getState()) {
		case WizardStep.STATE_ABORTED:
			stepAborted();
			break;
		case WizardStep.STATE_COMPLETED:
			stepCompleted();
			break;
		case WizardStep.STATE_RUNNING:
		case WizardStep.STATE_PENDING:
		default:
			break;
		}
	}
	
	/**
	 * Set up the Wizard's flow. You must override this method and use {@link WizardFlow.Builder} to create the Wizard's flow.
	 * @param flow The wizard's flow which was created by calling {@link WizardFlow.Builder#create()}.
	 */
	public void onSetup(WizardFlow flow) {
		this.flow = flow;
	}

	/**
	 * Execute when wizard is complete.  
	 */
	public void onWizardDone() {
	}

 	private void stepAborted() {
        Log.v(TAG, "Step was aborted, going back one step");
		if (wizard.isFirstStep()) {
			finish();
		}
		else {
			wizard.back();
		}
	}
	
	private void stepCompleted() {
        Log.v(TAG, "Step completed, proceeding to the next step");
		if (!wizard.isLastStep()) {
			wizard.next();
		}
		else {
			onWizardDone();
		}
	}
}