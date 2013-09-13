package org.codepond.android.wizardroid;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.Date;

/**
 * Dummy test step
 */
public class TestStep extends WizardStep {

    @ContextVariable
    private Date timestamp;

    public  TestStep() {

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
