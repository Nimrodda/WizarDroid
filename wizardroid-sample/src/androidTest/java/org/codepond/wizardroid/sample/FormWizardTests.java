package org.codepond.wizardroid.sample;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class FormWizardTests extends ActivityInstrumentationTestCase2<FormActivity> {
	private Solo solo;

	public FormWizardTests() {
		super(FormActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testAdvancingWizardViaNextButtonShouldPersistContext() {
		advanceWizard(false);
	}

	public void testAdvancingWizardViaSwipeShouldPersistContext() {
		advanceWizard(true);
	}

	private void advanceWizard(boolean swipe) {
		final String expected1 = "Han";
		final String expected2 = "Solo";

		// First step
		final EditText firstname = (EditText) solo.getView(R.id.firstnameField);
		final EditText lastname = (EditText) solo.getView(R.id.lastnameField);

		solo.clearEditText(firstname);
		solo.clearEditText(lastname);
		solo.enterText(firstname, expected1);
		solo.enterText(lastname, expected2);

		if (swipe) {
			solo.scrollViewToSide(solo.getView(R.id.step_container), Solo.RIGHT);
		}
		else {
			solo.clickOnButton("Next");
		}

		// 2nd step
		solo.waitForView(R.id.sample_form2_checkbox);
		solo.clickOnCheckBox(0);
		if (swipe) {
			solo.scrollViewToSide(solo.getView(R.id.step_container), Solo.RIGHT);
		}
		else {
			solo.clickOnButton("Next");
		}

		// Summary step
		TextView tv1 = (TextView) solo.getView(R.id.firstname);
		TextView tv2 = (TextView) solo.getView(R.id.lastname);

		assertEquals(expected1, tv1.getText().toString());
		assertEquals(expected2, tv2.getText().toString());
	}
}
