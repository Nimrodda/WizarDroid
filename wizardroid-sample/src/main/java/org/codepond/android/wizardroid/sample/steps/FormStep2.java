package org.codepond.android.wizardroid.sample.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.codepond.android.wizardroid.persistence.ContextVariable;
import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardStep;

public class FormStep2 extends WizardStep {

    //NOTE: field names MUST be identical to previous step's fields names
    @ContextVariable
    private String firstname;
    @ContextVariable
    private String lastname;


    //You must have an empty constructor for every step
    public FormStep2() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_summary, container, false);
        TextView firstnameTv = (TextView) v.findViewById(R.id.firstname);
        TextView lastnameTv = (TextView) v.findViewById(R.id.lastname);

        //WizarDroid will automatically inject the values for these fields
        //so we can simply set the text views
        firstnameTv.setText(firstname);
        lastnameTv.setText(lastname);

        return v;
    }
}
