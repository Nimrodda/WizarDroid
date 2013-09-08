package org.codepond.android.wizardroid.sample.wizards;

import org.codepond.android.wizardroid.WizardFlow;
import org.codepond.android.wizardroid.sample.steps.FormStep1;
import org.codepond.android.wizardroid.sample.steps.FormStep2;
import org.codepond.android.wizardroid.ui.BasicWizard;

/**
 * A sample to demonstrate a form in multiple steps.
 */
public class FormWizard extends BasicWizard {

    public FormWizard() {
        super();
    }

    //You must override this method and create a wizard flow by
    //using WizardFlow.Builder as shown in this example
    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                .addStep(FormStep1.class)                       //Add your steps in the order you want them
                .addStep(FormStep2.class)                       //to appear and eventually call create()
                .create();                                      //to create the wizard flow.
    }
}
