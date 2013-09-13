package org.codepond.wizardroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Base class for a wizard's step.
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
     * @param exitCode Code indicating whether the wizard is going to the next or previous step. The value would either be
     *                 WizardStep.EXIT_NEXT when wizard is about to go to the next step or
     *                 WizardStep.EXIT_PREVIOUS when wizard is about to go to the previous step.
     */
    public void onExit(int exitCode) {
    }

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
