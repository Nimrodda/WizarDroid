package org.codepond.android.wizardroid.sample.wizards;

import org.codepond.android.wizardroid.WizardFlow;
import org.codepond.android.wizardroid.layouts.BasicWizardLayout;
import org.codepond.android.wizardroid.persistence.ContextVariable;
import org.codepond.android.wizardroid.sample.steps.FormStep1;
import org.codepond.android.wizardroid.sample.steps.FormStep2;

/**
 * A sample to demonstrate a form in multiple steps.
 */
public class FormWizard extends BasicWizardLayout {

    /**
     * Tell WizarDroid that these are context variables and set default values.
     * These values will be automatically bound to any field annotated with {@link ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */
    @ContextVariable
    private String firstname = "WizarDroid";
    @ContextVariable
    private String lastname = "CondPond.org";

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
