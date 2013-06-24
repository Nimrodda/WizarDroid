package org.codepond.android.wizardroid.sample.steps;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.codepond.android.wizardroid.ContextVariable;
import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardStep;

public class FormStep2 extends WizardStep implements View.OnClickListener {

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

        firstnameTv.setText(firstname);
        lastnameTv.setText(lastname);

        //Set listener for 'Next' button click
        //Note that we are setting OnClickListener using getActivity() because
        //the 'Next' button is actually part of the hosting activity's layout and
        //not the step's layout
        Button nextButton = (Button) getActivity().findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
        nextButton.setText("Finish");

        return v;
    }

    @Override
    public void onClick(View view) {
        //Do some work
        //...
        //And call done() to signal that the step is completed successfully
        done();
    }
}
