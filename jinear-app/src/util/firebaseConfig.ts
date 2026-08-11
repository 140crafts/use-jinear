// Runtime-configurable via env (see .env.example). The same values must be used
// by the service worker (src/sw.ts) so app and background push bind to one
// Firebase project. Self-hosters override these without rebuilding the image.
//
// Kept out of FirebaseConfigration.tsx so the permission modal can read the VAPID
// key without importing the component (which imports the modal back).
export const firebaseConfig = {
    apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
    authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
    projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID,
    storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID,
    appId: import.meta.env.VITE_FIREBASE_APP_ID,
    measurementId: import.meta.env.VITE_FIREBASE_MEASUREMENT_ID
};

export const VAPID_PUBLIC_KEY = import.meta.env.VITE_FIREBASE_VAPID_KEY;
