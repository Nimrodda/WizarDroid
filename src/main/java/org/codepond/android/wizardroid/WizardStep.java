package org.codepond.android.wizardroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class WizardStep extends Fragment {
	private static final String TAG = "WizardStep";

	static interface OnStepDoneListener {
		void onStepDone(WizardStep step); 
	}

	static final int STATE_PENDING 	= 0;
	static final int STATE_RUNNING 	= 1;
	static final int STATE_COMPLETED = 2;
	static final int STATE_ABORTED 	= 3;
	
	private final String key = String.format("%s#WizardStepModel", getClass().getName());
	private OnStepDoneListener onStepDoneListener;
	private int state = STATE_PENDING;
	
	WizardStep setOnStepDoneListener(OnStepDoneListener onStepDoneListener) {
		this.onStepDoneListener = onStepDoneListener;
		return this;
	}
	
	int getState() {
		return state;
	}

	void setState(int state) {
		this.state = state;
	}

	<T> WizardStep saveModel(T value) {
		Bundle args = new Bundle();
		if (value instanceof String) {
			args.putString(key, (String) value);
		}
		else if (value instanceof Parcelable){
			args.putParcelable(key, (Parcelable) value);
		}
		else {
			throw new IllegalArgumentException(String.format("Unable to set argument. The type %s is not supported.", value.getClass().getName()));
		}
		setArguments(args);
		return this;
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	public void done() {
		state = STATE_COMPLETED;
		onStepDoneListener.onStepDone(this);
	}
	
	public void abort() {
		state = STATE_ABORTED;
		onStepDoneListener.onStepDone(this);
	}
	
	public void onModelBound(Parcelable model) {
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.v(TAG, String.format("Running step: %s", getStepDescription()));
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

	public String getStepDescription() {
		throw new UnsupportedOperationException("You must override WizardStepFragment#getStepDescription()"); 
	}
}
