WizarDroid
==========

WizarDroid is an Android library for creating Wizard like Activities such as Installation Wizard, 
Step by Step processes, etc. It is built on top of Android's Fragments featuring Wizard state persistence 
and simple API for controling the flow of your application. 

Prereqiusits
------------

You need to make sure that your project is compatible with the following:

1.	Minimum SDK is API level 9
2.	Android's support library

Getting started
---------------

The basic Wizard scenario:

1.	Create an Activity that inherits from WizardActivity
2.	Create your Wizard's steps by inheriting from WizardStep
3.	Once you've got your Wizard's steps ready, override onSetup() in your WizardActivity and create a new WizardFlow
4.	Last but not least, override onWizardDone() in your WizardActivity and do whatever you want to do once the Wizard reached the last step (typically, here you will call the activity's finish() method or return a result, etc.)

**1.	The Activity's Layout**

**Notice the FrameLayout which will act as our Step container.**

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		tools:context=".BasicWizardActivity" >

		<!-- **You must have this FrameLayout as the WizardStep container** -->
		<FrameLayout 
			android:id="@+id/step_container"
			android:layout_width="match_parent"
			android:layout_height="wrap_content" />
		<-- End of WizardStep container -->
		
		<Button
			android:id="@+id/next_button"
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_alignParentBottom="true"
			android:layout_alignParentRight="true"
			android:text="@string/button_next" />
	</RelativeLayout>

**2.	The Activity**

	public class BasicWizardctivity extends WizardActivity implements OnClickListener {
		//Button for moving to the next step
		private Button nextButton; 
		
		//You must override this method
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			//Very basic activity initialization
			super.onCreate(savedInstanceState);
			setContentView(R.layout.basic_wizard);
			nextButton = (Button)findViewById(R.id.next_button);
			nextButton.setOnClickListener(this);
		}

		//Creating the WizardFlow using the WizardFlow.Builder
		@Override
		public WizardFlow onSetup() {
			//The builder must first set the current activity right after it the container for the Wizard Steps
			return new WizardFlow.Builder()
				.setActivity(this)
				.setContainerId(R.id.step_container) 
				.addStep(new Step1())
				.addStep(new Step2())
				.addStep(new Step3())
				.create(); //Finally call create() to create the WizardFlow object
		}
		
		//Global handler for all steps for the Next button click
		//You can also wire the handler locally per WizardStep instead of globally within the WizardActivity
		@Override
		public void onClick(View v) {
			WizardStep step = super.getCurrentStep();
			//Calling done() on a step will signal the wizard that the step
			//was completed successfully and proceed to the next step
			step.done();
		}
		
		//You must override this method
		@Override
		public void onWizardDone() {
			//Do whatever you want to do once the Wizard is complete
			//in this case I just close the activity, which causes Android
			//to go back to the previous activity
			finish();
		}
	}

**3.	Steps Layout**

**To make it simple, we will use the same layout and code for all the steps.**

**XML:**

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:orientation="vertical" >
		<TextView 
			android:id="@+id/step_text"
			android:layout_width="match_parent"
			android:layout_height="wrap_content" />
	</LinearLayout>

**Code:**

	//Make sure to rename this class appropriately
	public class Step1 extends WizardStep {
		//You must have an empty constructor for every step
		public Step1() {
		}

		//Set your layout here
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.step1_content, container, false);
			//Do your thing here
			return v;
		}
	}

**That's it!** Run the application and see how it's working.
Press the next button to go to the next step and press your phone's back button to go back one step.

**This is just a simple example of what you can do with WizarDroid.
For more complex scenarios continue to the Advanced section.**

Installation
------------

1.	The easiest way is to add a Maven dependency like so:

    <dependency>
		<groupId>org.codepond.android.wizardroid</groupId>
		<artifactId>wizardroid-core</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>

2.	You can also download the latest release JAR and simply put it in your class path.

License
-------

You may use WizarDroid under the terms of MIT License.
