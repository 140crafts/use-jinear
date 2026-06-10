import { APPLE_CLIENT_ID, APPLE_REDIRECT_URI } from "@/util/constants";
import { initPaddle } from "@/util/paddle";
import Logger from "@/util/logger";
import React, { useEffect } from "react";

const logger = Logger("Scripts");

function loadScript(src: string, onLoad: () => void): () => void {
    let script = document.querySelector<HTMLScriptElement>(`script[src="${src}"]`);

    if (script) {
        if (script.dataset.loaded === "true") {
            onLoad();
        } else {
            script.addEventListener("load", onLoad);
        }
        return () => script?.removeEventListener("load", onLoad);
    }

    script = document.createElement("script");
    script.src = src;
    script.async = true;
    const handleLoad = () => {
        script!.dataset.loaded = "true";
        onLoad();
    };
    script.addEventListener("load", handleLoad);
    document.body.appendChild(script);

    return () => script?.removeEventListener("load", handleLoad);
}

const Scripts: React.FC = () => {
    useEffect(() => {
        initPaddle();
    }, []);

    useEffect(() => {
        return loadScript(
            "https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js",
            () => {
                try {
                    if (window.AppleID && APPLE_REDIRECT_URI && APPLE_CLIENT_ID) {
                        window.AppleID.auth.init({
                            clientId: APPLE_CLIENT_ID,
                            scope: "name email",
                            redirectURI: APPLE_REDIRECT_URI,
                            usePopup: true,
                        });
                        logger.log("Apple Sign In initialized globally");
                    }
                } catch (err) {
                    logger.log({ m: "Failed to initialize Apple Sign In", err });
                }
            }
        );
    }, []);

    return null;
};

export default Scripts;