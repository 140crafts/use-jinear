"use client";
import React, { createContext, useContext, useEffect, useState } from "react";
import Logger from "@/utils/logger";

interface OnInstallPromptEventProviderProps {
  children: React.ReactNode;
}

const OnInstallPromptEventContext = createContext<{
  onInstallPromptEvent: Event | null
}>({
  onInstallPromptEvent: null
});

export function useOnInstallPromptEvent() {
  const ctx = useContext(OnInstallPromptEventContext);
  return ctx.onInstallPromptEvent;
}

const logger = Logger("OnInstallPromptEventProvider");

const OnInstallPromptEventProvider: React.FC<OnInstallPromptEventProviderProps> = ({ children }) => {
  const [onInstallPromptEvent, setOnInstallPromptEvent] = useState<Event | null>(null);
  logger.log({ onInstallPromptEvent });

  useEffect(() => {
    window.addEventListener("beforeinstallprompt", (event) => {
      event.preventDefault();
      setOnInstallPromptEvent(event);
    });
  }, []);

  return <OnInstallPromptEventContext.Provider
    value={{ onInstallPromptEvent }}>
    {children}
  </OnInstallPromptEventContext.Provider>;
};

export default OnInstallPromptEventProvider;