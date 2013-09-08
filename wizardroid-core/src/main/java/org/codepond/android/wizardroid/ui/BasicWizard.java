package org.codepond.android.wizardroid.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;

import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardFragment;

/**
 * Basic Wizard UI class with built-in layout.
 * The layout uses two buttons 'Next' and 'Back' to control the wizard. When the wizard
 * reaches the last step, the 'Next' button label will change to 'Finish'. The 'Back' button
 * is disabled on the first step. Extend this class if you wish to use a wizard with this built-in layout.
 * Otherwise, extend {@link WizardFragment} and implement a custom wizard layout.
 * Override {@link WizardFragment#onSetup()} to set up the wizard's flow
 * and optionally {@link WizardFragment#onWizardComplete()} to handle wizard's finish event.

 */
public abstract class BasicWizard extends WizardFragment implements View.OnClickListener {

    private Button nextButton;
    private Button previousButton;

    private String nextButtonText;
    private String finishButtonText;
    private String backButtonText;

    /**
     * Empty constructor for Fragment
     * You must have an empty constructor according to {@link #Fragment} documentation
     */
    public BasicWizard() {
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
     * Override this method to change the default behavior.
     */
    @Override
    public void onWizardComplete() {
        //Do whatever you want to do once the Wizard is complete
        //in this case I just close the activity, which causes Android
        //to go back to the previous activity.
        getActivity().finish();
    }

    /**
     * OnClick event for the built-in wizard control buttons
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wizard_next_button) {
            //Tell the wizard to go to next step
            wizard.goNext();
        }
        else if (v.getId() == R.id.wizard_previous_button) {
            //Tell the wizard to go back one step
            wizard.goBack();
        }
    }

    /**
     * Event triggered after a step was changed
     */
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

    /**
     * Get 'Next' button label
     * @return Default label 'Next' or user defined label
     */
    public String getNextButtonText() {
        return TextUtils.isEmpty(nextButtonText) ? getResources().getString(R.string.action_next) : nextButtonText;
    }

    /**
     * Set 'Next' button label
     * @param nextButtonText Label for the Next button
     */
    public void setNextButtonText(String nextButtonText) {
        this.nextButtonText = nextButtonText;
    }

    /**
     * Get 'Finish' button label. 'Finish' button appears at the last step.
     * @return Default label 'Finish' or user defined label
     */
    public String getFinishButtonText() {
        return TextUtils.isEmpty(finishButtonText) ? getResources().getString(R.string.action_finish) : finishButtonText;
    }

    /**
     * Set 'Finish' button label
     * @param finishButtonText    Label for the Finish button
     */
    public void setFinishButtonText(String finishButtonText) {
        this.finishButtonText = finishButtonText;
    }

    /**
     * Get 'Back' button label
     * @return Default label 'Back' or user defined label
     */
    public String getBackButtonText() {
        return TextUtils.isEmpty(backButtonText) ? getResources().getString(R.string.action_previous) : backButtonText;
    }

    /**
     * Set 'Back' button label
     * @param backButtonText Label for the Back button
     */
    public void setBackButtonText(String backButtonText) {
        this.backButtonText = backButtonText;
    }
}