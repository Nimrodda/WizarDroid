package org.codepond.android.wizardroid.persistence;

import android.os.Bundle;
import org.codepond.android.wizardroid.WizardStep;

/**
 * This interface defines the wizard context manager API
 * used to pass data between steps.
 */
public interface ContextManager {
    void loadStepContext(WizardStep step);
    void persistStepContext(WizardStep step);
    Bundle getContext();
    void setContext(Bundle context);
}
