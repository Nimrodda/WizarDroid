package org.codepond.android.wizardroid.sample.steps;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.codepond.android.wizardroid.ContextVariable;
import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardStep;

public class FormStep1 extends WizardStep implements View.OnClickListener {

    //The wizard model. Note that it has to be public!
    //public FormWizardContext mForm;

    @ContextVariable
    private String firstname;
    @ContextVariable
    private String lastname;

    //You must have an empty constructor for every step
    public FormStep1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_form, container, false);
        //Set listener for 'Next' button click
        //Note that we are setting OnClickListener using getActivity() because
        //the 'Next' button is actually part of the hosting activity's layout and
        //not the step's layout
        Button nextButton = (Button) getActivity().findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        nextButton.setText("Next");

        return v;
    }

    @Override
    public void onClick(View view) {
        //Do some work
        //...
        EditText firstnameEt = (EditText) getActivity().findViewById(R.id.firstnameField);
        EditText lastnameEt = (EditText) getActivity().findViewById(R.id.lastnameField);

        //These model changes will persist to the next step.
        //Just make sure that you have the model defined there as well.
        firstname = firstnameEt.getText().toString();
        lastname = lastnameEt.getText().toString();

        //mForm.setFirstname(firstname.getText().toString());
        //mForm.setLastname(lastname.getText().toString());

        //And call done() to signal that the step is completed successfully
        done();
    }
}
