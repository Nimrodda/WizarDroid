Overview
--------

WizarDroid is a lightweight Android library, developed by CodePond.org, that addresses a feature that Android is surprisingly missing, Wizards. It is built on top of Android's ViewPager to enable slide animation and touch functionality.
Implementation is done mainly by extending __BasicWizardLayout__ class.

Key advantages:

* Built-in basic wizard layout with paging and slide animation
* Wizard context for persisting data in the wizard
* Compatible with other libraries such as [ActionBarSherlock](http://actionbarsherlock.com/)
* Support for nested fragments
* Wizard's flow is defined in one place and can be maintained easily
* Simple API for controlling wizard's flow in runtime 


More info is available on WizarDroid [home page](http://wizardroid.codepond.org). 

Requirements
------------

You need to make sure that your project is compatible with the following:

1.	Minimum SDK is API level 9
2.	Android's support-library-v4 r18


Build and Documentation
-----------------------
When cloning the repository, make sure to clone also the documentation submodule. To do this, type the following from the root folder of the repository:

    git submodule init
    git submodule update
    
For more information refer to the [Wiki pages on github](https://github.com/Nimrodda/WizarDroid/wiki) or check out the bundled sample project.

Contribute
----------

Right now the sample app is very poor. I'm looking for extra hands on that.
Please send me an email if you wish to help me with it.
Otherwise if you wish to contribute, fork and create a pull request. Also don't forget to update the documentation located in the `docs/` folder.

Thanks!

Credits
-------
WizarDroid is developed and maintained by Nimrod Dayan ([CodePond.org](http://www.codepond.org)).
Feedback is warmly welcome: feedback@codepond.org or open an issue on [Github](https://github.com/Nimrodda/WizarDroid/issues).

License
-------
You may use WizarDroid under the terms of [MIT License](https://github.com/Nimrodda/WizarDroid/blob/master/license).
    


