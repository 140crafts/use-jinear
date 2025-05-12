"use client";
import React, { useEffect } from "react";
import { useRetrievePublicProjectInfoWithDomainQuery } from "@/api/projectFeedApi";

interface DynamicTitleUpdateHandlerProps {

}

const DynamicTitleUpdateHandler: React.FC<DynamicTitleUpdateHandlerProps> = ({}) => {
  const domain = typeof window == "object" && window?.location?.hostname || "";

  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoWithDomainQuery({ domain }, { skip: domain == "" });

  useEffect(() => {
    document.title = retrievePublicProjectResponse?.data?.title || "Project";
    var link = document.querySelector("link[rel~='icon']");
    if (!link) {
      link = document.createElement("link");
      // @ts-ignore
      link.rel = "icon";
      document.head.appendChild(link);
    }
    // @ts-ignore
    link.href = retrievePublicProjectResponse?.data.logo?.url;
  }, [retrievePublicProjectResponse]);

  return null;
};

export default DynamicTitleUpdateHandler;