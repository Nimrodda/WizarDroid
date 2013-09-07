package org.codepond.android.wizardroid.sample.wizards;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;

import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardFragment;

/**
 * Base Wizard UI class with built-in layout.
 * The layout uses two buttons 'Next' and 'Back' to control the wizard.
 */
public abstract class BaseWizard extends WizardFragment implements View.OnClickListener {

    private Button nextButton;
    private Button previousButton;

    private String nextButtonText;
    private String finishButtonText;
    private String backButtonText;

    /**
     * Empty constructor for Fragment
     * You must have an empty constructor according to {@link #Fragment} documentation
     */
    public BaseWizard() {
        super();
    }
    /**
     * Setting common layout for all wizards
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View wizardLayout = inflater.inflate(R.layout.wizard, container, false);
        nextButton = (Button) wizardLayout.findViewById(R.id.wizard_next_button);
        nextButton.setOnClickListener(this);
        previousButton = (Button) wizardLayout.findViewById(R.id.wizard_previous_button);
        previousButton.setOnClickListener(this);

        return wizardLayout;
    }

    /**
     * Triggered when the wizard is completed.
     * Overriding this method is optional.
     * Default implementation closes the wizard and goes back to the calling Activity.
     */
    @Override
    public void onWizardComplete() {
        //Do whatever you want to do once the Wizard is complete
        //in this case I just close the activity, which causes Android
        //to go back to the previous activity.
        getActivity().finish();
    }

    /**
     * Controlling wizard flow
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.wizard_next_button:
                //Tell the wizard to go to next step
                wizard.goNext();
                break;
            case R.id.wizard_previous_button:
                //Tell the wizard to go back one step
                wizard.goBack();
                break;
        }
    }

    @Override
    public void onStepChanged() {
        updateWizardControls();
    }

    /**
     * Updates the UI according to current step position
     */
    private void updateWizardControls() {
        previousButton.setEnabled(!wizard.isFirstStep());
        previousButton.setText(getBackButtonText());
        nextButton.setText(wizard.isLastStep()
                ? getFinishButtonText()
                : getNextButtonText());
    }

    public String getNextButtonText() {
        return TextUtils.isEmpty(nextButtonText) ? getResources().getString(R.string.action_next) : nextButtonText;
    }

    public void setNextButtonText(String nextButtonText) {
        this.nextButtonText = nextButtonText;
    }

    public String getFinishButtonText() {
        return TextUtils.isEmpty(finishButtonText) ? getResources().getString(R.string.action_finish) : finishButtonText;
    }

    public void setFinishButtonText(String finishButtonText) {
        this.finishButtonText = finishButtonText;
    }

    public String getBackButtonText() {
        return TextUtils.isEmpty(backButtonText) ? getResources().getString(R.string.action_previous) : backButtonText;
    }

    public void setBackButtonText(String backButtonText) {
        this.backButtonText = backButtonText;
    }
}