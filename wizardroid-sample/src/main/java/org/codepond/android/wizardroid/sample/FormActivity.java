package org.codepond.android.wizardroid.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.codepond.android.wizardroid.R;

public class FormActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
    }
}