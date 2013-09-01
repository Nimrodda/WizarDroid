package org.codepond.android.wizardroid.sample.wizards;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;

import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardFragment;

/**
 * Base wizard class which uses the action bar to control the flow
 * of the wizard. The action bar is invalidated after each step, enabling
 * us to set buttons' labels according to the given step.
 * @see <a href="http://developer.android.com/guide/topics/ui/actionbar.html">How to use the action bar</a>
 */
public abstract class BaseWizard extends WizardFragment implements View.OnClickListener {

    private static final String TAG = BaseWizard.class.getSimpleName();
    private Button nextButton;
    private Button previousButton;

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
     * Triggered whenever the wizard is completed.
     * Overriding this method is optional.
     */
    @Override
    public void onWizardDone() {
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
                wizard.getCurrentStep().done();
                break;
            case R.id.wizard_previous_button:
                wizard.getCurrentStep().abort();
                break;
        }
        //Updating UI
        updateWizardControls();
    }

    /**
     * Updates the UI according to current step position
     */
    private void updateWizardControls() {
        previousButton.setEnabled(!wizard.isFirstStep());
        nextButton.setText(wizard.isLastStep()
                ? R.string.action_finish
                : R.string.action_next);
    }
}