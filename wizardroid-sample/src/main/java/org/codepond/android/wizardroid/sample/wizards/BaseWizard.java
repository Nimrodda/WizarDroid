package org.codepond.android.wizardroid.sample.wizards;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import org.codepond.android.wizardroid.R;
import org.codepond.android.wizardroid.WizardActivity;

/**
 * Base wizard class which uses the action bar to control the flow
 * of the wizard. The action bar is invalidated after each step, enabling
 * us to set buttons' labels according to the given step.
 * @see <a href="http://developer.android.com/guide/topics/ui/actionbar.html">How to use the action bar</a>
 */
public abstract class BaseWizard extends WizardActivity {

    private static final String TAG = BaseWizard.class.getSimpleName();

    /**
     * Setting common layout for all wizards
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);
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
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.wizard, menu);

        menu.findItem(R.id.action_previous).setEnabled(!wizard.isFirstStep());

        //Add either a "next" or "finish" button to the action bar, depending on which step
        //is currently visible.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (wizard.isLastStep())
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_previous:
                //Go to the previous step in the wizard. If there is no previous step, the
                //wizard will terminate and go back to the calling activity.
                wizard.getCurrentStep().abort();
                invalidateOptionsMenu();
                return true;

            case R.id.action_next:
                //Advance to the next step in the wizard. If there is no next step, the wizard
                //will terminate and go back to the calling activity.
                wizard.getCurrentStep().done();
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for Back key pressed which overrides WizardActivity#onKeyDown to invalidate
     * the action bar whenever back button is pressed and therefore change the buttons' labels.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.v(TAG, "Going back one step");
            wizard.getCurrentStep().abort();
            invalidateOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}