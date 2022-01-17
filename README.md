# Chicago Transit Authority L Train Live Map Android Application


**ABOUT THIS PROJECT**

Working on switching this from a school project to an actual releasable Android Application, so code related to school project has been removed in this version, other version is set to private for sercurity purposes.

**Updated UI:**

The Bottom Navigation bar toggled between two views: a map view and a webpage view. The map view shows a map with all the train routes in color, and optionally displays trains for each route and all train stops with a button press The show trains button loads a fragment on top of the map with a transparent background and has a toggle button for each train line to toggle between add/show and remove/removefor the database listener/UI component. When the button is pressed it toggles between the two states mentioned. Show stops button toggle displays all the L train stops from a KML file on the map. The Alerts button loads a webview going to the CTA website with alerts for down trains. The whole CTA website and links from their page can be accessed through this web view.

**Updated Server:**

Switched from MongoDb to Google Firebase Realtime Database Model

Implemented a job scheduler to only schedule making API requests for certain trains based on their hours of operation, adding some optimization to scheduled calls.

Since the Red/Blue lines service more stops and run 24/7, more weight was given to their update interval and make more frequent API calls.

Job scheduler for all other lines checks to see if the train is in the hours of operation every X seconds, and only makes API calls if the trains are running based on check

Job Scheduler currently makes self request to server to keep it active 24/7.

Added a method to remove trains no longer active from the database if they have not been written to in 20 seconds since last write.

**Working On**

**Updating UI and Features:** color schemes, adding dark mode, adjusting transparency in show trains frame, adjust text fields, fonts, and layout in marker window adapter

add fragment for when a user cannot login, and method to send email to contact me to fix problem in this instance

add language and accessibility support.

Switch to paid model to keep server running 24/7 instead of running out of free hours after 23 days of running 24/7.

**Comments:** Need to adjust and add more comments on recent commit

**Testing:** Need to create proper testing suite instead of test-as-I-go approach REALLY NEED TO START CREATING TEST CLASSES AS I CODE!!

**Refactor:** Need to move some code out of Main Activity and into FragmentUtilityLoader

**Optimization** and Hardening: Need to try and reduce CPU load, remove unused packages, reduce size, add fault tolerance where needed
