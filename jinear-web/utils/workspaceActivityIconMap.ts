import { WorkspaceActivityType } from "@/model/be/jinear-core";
import {
  IoCalendar,
  IoCaretForward,
  IoCheckmark,
  IoCog,
  IoPencil,
  IoPersonAdd,
  IoPersonRemove,
  IoPlayForward,
  IoPricetag,
  IoShield,
  IoSparkles,
} from "react-icons/io5";

const workspaceIconMap = {
  MEMBER_JOIN: IoPersonAdd,
  MEMBER_LEFT: IoPersonRemove,
  MEMBER_REMOVED: IoPersonRemove,
  MEMBER_REQUESTED_ACCESS: IoShield,
  TASK_INITIALIZED: IoSparkles,
  TASK_CLOSED: IoCheckmark,
  EDIT_TASK_TITLE: IoPencil,
  EDIT_TASK_DESC: IoPencil,
  TASK_UPDATE_TOPIC: IoPricetag,
  TASK_UPDATE_WORKFLOW_STATUS: IoCog,
  TASK_CHANGE_ASSIGNEE: IoPlayForward,
  TASK_CHANGE_ASSIGNED_DATE: IoCalendar,
  TASK_CHANGE_DUE_DATE: IoCalendar,
  RELATION_INITIALIZED: IoSparkles,
};

const decideWorkspaceActivityIcon = (type: WorkspaceActivityType) => {
  return workspaceIconMap[type] || IoCaretForward;
};

export default decideWorkspaceActivityIcon;
