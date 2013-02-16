package org.codepond.android.wizardroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

public class WizardActivity extends FragmentActivity implements WizardStep.OnStepDoneListener {
	private static final String TAG = "WizardActivity";
	private static final String WIZARD_LAST_STEP = WizardActivity.class.getName() + "#WIZARD_LAST_STEP";
	private Wizard wizard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wizard = new Wizard(onSetup());
		if (savedInstanceState != null) {
			wizard.setCurrentStep(savedInstanceState.getInt(WIZARD_LAST_STEP));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(WIZARD_LAST_STEP, wizard.getCurrentStepId());
	}
	
	//Handler for Back key pressed
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.v(TAG, String.format("Fragment backstack count: %s", getSupportFragmentManager().getBackStackEntryCount()));
			wizard.getCurrentStep().setState(WizardStep.STATE_ABORTED);
			stepAborted();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onStepDone(WizardStep step) {
		//do what you need...
		switch (step.getState()) {
		case WizardStep.STATE_ABORTED:
			stepAborted();
			break;
		case WizardStep.STATE_COMPLETED:
			stepCompleted();
			break;
		}
	}
	
	public WizardFlow onSetup() {
		throw new IllegalArgumentException("Error setting up Wizard. You must override WizardActivity#onSetup and use Wizard.Builder to setup the Wizard");
	}

	public void onWizardDone() {
		throw new UnsupportedOperationException("Must override onWizardDone");
	}

	private void stepAborted() {
		if (wizard.isFirstStep()) {
			finish();
		}
		else {
			wizard.back();
		}
	}
	
	private void stepCompleted() {
		if (!wizard.isLastStep()) {
			wizard.next();
		}
		else {
			onWizardDone();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "paused");
		wizard.isFirstStep();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "stopped");
		wizard.isFirstStep();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "destroyed");
		wizard.isFirstStep();
	}

	public WizardStep getCurrentStep() {
		return wizard.getCurrentStep();
	}
}