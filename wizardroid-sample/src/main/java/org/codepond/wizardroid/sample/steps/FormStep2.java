package org.codepond.wizardroid.sample.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.sample.R;

/**
 * This step will block the user from proceeding to the next step
 * unless the user mark the checkbox. The step is marked as required
 * when the wizard flow is built. See {@link org.codepond.wizardroid.sample.wizards.FormWizard#onSetup()} for more info.
 */
public class FormStep2 extends WizardStep {

    private CheckBox checkBox;
    //You must have an empty constructor for every step
    public FormStep2() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_form2, container, false);
        checkBox = (CheckBox) v.findViewById(R.id.sample_form2_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Notify that the step is completed
                    notifyCompleted();
                }
                else {
                    //Notify that the step is incomplete
                    notifyIncomplete();
                }
            }
        });
        return v;
    }
}
