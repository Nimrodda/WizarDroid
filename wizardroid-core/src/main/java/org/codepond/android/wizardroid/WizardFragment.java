package org.codepond.android.wizardroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.codepond.android.wizardroid.persistence.ContextManager;
import org.codepond.android.wizardroid.persistence.ContextManagerImpl;

/**
 * Base class for fragments that want to implement step-by-step wizard functionality.
 * Override {@link WizardFragment#onSetup()} to set up the wizard's flow
 * and optionally {@link WizardFragment#onWizardComplete()} ()} to handle wizard's finish event.
 * Extend this class to implement your own custom wizard layout and user {@link Wizard} API to
 * control the wizard. Typically, you'd call {@link Wizard#goNext()} and {@link Wizard#goBack()}
 * from your controls onClick event to control the flow of the wizard.
 */
public abstract class WizardFragment extends Fragment implements Wizard.WizardCallbacks {
	private static final String TAG = WizardFragment.class.getSimpleName();
    private static final String STATE_WIZARD_CONTEXT = "ContextVariable";
    private WizardFlow flow;
    private ContextManager contextManager;

    protected Wizard wizard;

    public WizardFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "Loading wizard data");
        flow = onSetup();
        if (flow == null) {
            throw new IllegalArgumentException("Error setting up the Wizard's flow. You must override WizardFragment#onSetup " +
                    "and use WizardFlow.Builder to create the Wizard's flow followed by WizardFragment#super.onSetup(flow)");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO: get rid of this dependency
        contextManager = new ContextManagerImpl();
        if (savedInstanceState != null) {
            //Load pre-saved wizard context
            contextManager.setContext(savedInstanceState.getBundle(STATE_WIZARD_CONTEXT));
        }
        else {
            //Initialize wizard context
            contextManager.setContext(new Bundle());
        }
        wizard = new Wizard(flow, contextManager, this, this.getActivity());
        //Persist hosting activity/fragment fields to wizard context enabling easy data transfer between
        //wizard host and the steps
        contextManager.persistStepContext(this);
    }

    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        //Persist wizard context
        outState.putBundle(STATE_WIZARD_CONTEXT, contextManager.getContext());
	}

    /**
     * Execute when wizard is complete.
     */
    @Override
    public void onWizardComplete() {

    }

    /**
	 * Set up the Wizard's flow. Use {@link WizardFlow.Builder} to create the Wizard's flow.
	 */
	public abstract WizardFlow onSetup();

}