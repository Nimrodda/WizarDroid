Deprecated
----------

This library is not being developed anymore. I highly recommend that you check out [Android Architecture Components Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) in combination with [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel) to build a Wizard flow.

Overview
--------

WizarDroid is a lightweight Android library that addresses a feature that Android is surprisingly missing, Wizards. It is built on top of Android's ViewPager to enable slide animation and touch functionality.

Key advantages:

* Built-in basic wizard layout with paging and slide animation
* Wizard context for persisting data in the wizard
* Support for nested fragments
* Wizard's flow is defined in one place and can be maintained easily
* Simple API for controlling wizard's flow in runtime 

For more information refer to the [Wiki pages on github](https://github.com/Nimrodda/WizarDroid/wiki) or check out the bundled sample project.

Use with Gradle
---------------

    dependencies {
        compile 'org.codepond:wizardroid:1.3.1'
    }

Contribute
----------

Fork and create a pull request. Also don't forget to update the documentation located in the `docs/` folder.
Thanks!

Credits
-------

WizarDroid is developed and maintained by Nimrod Dayan ([CodePond.org](http://www.codepond.org)).
Feedback is warmly welcome: feedback@codepond.org or open an issue on [Github](https://github.com/Nimrodda/WizarDroid/issues).

License
-------

WizarDroid is available under the [MIT License](https://github.com/Nimrodda/WizarDroid/blob/master/license).
    


