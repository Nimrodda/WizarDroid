package org.codepond.wizardroid.sample.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import org.codepond.wizardroid.persistence.ContextVariable;
import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.sample.R;

public class FormStep1 extends WizardStep {

    /**
     * Tell WizarDroid that these are context variables.
     * These values will be automatically bound to any field annotated with {@link ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */
    @ContextVariable
    private String firstname;
    @ContextVariable
    private String lastname;

    EditText firstnameEt;
    EditText lastnameEt;

    //You must have an empty constructor for every step
    public FormStep1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_form, container, false);
        //Get reference to the textboxes
        firstnameEt = (EditText) v.findViewById(R.id.firstnameField);
        lastnameEt = (EditText) v.findViewById(R.id.lastnameField);

        //and set default values by using Context Variables
        firstnameEt.setText(firstname);
        lastnameEt.setText(lastname);

        return v;
    }

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.
        firstname = firstnameEt.getText().toString();
        lastname = lastnameEt.getText().toString();
    }
}
