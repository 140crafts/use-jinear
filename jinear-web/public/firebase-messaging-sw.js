importScripts("https://www.gstatic.com/firebasejs/10.1.0/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/10.1.0/firebase-messaging-compat.js");

const firebaseConfig = {
  apiKey: "AIzaSyBZq8Pg2pDDweDSNqTwdrCR-xBe1mJGBco",
  authDomain: "jinear-f3ab4.firebaseapp.com",
  projectId: "jinear-f3ab4",
  storageBucket: "jinear-f3ab4.appspot.com",
  messagingSenderId: "72155538781",
  appId: "1:72155538781:web:767cb1558cd358cfacf4b4",
  measurementId: "G-FMXGQ5XM95",
};

let firebaseApp = firebase.initializeApp(firebaseConfig);
let messaging = firebase.messaging.isSupported() ? firebase.messaging() : null;

// messaging.onBackgroundMessage((payload) => {
//   console.log("[firebase-messaging-sw.js] Received background message ", payload);
//   // const notificationOptions = {
//   //   ...payload.notification,
//   // };

//   // self.registration.showNotification(notificationOptions.title, notificationOptions);
// });
