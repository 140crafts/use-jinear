import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import Breadcrumb from "../breadcrumb/Breadcrumb";
import BreadcrumbLink from "../breadcrumb/BreadcrumbLink";

interface TaskDetailBreadcrumbProps {
  task: TaskDto;
}

const TaskDetailBreadcrumb: React.FC<TaskDetailBreadcrumbProps> = ({
  task,
}) => {
  const workspaceUsername = task.workspace?.username || "";
  const workspaceUsernameEncoded = encodeURI(workspaceUsername);
  const teamName = task.team?.name || "";
  const teamNameEncoded = encodeURI(teamName);

  return (
    <Breadcrumb>
      <BreadcrumbLink
        label={workspaceUsername}
        url={`/${workspaceUsernameEncoded}`}
      />
      <BreadcrumbLink
        label={teamName}
        url={`/${workspaceUsernameEncoded}/${teamNameEncoded}/weekly`}
      />
      <BreadcrumbLink
        label={`${task.team?.tag}-${task.teamTagNo}` || ""}
        url={`/${workspaceUsernameEncoded}/task/${task.team?.tag}-${task.teamTagNo}`}
      />
    </Breadcrumb>
  );
};

export default TaskDetailBreadcrumb;
