package org.codepond.android.wizardroid.sample.wizards;

import android.os.Bundle;
import org.codepond.android.wizardroid.sample.steps.TutorialStep1;
import org.codepond.android.wizardroid.sample.steps.TutorialStep2;
import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardActivity;
import org.codepond.android.wizardroid.WizardFlow;

public class TutorialWizard extends WizardActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);
    }

    //You must override this method and create a wizard flow by
    //using WizardFlow.Builder as shown in this example
    @Override
    public void onSetup(WizardFlow flow) {
        flow = new WizardFlow.Builder()
                .setActivity(this)                      //First, set the hosting activity for the wizard
                .setContainerId(R.id.step_container)    //then set the layout container for the steps.
                .addStep(TutorialStep1.class)           //Add your steps in the order you want them
                .addStep(TutorialStep2.class)           //to appear and eventually call create()
                .create();                              //to create the wizard flow.

        //Call the super method using the newly created flow
        super.onSetup(flow);
    }

    //Overriding this method is optional
    @Override
    public void onWizardDone() {
        //Do whatever you want to do once the Wizard is complete
        //in this case I just close the activity, which causes Android
        //to go back to the previous activity.
        finish();
    }
}