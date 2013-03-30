package org.codepond.android.wizardroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Base class for a wizard's step.
 * As with regular {@link Fragment} each inherited class must have an empty constructor.
 */
public abstract class WizardStep extends Fragment {
	private static final String TAG = "WizardStep";

	static interface OnStepStateChangedListener {
		void onStepStateChanged(WizardStep step); 
	}

	static final int STATE_PENDING 	= 0;
	static final int STATE_RUNNING 	= 1;
	static final int STATE_COMPLETED = 2;
	static final int STATE_ABORTED 	= 3;
	
	private final String key = String.format("%s#WizardStepModel", getClass().getName());
	private OnStepStateChangedListener onStepStateChangedListener;
	private int state = STATE_PENDING;
	
	WizardStep setOnStepDoneListener(OnStepStateChangedListener onStepStateChangedListener) {
		this.onStepStateChangedListener = onStepStateChangedListener;
		return this;
	}
	
	int getState() {
		return state;
	}

	void setState(int state) {
		this.state = state;
		onStepStateChangedListener.onStepStateChanged(this);
	}

	WizardStep saveModel(Parcelable value) {
		Bundle args = new Bundle();
		args.putParcelable(key, (Parcelable) value);
		setArguments(args);
		return this;
	}
	
	/**
	 * Mark the step as 'Done' and proceed to the next step in the flow.
	 */
	public void done() {
		setState(STATE_COMPLETED);
	}
	
	/**
	 * Mark the step as 'Aborted' and go back to previous step or activity.
	 */
	public void abort() {
		setState(STATE_ABORTED);
	}
	
	/**
	 * Event which is executed after the step's model had been loaded once it's initialized.
	 * Use this event to wire your step's model. 
	 * @param model the Parcelable model which was loaded. 
	 */
	public void onModelBound(Parcelable model) {
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		bindModel();
	}

	private void bindModel() {
		Parcelable model;
		try {
			model = getArguments().getParcelable(key);
		}
		catch (NullPointerException e) {
			model = null;
		}
		onModelBound(model);
	}
}
