# Android Application Template
An universal application template to bootstrap your next Android app almost instantly in either old Java or plain Kotlin style!

## The Lazy Idea

Bootstrapping a new app from scratch is quite boring - you have to create a well supportable base gradle project,
setup commonly used dependencies, write commonly used code snippets such as App class, base activities, etc,
attach third-party everyday libs like picasso or glide, event bus, your lovely mvp framework, the butterknife, add themes, 
perform some initial material design tweaks and a lot of other small but boring stuff you have to repeat from app to app every time.
And only once all above is done and errors fixed, you can relax, build the coffee and start coding your idea for real.

The main goal of this template is to speed up bootstrapping the new Android application project by having all the boring things 
already done once and perfectly done. Via the well mainteained template. 

## Quick Start

- Clone or download this repo
- When you need to start a new android app - simply run the bootstrap script - either for old java or new oure kotlin template:

__./mkproject-java.sh PATH-TO-NEW-APP-PROJECT-FOLDER__
__./mkproject-kotlin.sh PATH-TO-NEW-APP-PROJECT-FOLDER__

- Answer to a couple of questions such as app name, app ID and project location
- Wait till bootstrap process is completed (seconds)
- Open __PATH-TO-NEW-APP-PROJECT-FOLDER__ in Android Studio 3.0.1 or higher - it will recognize the gradle-based project and automatically import it. 

That's it! Your have complete app project ready - press "run" to see it compiles and deploys to your device, displaying the empty activity.
Now you can start coding your next great idea! :)

The bootstrap script will copy the template files to your new project folder, set your application ID to gradle (and other) files and rename 
the java packages accordingly. Once finished you should have the complete android app project ready to run - simply start coding your actual app from now on.


This project template contains:

- Nice maintainable gradle project structure with version and deps management
- Pre-configured modules for mobile app and its wear app companion
- Pre-configured the most commonly used deps (at least for me, but feel free to fork and create your own defaults)
- Pre-configured app signing infrastructure

Even if you'll find this template contain things you don't not need in your next app, it is always easier and faster to delete 
something unneeded rather than add and configure, so simply inspect the build.gradle file and remove the stuff you don't need :)

Basically, just fork this repo, adjust the template to your own needs and requirements and use it in everyday work then.


### Fine-tune the project, created from this template:

- Replace dummy mobile/google-services.json with your actual one (download it from your firebase console)
- Generate the keystore for your app, look at config/signing.gradle to attach it to your project for automatic signing
- Explore config/deps.gradle and mobile/build.gradle to remove the unneeded deps, add your specific ones, etc
- Explore config/versions.gradle to see/adjust version numbers

### FCM

This template contains the pre-configured client-side FCM token management and services registration. If you're going to use the FCM in your app, generated from
this template, all you need is to just add your token-to-backend delivery code into the AppFirebaseToBackendDeliveryService class - see the empty placeholder
method named "sendTokenToBackend(String)". The rest is already preconfigured. Receive your messages in AppFirebaseMessageService.class in "onMessageReceived(RemoteMessage)"
empty placeholder method, according to FCM docs.

## Notes
The __mkproject-[java|kotlin].sh__ scripts are tested in OSX only. Although this is a bash script abd should work on any unix system, 
some (most likely the "sed") commands may have non-compatible args between various unix flavors, you might need to adjust them then.

Im not a bash scripting guy, so the bootstrapping script is definitely not perfect. I'd say it is very bad.
Would be happy for any help with improving it - post your pull requests with enhancements/fixes/updates - always welcome :)

Cheers!

