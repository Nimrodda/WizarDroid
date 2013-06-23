package org.codepond.android.wizardroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

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
	
	private OnStepStateChangedListener onStepStateChangedListener;
	private int state = STATE_PENDING; //Default state for all steps

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            bindFields(args);
        }
    }

    private void bindFields(Bundle args) {
        //Scan the step for fields annotaed with @ContextVariable
        //and bind value if found in step's arguments
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(ContextVariable.class) != null && args.containsKey(field.getName())) {
                field.setAccessible(true);
                try {
                    field.set(this, args.get(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Mark the step as 'Done' and proceed to the next step in the flow.
     */
    public final void done() {
        setState(STATE_COMPLETED);
    }

    /**
     * Mark the step as 'Aborted' and go back to previous step or activity.
     */
    public final void abort() {
        setState(STATE_ABORTED);
    }

    void setOnStepChangedListener(OnStepStateChangedListener onStepStateChangedListener) {
		this.onStepStateChangedListener = onStepStateChangedListener;
	}
	
	int getState() {
		return state;
	}

	void setState(int state) {
		this.state = state;
		onStepStateChangedListener.onStepStateChanged(this);
	}
}
