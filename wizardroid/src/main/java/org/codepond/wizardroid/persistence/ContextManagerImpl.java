package org.codepond.wizardroid.persistence;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import org.codepond.wizardroid.WizardFragment;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * This class implements {@link ContextManager} and uses Android
 * built-in Bundle and Fragment's arguments to pass data
 * in the wizard.
 */
public class ContextManagerImpl implements ContextManager {

    private Bundle context;

    @Override
    public void loadStepContext(Fragment step) {
        Field[] fields = step.getClass().getDeclaredFields();
        //Check if arguments were already set on setup, otherwise creates a new bundle
        Bundle args = step.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        //Scan the step for fields annotated with @ContextVariable and check if there is a value stored in the Wizard Context for the field name
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
                else if (Parcelable.class.isAssignableFrom(field.getType())) {
                    args.putParcelable(field.getName(), context.getParcelable(field.getName()));
                }
                else if (field.getType() instanceof Serializable) {
                    args.putSerializable(field.getName(), context.getSerializable(field.getName()));
                }
                else {
                    //TODO: Add support for arrays
                    throw new RuntimeException(String.format("Unsuported type. Cannot pass value to variable %s of step %s. Variable type is unsuported.",
                            field.getName(), step.getClass().getName()));
                }
            }
        }
        if (step instanceof WizardFragment) {
            bindFields((WizardFragment)step, args);
        }
        else {
            step.setArguments(args);
        }
    }

    @Override
    public void persistStepContext(Fragment step) {
        //Scan the step for fields annotated with @ContextVariable
        Field[] fields = step.getClass().getDeclaredFields();
        for (Field field : fields) {
            ContextVariable contextVar = field.getAnnotation(ContextVariable.class);
            if (contextVar != null) {
                //Store its value in the Wizard Context
                field.setAccessible(true);
                try {
                    if (field.getType() == String.class) {
                        context.putString(field.getName(), (String) field.get(step));
                    }
                    else if (field.getType() == Integer.class) {
                        context.putInt(field.getName(), field.getInt(step));
                    }
                    else if (field.getType() == Boolean.class) {
                        context.putBoolean(field.getName(), field.getBoolean(step));
                    }
                    else if (field.getType() == Double.class) {
                        context.putDouble(field.getName(), field.getDouble(step));
                    }
                    else if (field.getType() == Float.class) {
                        context.putFloat(field.getName(), field.getFloat(step));
                    }
                    else if (field.getType() == Short.class) {
                        context.putShort(field.getName(), field.getShort(step));
                    }
                    else if (field.getType() == Byte.class) {
                        context.putByte(field.getName(), field.getByte(step));
                    }
                    else if (field.getType() == Long.class) {
                        context.putLong(field.getName(), field.getLong(step));
                    }
                    else if (field.getType() == Character.class) {
                        context.putChar(field.getName(), field.getChar(step));
                    }
                    else if (Parcelable.class.isAssignableFrom(field.getType())) {
                        context.putParcelable(field.getName(), (Parcelable) field.get(step));
                    }
                    else if (field.getType() == Date.class) {
						Date d = (Date) field.get(step);
						if (d != null) {
							context.putLong(field.getName(), d.getTime());
						}
                    }
                    else if (field.getType() instanceof Serializable) {
                        context.putSerializable(field.getName(), (Serializable) field.get(step));
                    }
                    else {
                        //TODO: Add support for arrays
                        throw new RuntimeException(String.format("Unsuported type. Cannot pass value to variable %s of step %s. Variable type is unsuported.",
                                field.getName(), step.getClass().getName()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Bundle getContext() {
        return context;
    }

    @Override
    public void setContext(Bundle context) {
        this.context = context;
    }

    private void bindFields(WizardFragment wizardFragment, Bundle args) {
        //Scan the step for fields annotated with @ContextVariable
        //and bind value if found in step's arguments
        Field[] fields = wizardFragment.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(ContextVariable.class) != null && args.containsKey(field.getName())) {
                field.setAccessible(true);
                try {
                    if (field.getType() == Date.class) {
                        field.set(wizardFragment, new Date(args.getLong(field.getName())));
                    }
                    else {
                        field.set(wizardFragment, args.get(field.getName()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
