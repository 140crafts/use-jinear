import { API_ROOT } from "@/utils/constants";
import React from "react";
import { SSEProvider } from "react-hooks-sse";

interface SseProviderWorkspaceActivitiesProps {
  workspaceId?: string;
  children: React.ReactNode;
}

const SseProviderWorkspaceActivities: React.FC<SseProviderWorkspaceActivitiesProps> = ({ workspaceId, children }) => {
  const getSource = () => {
    const url = `${API_ROOT}v1/sse/workspace/activity/${workspaceId}`;
    return new EventSource(url, { withCredentials: true });
  };

  return workspaceId ? <SSEProvider source={getSource}>{children}</SSEProvider> : <>{children}</>;
};

export default SseProviderWorkspaceActivities;
