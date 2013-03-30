package org.codepond.android.wizardroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelloWizardActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tutorialButton:
                startActivity(new Intent(this, TutorialWizard.class));
                break;
            case R.id.interactiveButton:
                break;
            default:
                break;
        }
    }
}

