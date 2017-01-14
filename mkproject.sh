#!/bin/bash

TMPL_PACKAGE="eu.livotov.labs.androidappskeleton"
TMPL_PACKAGE_TLD="eu"
TMPL_NAME="LLAndroidApplicationTemplate"

echo "Livotov Labs > Android Project Template v1.0"
echo

read -p "Application name: " PROJECT_NAME
read -p "Android app id (package): " PROJECT_PACKAGE
read -p "Project location [$1]: " PROJECT_LOCATION

PROJECT_LOCATION=${PROJECT_LOCATION:-$1}

if [ -z "$PROJECT_LOCATION" ]; then
    echo "Project location must be specified in order to continue !"
   exit
fi

echo
echo
echo "Bootstrapping project, please wait...";

mkdir -p "$PROJECT_LOCATION"
cp -r template/* "$PROJECT_LOCATION"

find "$PROJECT_LOCATION/" -type f -name '*.gradle' -exec sed -i '' "s/$TMPL_NAME/$PROJECT_NAME/g" {} \;
find "$PROJECT_LOCATION/" -type f -name '*.xml' -exec sed -i '' "s/$TMPL_NAME/$PROJECT_NAME/g" {} \;
find "$PROJECT_LOCATION/" -type f -name '*.java' -exec sed -i '' "s/$TMPL_PACKAGE/$PROJECT_PACKAGE/g" {} \;
find "$PROJECT_LOCATION/" -type f -name '*.xml' -exec sed -i '' "s/$TMPL_PACKAGE/$PROJECT_PACKAGE/g" {} \;
find "$PROJECT_LOCATION/" -type f -name '*.gradle' -exec sed -i '' "s/$TMPL_PACKAGE/$PROJECT_PACKAGE/g" {} \;
find "$PROJECT_LOCATION/" -type f -name '*.properties' -exec sed -i '' "s/$TMPL_PACKAGE/$PROJECT_PACKAGE/g" {} \;
find "$PROJECT_LOCATION/" -type f -name '*.json' -exec sed -i '' "s/$TMPL_PACKAGE/$PROJECT_PACKAGE/g" {} \;


PATH_PKG_PART="${PROJECT_PACKAGE//.//}"
PATH_TPKG_PART="${TMPL_PACKAGE//.//}"

mkdir -p "$PROJECT_LOCATION/mobile/src/main/java/$PATH_PKG_PART"
mkdir -p "$PROJECT_LOCATION/mobile/src/test/java/$PATH_PKG_PART"
mkdir -p "$PROJECT_LOCATION/mobile/src/androidTest/java/$PATH_PKG_PART"
mkdir -p "$PROJECT_LOCATION/wear/src/main/java/$PATH_PKG_PART"

cp -r "$PROJECT_LOCATION/mobile/src/main/java/$PATH_TPKG_PART/" "$PROJECT_LOCATION/mobile/src/main/java/$PATH_PKG_PART/"
cp -r "$PROJECT_LOCATION/mobile/src/test/java/$PATH_TPKG_PART/" "$PROJECT_LOCATION/mobile/src/test/java/$PATH_PKG_PART/"
cp -r "$PROJECT_LOCATION/mobile/src/androidTest/java/$PATH_TPKG_PART/" "$PROJECT_LOCATION/mobile/src/androidTest/java/$PATH_PKG_PART/"
cp -r "$PROJECT_LOCATION/wear/src/main/java/$PATH_TPKG_PART/" "$PROJECT_LOCATION/wear/src/main/java/$PATH_PKG_PART/"

rm -rf "$PROJECT_LOCATION/mobile/src/main/java/$TMPL_PACKAGE_TLD"
rm -rf "$PROJECT_LOCATION/mobile/src/test/java/$TMPL_PACKAGE_TLD"
rm -rf "$PROJECT_LOCATION/mobile/src/androidTest/java/$TMPL_PACKAGE_TLD"
rm -rf "$PROJECT_LOCATION/wear/src/main/java/$TMPL_PACKAGE_TLD"

rm -rf "$PROJECT_LOCATION/build"
rm -rf "$PROJECT_LOCATION/mobile/build"
rm -rf "$PROJECT_LOCATION/wear/build"
rm -rf "$PROJECT_LOCATION/.idea"
rm -f "$PROJECT_LOCATION/mobile/*.iml"
rm -f "$PROJECT_LOCATION/wear/*.iml"

echo "Bootstrapping completed, your project is now ready at $PROJECT_LOCATION"
echo
echo


