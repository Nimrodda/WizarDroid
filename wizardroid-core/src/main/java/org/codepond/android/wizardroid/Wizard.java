package org.codepond.android.wizardroid;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * The engine of the Wizard. This class is in charge of
 * the wizard's flow progression. It is used directly by
 * {@link WizardActivity} to manage the wizard.
 */
class Wizard {
	private static final String TAG = "Wizard";
	private int currentStep;
	private final FragmentManager fragmentManager;
	private final WizardFlow flow;
	private final int fragmentContainerId;

    private Bundle context;


    /**
	 * Constructor for Wizard
	 * @param wizardFlow WizardFlow instance. See WizardFlow.Builder for more information on creating WizardFlow objects.
	 */
	Wizard(WizardFlow wizardFlow) {
		this.flow = wizardFlow;
		this.fragmentContainerId = wizardFlow.getFragmentContainerId();
		this.fragmentManager = flow.getFragmentManager();
		this.context = new Bundle();

		String currentStepTag = WizardFlow.getTagForWizardStep(currentStep, getCurrentStep().getClass());
		WizardStep step = (WizardStep) fragmentManager.findFragmentByTag(currentStepTag);
		if (step == null) {
			fragmentManager.beginTransaction().add(fragmentContainerId, getCurrentStep(), currentStepTag).commit();
			getCurrentStep().setState(WizardStep.STATE_RUNNING);
		}

	}

	/**
	 * Advance the wizard to the next step
	 */
	void next() {
        persistStepContext();
		currentStep++;
        passStepContext();
		String currentStepTag = WizardFlow.getTagForWizardStep(currentStep, getCurrentStep().getClass());
		fragmentManager.beginTransaction().replace(fragmentContainerId, getCurrentStep(), currentStepTag)
				.addToBackStack(null).commit();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}

    /**
	 * Takes the wizard one step back
	 */
	void back() {
		getCurrentStep().setState(WizardStep.STATE_PENDING);
		currentStep--;
		fragmentManager.popBackStack();
		getCurrentStep().setState(WizardStep.STATE_RUNNING);
	}
	
	/**
	 * Sets the current step of the wizard
	 * @param stepId the position of the step within the WizardFlow
	 */
	void setCurrentStep(int stepId) {
		currentStep = stepId;
	}
	
	/**
	 * Gets the current step position
	 * @return integer representing the position of the step in the WizardFlow
	 */
	int getCurrentStepPosition() {
		return currentStep;
	}
	
	/**
	 * Gets the current step
	 * @return WizardStep the current WizardStep instance
	 */
	WizardStep getCurrentStep() {
		return flow.getSteps().get(currentStep);
	}
	
	/**
	 * Gets the step at specific position
	 * @param position the position of the step within the WizardFlow
	 * @return WizardStep the instance of WizardStep in the required position
	 * @throws ArrayIndexOutOfBoundsException
	 */
	WizardStep getStepAtPosition(int position) throws ArrayIndexOutOfBoundsException {
		return flow.getSteps().get(position);
	}
	
	/**
	 * Checks if the current step is the last step in the Wizard
	 * @return boolean representing the result of the check
	 */
	boolean isLastStep() {
		return currentStep == flow.getSteps().size() - 1;
	}
	
	/**
	 * Checks if the step is the first step in the Wizard
	 * @return boolean representing the result of the check
	 */
	boolean isFirstStep() {
		return currentStep == 0;
	}

    Bundle getContext() {
        return context;
    }

    void setContext(Bundle context) {
        this.context = context;
    }

    private void passStepContext() {
        Field[] fields = getCurrentStep().getClass().getDeclaredFields();
        //Check if arguments were already set on setup, otherwise creates a new bundle
        Bundle args = getCurrentStep().getArguments();
        if (args == null) {
            args = new Bundle();
        }
        //Scan the step for fields annotaed with @ContextVariable and check if there is a value stored in the Wizard Context for the field name
        for (Field field : fields) {
            if (field.getAnnotation(ContextVariable.class) != null && context.containsKey(field.getName())) {
                field.setAccessible(true);
                //Found a value for the annotated field, adding it to the step's argument for later binding
                if (field.getType() == String.class) {
                    args.putString(field.getName(), context.getString(field.getName()));
                }
                else if (field.getType() == Integer.class) {
                    args.putInt(field.getName(), context.getInt(field.getName()));
                }
                else if (field.getType() == Boolean.class) {
                    args.putBoolean(field.getName(), context.getBoolean(field.getName()));
                }
                else if (field.getType() == Double.class) {
                    args.putDouble(field.getName(), context.getDouble(field.getName()));
                }
                else if (field.getType() == Float.class) {
                    args.putFloat(field.getName(), context.getFloat(field.getName()));
                }
                else if (field.getType() == Short.class) {
                    args.putShort(field.getName(), context.getShort(field.getName()));
                }
                else if (field.getType() == Byte.class) {
                    args.putByte(field.getName(), context.getByte(field.getName()));
                }
                else if (field.getType() == Long.class || field.getType() == Date.class) {
                    args.putLong(field.getName(), context.getLong(field.getName()));
                }
                else if (field.getType() == Character.class) {
                    args.putChar(field.getName(), context.getChar(field.getName()));
                }
                else if (field.getType() == Parcelable.class) {
                    args.putParcelable(field.getName(), context.getParcelable(field.getName()));
                }
                else if (field.getType() instanceof Serializable) {
                    args.putSerializable(field.getName(), context.getSerializable(field.getName()));
                }
                //TODO: Add support for arrays
            }
        }
        getCurrentStep().setArguments(args);
    }

    private void persistStepContext() {
        //Scan the step for fields annotated with @ContextVariable
        Field[] fields = getCurrentStep().getClass().getDeclaredFields();
        for (Field field : fields) {
            ContextVariable contextVar = field.getAnnotation(ContextVariable.class);
            if (contextVar != null) {
                //Store its value in the Wizard Context
                field.setAccessible(true);
                try {
                    if (field.getType() == String.class) {
                        context.putString(field.getName(), (String) field.get(getCurrentStep()));
                    }
                    else if (field.getType() == Integer.class) {
                        context.putInt(field.getName(), field.getInt(getCurrentStep()));
                    }
                    else if (field.getType() == Boolean.class) {
                        context.putBoolean(field.getName(), field.getBoolean(getCurrentStep()));
                    }
                    else if (field.getType() == Double.class) {
                        context.putDouble(field.getName(), field.getDouble(getCurrentStep()));
                    }
                    else if (field.getType() == Float.class) {
                        context.putFloat(field.getName(), field.getFloat(getCurrentStep()));
                    }
                    else if (field.getType() == Short.class) {
                        context.putShort(field.getName(), field.getShort(getCurrentStep()));
                    }
                    else if (field.getType() == Byte.class) {
                        context.putByte(field.getName(), field.getByte(getCurrentStep()));
                    }
                    else if (field.getType() == Long.class) {
                        context.putLong(field.getName(), field.getLong(getCurrentStep()));
                    }
                    else if (field.getType() == Character.class) {
                        context.putChar(field.getName(), field.getChar(getCurrentStep()));
                    }
                    else if (field.getType() == Parcelable.class) {
                        context.putParcelable(field.getName(), (Parcelable) field.get(getCurrentStep()));
                    }
                    else if (field.getType() == Date.class) {
                        context.putLong(field.getName(), ((Date)field.get(getCurrentStep())).getTime());
                    }
                    else if (field.getType() instanceof Serializable) {
                        context.putSerializable(field.getName(), (Serializable) field.get(getCurrentStep()));
                    }
                    //TODO: Add support for arrays
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
