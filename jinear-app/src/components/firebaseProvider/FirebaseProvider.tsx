import React from 'react';
import ErrorBoundary from "@/components/error-boundary/ErrorBoundary.tsx";
import FirebaseConfigration from "@/components/firebaseConfiguration/FirebaseConfigration.tsx";


interface FirebaseProviderProps {

}

const FirebaseProvider: React.FC<FirebaseProviderProps> = ({}) => {
    return (
        <ErrorBoundary message={"Firebase Configuration Failed"}>
            <FirebaseConfigration/>
        </ErrorBoundary>
    );
}

export default FirebaseProvider;