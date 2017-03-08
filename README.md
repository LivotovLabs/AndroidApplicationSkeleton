# Android Application Bootstrap Template
An application template to bootstrap your next Android app almost instantly.

## The Lazy Idea

Bootstrapping a new app from scratch is a kind of pain - you have to create a base gradle project,
setup commonly used dependencies, write commonly used code snippets such as App class, base activities, etc,etc,
plug-in goodies like event bus, themes, perform some initial material design tweaks and a lot of other stuff you do in almost every project
and finally start coding your actual app logic and UI. New app ? Repeat the steps above again and again... Too boring!

Thus the main goal of this template is to speed up bootstrapping new android application project by automating all the boring things.

## Quick Start

So:

- Clone or download this repo
- When you need to start a new app project, simply run the bootstrap script: __./mkproject.sh PATH-TO-NEW-APP-PROJECT-FOLDER__
- Answer to a couple of questions
- Wait till bootstrap process is completed
- Open __PATH-TO-NEW-APP-PROJECT-FOLDER__ in Android Studio - it is ready to run, simple!

Even if this template contains things you don't not need in your next app, it is always easier and faster to delete something unneeded rather than
adding and configuring, so simply inspect the build.gradle file and remove the stuff you don't need :)


This project template contains:

- Nice gradle project structure with version management
- Pre-configured modules for mobile app and its wear app companion
- Pre-configured the most commonly used deps (at least for me, feel free to fork and create your own defaults set!)
- Pre-configured app signing infrastructure
- Examples package from where you can copy-paste the code

### Fine-tune the project

- Generate the keystore for your app, look at config/signing.gradle to attach it to your project for automatic signing
- Explore config/deps.gradle and mobile/build.gradle to remove the unneeded deps, add your specific ones, etc
- Explore config/versions.gradle to see/adjust version numbers
- Explore __YOUR_APP_PACKAGE_NAME/examples__ source folder to see the bundled examples - you can freely remove them or use as copy-paste source to build your app blocks
- Explore __YOUR_APP_PACKAGE_NAME/core__ source folder to see base classes you can extent your app, activities and fragments from (if you wish, of course !)


## Notes
The __mkproject.sh__ script was tested in OSX only. Although this is a bash script, some (most likely the "sed") commands args may vary between various unix flavors, you might need to adjust it.

Im not a bash scripting guy, so the bootstrapping script may be not too optimal/correct, would be happy to receive fixes/updates for it, feel free to pull-request!
