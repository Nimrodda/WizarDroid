package org.codepond.wizardroid.sample.wizards;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;
import org.codepond.wizardroid.sample.steps.TutorialStep1;
import org.codepond.wizardroid.sample.steps.TutorialStep2;

public class TutorialWizard extends BasicWizardLayout {

    /**
     * Note that we inherit from {@link android.support.v4.app.Fragment} and therefore must have an empty constructor
     */
    public TutorialWizard() {
        super();
    }

    /*
        You must override this method and create a wizard flow by
        using WizardFlow.Builder as shown in this example
     */
    @Override
    public WizardFlow onSetup() {
        return new WizardFlow.Builder()
                .addStep(TutorialStep1.class)           //Add your steps in the order you want them
                .addStep(TutorialStep2.class)           //to appear and eventually call create()
                .create();                              //to create the wizard flow.
    }

    /*
        You'd normally override onWizardComplete to access the wizard context and/or close the wizard
     */
    @Override
    public void onWizardComplete() {
        super.onWizardComplete();   //Make sure to first call the super method before anything else
        getActivity().finish();     //Terminate the wizard
    }
}