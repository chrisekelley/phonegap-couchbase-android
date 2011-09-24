## Kudos to Kevin Malakoff

I'm using the android portion of the code from [phonegap-couchbase-xplatform](https://github.com/kmalakoff/phonegap-couchbase-xplatform). 
Android Couchapps that use views currently do not work in the emulator, but they do work on the device:
 
https://groups.google.com/d/msg/mobile-couchbase/Lk_1eafr1pI/wSdv1YkSoYIJ

## Test app

This test app uses CouchDB and PhoneGap. The couchapp is using backbone.js for MVC framework.

## How It Works

Here is my current process. Run couchpack on the docs you want to include:
 
    couchpack document http://localhost:5984/odk/_design/render odk
    couchpack document http://localhost:5984/odk/ArrestDocket ad
    couchpack document http://localhost:5984/odk/PatientRegistration pr
 
It strips the id's and revs, and also creates a version id that can be used for version tracking. 
Kevin Malakoff's system creates a "shadow" document that has this version, I think, that is used for tracking freshness.
 
I would like a way to slurp the whole db into a single document. 
I tried couchapp push -export, but it did not quite have all of the magic sauce. 
But I may revisit that when I have a chance. 
But the good thing about the current manual method is that it does ensure a clean database.
 
The loading of these docs is currently hard-coded in the Java code:
 
    couchMover.loadDocumentFromAssetManager(getAssets(), "_design/render", "odk.json", "odk.version");
    couchMover.loadDocumentFromAssetManager(getAssets(), "ArrestDocket", "ad.json", "ad.version");
    couchMover.loadDocumentFromAssetManager(getAssets(), "PatientRegistration", "pr.json", "pr.version");
 
https://github.com/vetula/phonegap-couchbase-android/blob/master/src/com/phonegap/PhonegapCouchbaseAndroid/PhonegapCouchbaseAndroid.java

## Additions

* I added some code from Mobile Futon to set the app port to 5985 to make debugging a little easier. 
* Also added Mobile Futon to the app to view the CouchDB and also facilitate viewing the app as a CouchApp in Chrome.

## Next Steps
Next step is to code the app to look in assets/loadme directory and loop through the files. 
Get the document name from the filename. I would change how I'm handling the filename output by couchpack:
 
couchpack document http://localhost:5984/odk/ArrestDocket ArrestDocket

## Issues

The app installs and runs fine in the three Android devices tested; however, there are issues possibly caused by browser compatability issues. 
This project's issue tracker tracks Android compatability issues in [Issue 1](https://github.com/vetula/phonegap-couchbase-android/issues/1).
