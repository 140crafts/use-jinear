import { TeamWorkflowStateGroup } from "@/model/be/jinear-core";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";

const ICON_SIZE = 15;
const GROUP_ICON_MAP = {
  BACKLOG: IoPauseCircleOutline,
  NOT_STARTED: IoEllipseOutline,
  STARTED: IoContrast,
  COMPLETED: IoCheckmarkCircle,
  CANCELLED: IoCloseCircle,
};
export const retrieveTaskStatusIcon = (workflowStateGroup?: TeamWorkflowStateGroup) => {
  return workflowStateGroup ? GROUP_ICON_MAP?.[workflowStateGroup] || IoEllipseOutline : IoEllipseOutline;
};
