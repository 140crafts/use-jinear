import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import Breadcrumb from "../breadcrumb/Breadcrumb";
import BreadcrumbLink from "../breadcrumb/BreadcrumbLink";

interface TaskDetailBreadcrumbProps {
  task: TaskDto;
}

const TaskDetailBreadcrumb: React.FC<TaskDetailBreadcrumbProps> = ({ task }) => {
  const workspaceTitle = task.workspace?.title || "";
  const workspaceUsername = task.workspace?.username || "";
  const teamName = task.team?.name || "";
  const teamUsername = task.team?.username || "";

  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspaceTitle} url={`/${workspaceUsername}`} />
      <BreadcrumbLink label={teamName} url={`/${workspaceUsername}/${teamUsername}/weekly`} />
      <BreadcrumbLink
        label={`${task.team?.tag}-${task.teamTagNo}` || ""}
        url={`/${workspaceUsername}/task/${task.team?.tag}-${task.teamTagNo}`}
      />
    </Breadcrumb>
  );
};

export default TaskDetailBreadcrumb;
