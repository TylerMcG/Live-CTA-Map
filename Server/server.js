
const express = require("express");
const app = express();
const https = require("https");
const schedule = require('node-schedule');
const remove_train_interval = 20000;
// Import the functions you need from the SDKs you need
const { initializeApp } = require('firebase-admin/app');
const admin = require("firebase-admin");
const serviceAccount = require("./ServiceAccountKey.json");
admin.initializeApp({credential: admin.credential.cert(serviceAccount),
  databaseURL: process.env.DATABASE_URL,
  databaseAuthVariableOverride: { uid: process.env.FB_CRED}
});
const positionsBaseURL = process.env.CTA_KEY;
const serverURL = process.env.SERVER_KEY;
const db = admin.database();

const trainRoute7 = [
  "&rt=red", "&rt=blue", "&rt=brn", "&rt=G", "&rt=Org",
  "&rt=P", "&rt=Pink", "&rt=Y"];
const jsonOutput = "&outputType=JSON";
const trainRoute7Names = [
  "Red Line", "Blue Line", "Brown Line", "Green Line", "Orange Line",
  "Purple Line", "Pink Line", "Yellow Line"];

let port = process.env.PORT;
if(port == null || port == "") {
  port = 3000;
}

const serverRunning = schedule.scheduleJob("*/20 * * * *", function() {
  keepServerRunning();
});
//listening on port 3000 and heroku port
app.listen(port, ()=> {
  console.log("server started");
});

const redLineJob = schedule.scheduleJob("*/9 * * * * *", function() {
  callCTAUpdateDB(0);
});
const blueLineJob = schedule.scheduleJob("*/9 * * * * *", function() {
  callCTAUpdateDB(1);
});
const brownLineJob = schedule.scheduleJob("*/15 * * * * *", function() {
  scheduleTrainJob(4, 0, 2, 0, 2);
    // callCTAUpdateDB(2);
});
const greenLineJob = schedule.scheduleJob("*/15 * * * * *", function() {
  scheduleTrainJob(3, 50, 1, 10, 3);
    // callCTAUpdateDB(3);
});
const orangeLineJob = schedule.scheduleJob("*/14 * * * * *", function() {
  scheduleTrainJob(3, 30, 1, 30, 4)
  // callCTAUpdateDB(4);
});
const purpleLineJob = schedule.scheduleJob("*/14 * * * * *", function() {
  scheduleTrainJob(4, 20, 1, 45, 5);
  // callCTAUpdateDB(5);
});
const pinkLineJob = schedule.scheduleJob("*/15 * * * * *", function() {
  scheduleTrainJob(4, 0, 1, 30, 6);
  // callCTAUpdateDB(6);
});
const yellowLineJob = schedule.scheduleJob("*/16 * * * * *", function() {
  scheduleTrainJob(4, 45, 11, 20, 7);
  // callCTAUpdateDB(7);
});

//returns CTA API url for each train line
function getTrainRoute(index) {
  switch (index) {
    case 0:
      return new URL(positionsBaseURL + trainRoute7[0] + jsonOutput);
    case 1:
      return new URL(positionsBaseURL + trainRoute7[1] + jsonOutput);
    case 2:
      return new URL(positionsBaseURL + trainRoute7[2] + jsonOutput);
    case 3:
      return new URL(positionsBaseURL + trainRoute7[3] + jsonOutput);
    case 4:
      return new URL(positionsBaseURL + trainRoute7[4] + jsonOutput);
    case 5:
      return new URL(positionsBaseURL + trainRoute7[5] + jsonOutput);
    case 6:
      return new URL(positionsBaseURL + trainRoute7[6] + jsonOutput);
    case 7:
      return new URL(positionsBaseURL + trainRoute7[7] + jsonOutput);
    default:
      return new URL(positionsBaseURL + trainRoute7[0]) + jsonOutput;
  }
}

//needed to keep server active until plan is changedn on Heroku. This will run 24/7 but will run out of dyno hours after 23 days into every month.
//once the plan is changed to a paid plan, this can be removed.
function keepServerRunning(){
  try{
    const serverCall = https.request(serverURL, (response) => {
      console.log("Success!");
    });
    serverCall.end();
    console.log("Success!");
  } catch(error){
    console.log("Error ", error);
  }
}


