package org.codepond.android.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardStep;

public class TutorialStep1 extends WizardStep implements View.OnClickListener {

    //You must have an empty constructor for every step
    public TutorialStep1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tutorial_step, container, false);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText("This is an example of Step 1 in the wizard. Click the Next " +
                "button to proceed to the next tutorial_step. Hit your phone's back button to go back to the calling activity.");

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

        //And call done() to signal that the tutorial_step is completed successfully
        done();
    }
}
