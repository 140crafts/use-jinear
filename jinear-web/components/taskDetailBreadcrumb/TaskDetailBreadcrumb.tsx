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
  return (
    <Breadcrumb>
      <BreadcrumbLink
        label={task.workspace?.title || ""}
        url={`/${task.workspace?.username}`}
      />
      <BreadcrumbLink
        label={task.team?.name || ""}
        url={`/${task.workspace?.username}/${task.team?.name}/weekly`}
      />
      <BreadcrumbLink
        label={`${task.team?.tag}-${task.teamTagNo}` || ""}
        url={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
      />
    </Breadcrumb>
  );
};

export default TaskDetailBreadcrumb;
