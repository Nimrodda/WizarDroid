package org.codepond.wizardroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.codepond.wizardroid.infrastructure.Bus;
import org.codepond.wizardroid.infrastructure.events.StepCompletedEvent;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Base class for a wizard's step. Extend this class to create a step and override {@link #onExit(int)}
 * to handle input and do tasks before the wizard changes the current step.
 * As with regular {@link Fragment} each inherited class must have an empty constructor.
 */
public abstract class WizardStep extends Fragment {
	private static final String TAG = WizardStep.class.getSimpleName();

    /**
     * Step exit code when wizard proceeds to the next step
     */
    public static final int EXIT_NEXT = 0;
    /**
     * Step exit code when wizard goes back one step
     */
    public static final int EXIT_PREVIOUS = 1;

    /**
     * Called when the wizard is about to go to the next step or
     * the previous step. Override this method to handle input from the step.
     * Possible exit codes are {@link #EXIT_NEXT} and {@link #EXIT_PREVIOUS}.
     * @param exitCode Code indicating whether the wizard is going to the next or previous step. The value would either be
     *                 WizardStep.EXIT_NEXT when wizard is about to go to the next step or
     *                 WizardStep.EXIT_PREVIOUS when wizard is about to go to the previous step.
     */
    public void onExit(int exitCode) {
    }


    /**
     * Notify the wizard that this step state had changed
     * @param isStepCompleted true if this step is completed, false if it's incomplete
	 * @deprecated Please use {@link #notifyCompleted()} and {@link #notifyIncomplete()} instead
     */
	@Deprecated
    public final void notifyCompleted(boolean isStepCompleted) {
        Bus.getInstance().post(new StepCompletedEvent(isStepCompleted, this));
    }

	/**
	 * Notify the wizard that this step is completed
	 */
	public final void notifyCompleted() {
		Bus.getInstance().post(new StepCompletedEvent(true, this));
	}

	/**
	 * Notify the wizard that this step is incomplete
	 */
	public final void notifyIncomplete() {
		Bus.getInstance().post(new StepCompletedEvent(false, this));
	}

    /**
     * IMPORTANT: This method is overridden to bind the wizard context to the step's fields.
     * Make sure to call super.onAttach(activity), if you override this method in your step class.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            bindFields(args);
        }
    }

    private void bindFields(Bundle args) {
        //Scan the step for fields annotated with @ContextVariable
        //and bind value if found in step's arguments
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(ContextVariable.class) != null && args.containsKey(field.getName())) {
                field.setAccessible(true);
                try {
                    if (field.getType() == Date.class) {
                        field.set(this, new Date(args.getLong(field.getName())));
                    }
                    else {
                        field.set(this, args.get(field.getName()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
