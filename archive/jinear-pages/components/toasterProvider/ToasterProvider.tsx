"use client";
import React from "react";
import { Toaster } from "react-hot-toast";

interface ToasterProviderProps {}

const ToasterProvider: React.FC<ToasterProviderProps> = ({}) => {
  return <Toaster position="bottom-center" containerStyle={{ bottom: 68 }} toastOptions={{ className: "toast" }} />;
};

export default ToasterProvider;
