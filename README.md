Overview
--------

WizarDroid is a lightweight Android library, developed by CodePond.org, that addresses a feature that Android is surprisingly missing, Wizards. It is built on top of Android's Fragments, using FragmentManager to dynamically change the view. Implementation is done mainly by extending __WizardFragment__ class.

Key advantages:

* Compatible with other libraries such as ActionBarSherlock
* Support for nested fragments
* Wizard's flow is defined in one place and can be maintained easily
* Step context is persistent and passed between steps automatically using reflection
* Simple and consistent API for controlling wizard's flow in runtime 


More info is available on WizarDroid [home page](http://wizardroid.codepond.org). 

Requirements
------------

You need to make sure that your project is compatible with the following:

1.	Minimum SDK is API level 9
2.	Android's support library


Build
-----

Build is done using Gradle as of Google I/O 2013: Android new build system. [Learn more](http://tools.android.com)

Run the following from the project's root folder:

    Gradle assemble

Then copy the AAR file located under folder __wizardroid-core/build/libs__ to your project's classpath.

In case you are building your project with Gradle, simply copy __wizardroid-core__ to your project's __libs__ folder and update the dependency in both __settings.gradle__ and __build.gradle__ as follows:



__Settings.gradle__:

    include ':wizardroid-core'


__Build.gradle__:

    dependencies {
        compile 'com.android.support:support-v4:18.0.0'
        compile project(':wizardroid-core')
}

**NOTE:** In order to use Android Support library v4 in Gradle you have to install '__Android Support Repository__' in Android SDK Manager.


Documentation
-------------

Refer to the [Wiki pages on github](https://github.com/Nimrodda/WizarDroid/wiki) or check out the bundled sample project.


Credits
-------
WizarDroid is developed and maintained by Nimrod Dayan ([CodePond.org](http://www.codepond.org)).

License
-------

    Copyright (c) 2013 Nimrod Dayan (CodePond.org)

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
    to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
    INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
    PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
    HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION 
    OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
    SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
    


