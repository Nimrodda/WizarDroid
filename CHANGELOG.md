Change Log
===============================================================================

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