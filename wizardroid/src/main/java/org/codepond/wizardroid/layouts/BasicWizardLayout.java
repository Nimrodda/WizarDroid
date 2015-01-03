package org.codepond.wizardroid.layouts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;

import org.codepond.wizardroid.R;
import org.codepond.wizardroid.WizardFragment;
import org.codepond.wizardroid.persistence.ContextManager;

/**
 * Basic Wizard UI class with built-in layout.
 * The layout uses two buttons 'Next' and 'Back' to control the wizard. When the wizard
 * reaches the last step, the 'Next' button label will change to 'Finish'. The 'Back' button
 * is disabled on the first step. Extend this class if you wish to use a wizard with this built-in layout.
 * Otherwise, extend {@link WizardFragment} and implement a custom wizard layout.
 * Override {@link WizardFragment#onSetup()} to set up the wizard's flow
 * and optionally {@link WizardFragment#onWizardComplete()} to handle wizard's finish event.
 * Note that button labels are changeable by calling {@link #setNextButtonText(String)}, {@link #setBackButtonText(String)} and
 * {@link #setFinishButtonText(String)}.

 */
public abstract class BasicWizardLayout extends WizardFragment implements View.OnClickListener {

    private Button nextButton;
    private Button previousButton;

    private String nextButtonText;
    private String finishButtonText;
    private String backButtonText;

    /**
     * Empty constructor for Fragment
     * You must have an empty constructor according to {@link #Fragment} documentation
     */
    public BasicWizardLayout() {
        super();
    }

	public BasicWizardLayout(ContextManager contextManager) {
		super(contextManager);
	}

	/**
     * Setting the layout for this basic wizard layout and hooking up wizard controls to their
     * OnClickListeners.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View wizardLayout = inflater.inflate(R.layout.wizardroid_basic_wizard, container, false);
        nextButton = (Button) wizardLayout.findViewById(R.id.wizard_next_button);
        nextButton.setOnClickListener(this);
        nextButton.setText(getNextButtonLabel());
        previousButton = (Button) wizardLayout.findViewById(R.id.wizard_previous_button);
        previousButton.setOnClickListener(this);
        previousButton.setText(getBackButtonLabel());

        return wizardLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWizardControls();
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
        super.onWizardComplete();
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
     * Event triggered after a step was changed, updating the button labels accordingly
     */
    @Override
    public void onStepChanged() {
		super.onStepChanged();
        updateWizardControls();
    }

    /**
     * Updates the UI according to current step position
     */
    private void updateWizardControls() {
        //Disable the back button in the first step
        previousButton.setEnabled(!wizard.isFirstStep());

        previousButton.setText(getBackButtonLabel());
        //Disable the next button if the step is marked as 'required' and is incomplete
        nextButton.setEnabled(wizard.canGoNext());

        //Set different next button label based on the wizard position
        nextButton.setText(wizard.isLastStep()
                ? getFinishButtonText()
                : getNextButtonLabel());
    }

    /**
     * Get 'Next' button label
     * @return Default label 'Next' or user defined label
     */
    public String getNextButtonLabel() {
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
    public String getBackButtonLabel() {
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