import { useTheme } from "@/components/themeProvider/ThemeProvider";
import { FeedItemMessage } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import React, { RefObject, useEffect, useRef, useState } from "react";
import styles from "./MessageContent.module.css";

interface MessageContentProps {
  message: FeedItemMessage;
}

const htmlMimeType = "text/html";
const plainMimeType = "text/plain";

type PreferedContentType = "HTML_PART" | "BODY" | "PLAIN_PART";

const updateLinkTargets = (iframeRef: RefObject<HTMLIFrameElement>) => {
  const links = iframeRef.current?.contentWindow?.document.getElementsByTagName("a") || [];
  for (let i = 0; i < links.length; i++) {
    links[i].target = "_blank";
  }
};

const updateStyles = (iframeRef: RefObject<HTMLIFrameElement>) => {
  const primaryColor = getComputedStyle(document.body).getPropertyValue("--c-primary");
  const secondaryColor = getComputedStyle(document.body).getPropertyValue("--c-secondary");
  const linkColor = getComputedStyle(document.body).getPropertyValue("--c-link-color");
  const fade = getComputedStyle(document.body).getPropertyValue("--fade");
  if (iframeRef.current?.contentWindow) {
    let styles = `
        body { 
          transition: all ${fade};
          color: ${secondaryColor};
          background-color: ${primaryColor};
          font-size: 0.85rem;
       }`;
    styles += `a { color: ${linkColor} }`;
    const styleEl = document.createElement("style");
    styleEl?.appendChild?.(document.createTextNode(styles));
    iframeRef.current?.contentWindow?.document?.head?.appendChild?.(styleEl);
  }
  const elements = iframeRef.current?.contentWindow?.document.body.getElementsByTagName("*") || [];
  //   const elements = iframeRef.current?.contentWindow?.document.querySelectorAll("body *") || [];
  for (let i = 0; i < elements.length; i++) {
    const element: any = elements[i];
    element.style.color = secondaryColor;
    if (element.style && element.style.backgroundColor && !element.style.background) {
      element.style.backgroundColor = primaryColor;
    }
  }
};

const logger = Logger("MessageContent");

const MessageContent: React.FC<MessageContentProps> = ({ message }) => {
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const htmlPart = message.detailDataList?.filter((messageData) => htmlMimeType == messageData.mimeType)?.[0];
  const plainPart = message.detailDataList?.filter((messageData) => plainMimeType == messageData.mimeType)?.[0];
  const body = message.body;
  const preferedContent = htmlPart?.data ? htmlPart.data : body ? body : plainPart?.data ? plainPart.data : undefined;
  const preferedContentType = htmlPart?.data ? "HTML_PART" : body ? "BODY" : plainPart?.data ? "PLAIN_PART" : undefined;
  const [frameHeight, setFrameHeight] = useState(0);
  const theme = useTheme();

  useEffect(() => {
    onFrameLoad();
  }, [theme]);

  const onFrameLoad = () => {
    setTimeout(() => {
      if (iframeRef.current) {
        const iframeWindow = iframeRef.current?.contentWindow;
        const iframeDocument = iframeWindow?.document;
        const el = iframeDocument?.querySelector?.("html");

        const styles = el && iframeWindow?.getComputedStyle?.(el);
        const margin = styles && parseFloat(styles?.["marginTop"]) + parseFloat(styles?.["marginBottom"]);
        const elHeight = Math.max(el?.scrollHeight || 0, el?.offsetHeight || 0);
        const height = Math.ceil((elHeight || 0) + (margin || 0)) + 5;
        setFrameHeight(height);

        updateLinkTargets(iframeRef);
        // todo change to hsl based color change
        // updateStyles(iframeRef);
        // tag in icindeki text uzunsa wbr ile bol (td,div de gerek yok pre de var)
        //BU GEREKMEYEBILIR WBR ILE todo change max-width or width if (max-width or width)+padding+margin > device_width- Modal_body // Ayrica styles daki width leri de gezmek lazim
      }
    }, 250);
  };

  logger.log({ preferedContentType });

  return (
    <div className={styles.container}>
      {preferedContent && (
        <iframe
          ref={iframeRef}
          srcDoc={preferedContent}
          onLoad={onFrameLoad}
          height={frameHeight}
          width="100%"
          className={styles.iframe}
        />
      )}
    </div>
  );
};

export default MessageContent;
