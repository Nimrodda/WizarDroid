package org.codepond.wizardroid.sample.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;
import org.codepond.wizardroid.sample.R;

public class FormStep3 extends WizardStep {

    /**
     * Tell WizarDroid that these are context variables.
     * These values will be automatically bound to any field annotated with {@link org.codepond.wizardroid.persistence.ContextVariable}.
     * NOTE: Context Variable names are unique and therefore must
     * have the same name and type wherever you wish to use them.
     */
    @ContextVariable
    private String firstname;
    @ContextVariable
    private String lastname;


    //You must have an empty constructor for every step
    public FormStep3() {
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
