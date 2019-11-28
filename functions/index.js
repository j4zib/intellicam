//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


// Listens for new messages added to users/pushId
exports.pushNotification = functions.firestore.document('users/{userId}').onWrite((change, context) => {


    //  Grab the current value of what was written to the Realtime Database.
    const newValue = change.after.data();
    var spa = String(newValue.spam);
    console.log(String(newValue.spam));
    const payload = {
        data: {
            name: newValue.name,
            id:newValue.id,
            spam: spa,
            sound: "default"
        },
    };

    //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


    return admin.messaging().sendToTopic("pushNotifications", payload, options);
});
