Change Log
===============================================================================

Fixed #66: WizardStep's onResume() method not working properly
Fixed memory leaks in Wizard class

Version 1.3.1 *(2015-03-23)*
----------------------------

* Fixed #59: Wizard Context not saved when pressing the Next button
* Updated Android Gradle plugin to version 1.1.0
* Updated to support-v4 library to version 22.0.0
* Updated Android Gradle plugin ID in gradle build files
* Added functional test for verifying that the wizard context is saved whether swiping or tapping next button

Version 1.3.0 *(2014-12-28)*
----------------------------

* Fixed #48: Context Variable values lost after orientation change
* Fixed #34: Swipe Back/Prev is very sensitive
* Fixed #47: ContextManagerImpl.persistStepContext() throws NullPointerException
* Fixed #31: Crash occures when having more than one required step marked completed
* Added: Gradle Wrapper - solving the need to upgrade Gradle everytime
* Feature: Automatically hide soft keyboard on step switch - contributed by @Alexander--
* Removed: Installing WizarDroid.aar to local maven repository via 'gradle install' command is not supported anymore. Either use WizarDroid's global Gradle dependency via Maven Central or include it as a library project in your app.
* Upgraded to Android API level 21, including build tools 21.1.2 and support-v4:21.0.3

Version 1.2.0 *(2014-03-18)*
----------------------------

* Fix: Issue #37 - removed minSdk and targetSdk from AndroidManifest.xml - contributed by @knicknak
* Feature: Device's back button will now cause the wizard to go to the previous step - contributed by @denny0223
* Feature: Install WizarDroid.aar to local maven repository via 'gradle install' command
* Upgraded to Gradle 1.11 and Android Gradle plugin 0.9.0

Version 1.1.1 *(2013-11-10)*
----------------------------

* Fix: Issue #32 - context variables are now updated properly when onWizardComplete event is triggered


Version 1.1.0 *(2013-11-10)*
----------------------------

* Feature: Ability to block the user to proceed to the next event by swiping or clicking the button
* Fix: Removed unnecessary log statements
* Upgraded Android SDK and Gradle to the latest versions

Version 1.0.1 *(2013-10-06)*
----------------------------

 * Fix: Crash when moving to another screen after the display orientation had changed in the middle of the wizard