//makes the actual API call, gets the data as JSON and uses data to update the Firebase Realtime Database.
 function callCTAUpdateDB(i){
    const trainRequest = https.request(getTrainRoute(i), (response) =>{
      let data = "";
      response.on("data", (chunk) => {
        data += chunk;
      });
      response.on("end", () => {
        try {
          const json = JSON.parse(data);
          updateTrainData(json, trainRoute7Names[i]);
        } catch (error) {
          console.error("Error writing data : " + error.message);
        }
      });
    }).on("error", (error) => {
      console.error("error making API call: " + error.message);
    });
    trainRequest.end();
}

//writes data to Realtime database
 function updateTrainData(trainLineData, trainColor) {
   //single train (duplicate code could be abstracted to method)
  try{
    var ref = db.ref(trainColor);
    removeOldTrains(ref);
      if (typeof trainLineData.ctatt.route[0].train.length == "undefined") {
        let train_child = ref.child("singleTrain");
        train_child.set({
          line: trainColor,
          rn: trainLineData.ctatt.route[0].train.rn,
          destSt: trainLineData.ctatt.route[0].train.destSt,
          destNm: trainLineData.ctatt.route[0].train.destNm,
          trDr: trainLineData.ctatt.route[0].train.trDr,
          nextStaId: trainLineData.ctatt.route[0].train.nextStaId,
          nextStpId: trainLineData.ctatt.route[0].train.nextStpId,
          nextStaNm: trainLineData.ctatt.route[0].train.nextStaNm,
          prdt: trainLineData.ctatt.route[0].train.prdt,
          arrt: trainLineData.ctatt.route[0].train.arrT,
          isApp: trainLineData.ctatt.route[0].train.isApp,
          isDly: trainLineData.ctatt.route[0].train.isDly,
          lat: trainLineData.ctatt.route[0].train.lat,
          lon: trainLineData.ctatt.route[0].train.lon,
          heading: trainLineData.ctatt.route[0].train.heading,
          time: Date.now()
        });
      }
      else { //multiple trains
        for (let i = 0; i < trainLineData.ctatt.route[0].train.length; i++) {
          let train_child = ref.child("train" + i);
          train_child.set({
            line: trainColor,
            rn: trainLineData.ctatt.route[0].train[i].rn,
            destSt: trainLineData.ctatt.route[0].train[i].destSt,
            destNm: trainLineData.ctatt.route[0].train[i].destNm,
            trDr: trainLineData.ctatt.route[0].train[i].trDr,
            nextStaId: trainLineData.ctatt.route[0].train[i].nextStaId,
            nextStpId: trainLineData.ctatt.route[0].train[i].nextStpId,
            nextStaNm: trainLineData.ctatt.route[0].train[i].nextStaNm,
            prdt: trainLineData.ctatt.route[0].train[i].prdt,
            arrt: trainLineData.ctatt.route[0].train[i].arrT,
            isApp: trainLineData.ctatt.route[0].train[i].isApp,
            isDly: trainLineData.ctatt.route[0].train[i].isDly,
            lat: trainLineData.ctatt.route[0].train[i].lat,
            lon: trainLineData.ctatt.route[0].train[i].lon,
            heading: trainLineData.ctatt.route[0].train[i].heading,
            time: Date.now()
          });
        }
      }
   }
   catch(error) {
     console.log("Train json error: " +  error);
   }
 }
//remove a train if it has not been updated in more than 20 seconds.
 function removeOldTrains(ref) {
   try {
     ref.on("value", function(snapshot) {
       snapshot.forEach(function(data) {
            if (data.val().time + remove_train_interval < Date.now()) {
              data.ref.remove();
            }
       });
     });
   }
   catch (error) {
     console.log("Remove Train error: " + error);
   }
 }

//runs callCTAUpdateDB for train if it is within the scheduled hours of operations
function scheduleTrainJob(startHour, startMins, endHour, endMins, trainIndex){
    if (checkHours(startHour, startMins, endHour, endMins)) {
      callCTAUpdateDB(trainIndex);
    }
 }
//checks to see if the current time is within the bounds of a trains hours of operations
 function checkHours(startHour, startMins, endHour, endMins) {
   today = new Date();
   todayHours = today.getHours();
   todayMins = today.getMinutes()/60;
   startMins = startMins/60;
   endMins = endMins/60;
   if((todayHours + todayMins) < (endHour + endMins)
   || (todayHours + todayMins) >= (startHour + startMins)) {
        return true;
   }
   return false;
 }
