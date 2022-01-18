# Chicago Transit Authority L Train Live Map Android Application


**ABOUT THIS PROJECT**
Working on switching this from a school project to an actual releasable Android Application, 
so code related to school project has been removed, original version is its own repo and set to private for sercurity purposes.

# HOW TO USE
**Requirements:** 
A google firebase project will need to be created, and a ServiceAccountKey.json will need to be included in the Server directory in order to access the Firebase database
from the nodejs server. the firebase database url can be included in the .env file in the Server directory, or replace process.env.DATABASE_URL with the firebase database 
url in the server.js file (line 12). 
A CTA Key will need to be obtains from the CTA, which can be found at https://www.transitchicago.com/developers/traintrackerapply/

the posisitionBaseURL in the server.js file can be replaced with https://lapi.transitchicago.com/api/1.0/ttpositions.aspx?key="your api key goes here" (line 15)

the serverURL is the nodejs server, so it can be replaced with localhost, or the server url if hosted non-locally. Server is hosted by Heroku so thats why this is included to
keep the server running 24/7 under the current pricing plan. 

Requires node, node-schedule, firebase, express, firebase-admin and will need to be installed using npm install.

In the android app, in local.settings include a MAPS_API_KEY="your google maps api key goes here" to get the google maps sdk to work properly. 
more information can be found at https://developers.google.com/maps/documentation/android-sdk/start
include a google-services.json in the project folder that allows a connection to the Firebase database, more information can be found at https://firebase.google.com/docs/android/setup

Firebase rules:
implement your own signin method in the android application and change the rules in the firebase database in the rules tab. Currently, the project allows any anonoymously signed in android user in the application to read the entire database, since the data is publically provided from the CTA this seems okay. Only the web server is allowed to write to the database, so the example rules currently look like this:
{
  "rules": {
    ".read": "auth.uid !== null",
    ".write": "auth.uid === 'some authorized id to write to db"
  }
}
this could be set to .read: = true and .write = true for testing purposes. 
# Changes from School Project

**Updated UI:**

The Bottom Navigation bar toggled between two views: a map view and a webpage view. The map view shows a map with all the train routes in color, and optionally displays trains for each route and all train stops with a button press The show trains button loads a fragment on top of the map with a transparent background and has a toggle button for each train line to toggle between add/show and remove/removefor the database listener/UI component. When the button is pressed it toggles between the two states mentioned. Show stops button toggle displays all the L train stops from a KML file on the map. The Alerts button loads a webview going to the CTA website with alerts for down trains. The whole CTA website and links from their page can be accessed through this web view.

**Updated Server:**

Switched from MongoDb to Google Firebase Realtime Database Model

Implemented a job scheduler to only schedule making API requests for certain trains based on their hours of operation, adding some optimization to scheduled calls.

Since the Red/Blue lines service more stops and run 24/7, more weight was given to their update interval and make more frequent API calls.

Job scheduler for all other lines checks to see if the train is in the hours of operation every X seconds, and only makes API calls if the trains are running based on check

Job Scheduler currently makes self request to server to keep it active 24/7.

Added a method to remove trains no longer active from the database if they have not been written to in 20 seconds since last write.

# **Working On**

**Updating UI and Features:** color schemes, adding dark mode, adjusting transparency in show trains frame, adjust text fields, fonts, and layout in marker window adapter

add fragment for when a user cannot login, and method to send email to contact me to fix problem in this instance

add language and accessibility support.

Switch to paid model to keep server running 24/7 instead of running out of free hours after 23 days of constant uptime.

Add more details for each train, similiar to options found by clicking on a train on transitchicago.com/traintrackermap. This will require more API calls and will need
permission from CTA due to exceeding 50,000 daily limit to keep information accurate

**Comments:** Need to adjust and add more comments on recent commit

**Testing:** Need to create proper testing suite instead of test-as-I-go approach REALLY NEED TO START CREATING TEST CLASSES AS I CODE!!

**Refactor:** Need to move some code out of Main Activity and into FragmentUtilityLoader

**Optimization** and Hardening: Need to try and reduce CPU load, remove unused packages, reduce size, add fault tolerance where needed
