# Android Application Template
An application template to bootstrap your next Android app almost instantly.

## The Lazy Idea

Bootstrapping a new app from scratch is a kind of pain - you have to create a base gradle project,
setup commonly used dependencies, write commonly used code snippets such as App class, base activities,
attach third-party everyday libs like event bus, butterknife, add themes, perform some initial material design tweaks and a lot of other stuff you repeat for almost every new Android project. And only after this you can finally start coding your actual app logic and UI. 

Starting the new app ? Repeat all the steps above again and again... Too boring!

The main goal of this template is to speed up bootstrapping the new Android application project by having all the boring things already done once in a template project. Then copy this project to your new application folder and change the package names and identifiers. A several seconds task for a bash script!

## Quick Start

- Clone or download this repo
- When you need to start a new android app - simply run the bootstrap script: __./mkproject.sh PATH-TO-NEW-APP-PROJECT-FOLDER__
- Answer to a couple of questions
- Wait till bootstrap process is completed
- Open __PATH-TO-NEW-APP-PROJECT-FOLDER__ in Android Studio - it will recognize gradle-based project and import it. 

That's it! Your have complete app project and can start coding your great things.

The bootstrap script will copy the template files to your new project folder, set your application ID to gradle files and rename the java packages accordingly. Once finished you should have the complete android app project ready to run - simply start coding your actual app from now on.

This project template contains:

- Nice gradle project structure with version management
- Pre-configured modules for mobile app and its wear app companion
- Pre-configured the most commonly used deps (at least for me, but feel free to fork and create your own defaults)
- Pre-configured app signing infrastructure
- Examples package from where you can copy-paste the code

Even if you'll find this template contain things you don't not need in your next app, it is always easier and faster to delete something unneeded rather than add and configure something you need for everyday work, so simply inspect the build.gradle file and remove the stuff you don't need :)


### Fine-tune the project

- Replace dummy mobile/google-services.json with your actual one (download it from firebase console)
- Generate the keystore for your app, look at config/signing.gradle to attach it to your project for automatic signing
- Explore config/deps.gradle and mobile/build.gradle to remove the unneeded deps, add your specific ones, etc
- Explore config/versions.gradle to see/adjust version numbers
- Explore __YOUR_APP_PACKAGE_NAME/examples__ source folder to see the bundled examples - you can freely remove them or use as copy-paste source to build your app blocks
- Explore __YOUR_APP_PACKAGE_NAME/core__ source folder to see base classes you can extent your app, activities and fragments from (if you wish, of course)


## Notes
The __mkproject.sh__ script was tested in OSX only. Although this is a bash script, some (most likely the "sed") commands args may vary between various unix flavors, you might need to adjust them then.

Im not a bash scripting guy, so the bootstrapping script may be not too optimal/correct (moreover, I'm sure it is written badly!), would be happy to receive enhancements/fixes/updates for it, feel free to pull-request.
