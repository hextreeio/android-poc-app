# Proof of Concept App

This is a simple Android app based on the "Empty Views Activity". It includes a few useful Utils 
classes and a basic layout with a button and debug text output ready to go.

**How to use:**
 - File > New > Project from Version Control...
 - Repository: https://github.com/hextreeio/android-poc-app
 - Implement your attack

# Troubleshooting

## ActivityNotFoundException

If you encounter the following error when trying to start an activity you know exists, try to add a `<queries>` tag to the Android manifest.
```
android.content.ActivityNotFoundException: Unable to find explicit activity class {...}; have you declared this activity in your AndroidManifest.xml, or does your intent not match its declared <intent-filter>?
```
                                                                                                    
```xml
<queries>
    <package android:name="<package_name>" />
</queries>
```