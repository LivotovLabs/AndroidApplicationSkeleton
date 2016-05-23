# AndroidApplicationSkeleton
An application skeleton to bootstrap your next Android app instantly

## The Lazy Idea

The main idea of this project is that's Im very lazy :) 
Bootstrapping a new app from scratch is a kind of pain - you have to create a base gradle project,
setup commonly used deps, write commonly used code such as App class, base activities, plug in
event bus, plug in themes, material design tweaks, etc, etc, etc. And this repeats from project to project !

So the main goal of this skeleton is to speed up the new app bootstrapping process. To delete smth
is always faster than to create smth :)

So, this project is just a skeleton with:

- Nice gradle project structure
- Pre-configured modules for mobile app and its wear app companion
- Pre-configured commonly used deps
- Pre-configured app signing infrastructure
- Examples package from where you can copy-paste the code


## Quick Start

### Initializing your new App
- Clone or download this repo
- When you need to start a new app project, simply run: __sh bootstrap-app.sh <your-app-package-name> <<your-app-project-folder>>__
- Open <your-app-project-folder> in Android Studio

### Fine-tune the project
Now you have a nice app project which is ready to be run and loaded with the bunch of goofies. You can now fine-tune your new app project:

- Generate the keystore for your app, look at config/signing.gradle to attach it to your project for automatic signing
- Explore config/deps.gradle and mobile/build.gradle to remove the unneeded deps, add your specific ones, etc
- Explore <your-package-name>/examples source folder to see the bundled examples - you can freely remove them or use as copy-paste source to build your app blocks
- Explore <your-package-name>/core source folder to see base classes you can extent your app, activities and fragments from (if you wish, of course !)
- Start coding your actual app logic


More docs will come later, this skeleton is still in progress, stay tuned !
