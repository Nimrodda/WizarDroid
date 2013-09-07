package org.codepond.android.wizardroid.sample.wizards;

import org.codepond.android.wizardroid.WizardFlow;
import org.codepond.android.wizardroid.sample.steps.TutorialStep1;
import org.codepond.android.wizardroid.sample.steps.TutorialStep2;

public class TutorialWizard extends BaseWizard {

    public TutorialWizard() {
        super();
    }

    //You must override this method and create a wizard flow by
    //using WizardFlow.Builder as shown in this example
    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                .addStep(TutorialStep1.class)           //Add your steps in the order you want them
                .addStep(TutorialStep2.class)           //to appear and eventually call create()
                .create();                              //to create the wizard flow.
    }
}