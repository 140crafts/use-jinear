import { WorkspaceActivityType } from "@/model/be/jinear-core";
import {
  IoCalendarOutline,
  IoCaretForward,
  IoChatboxOutline,
  IoCheckmarkOutline,
  IoCloseOutline,
  IoCogOutline,
  IoDocumentAttachOutline,
  IoPencilOutline,
  IoPersonAddOutline,
  IoPersonRemoveOutline,
  IoPlayForwardOutline,
  IoPricetagOutline,
  IoReaderOutline,
  IoShieldOutline,
  IoSparklesOutline,
  IoTrashBinOutline,
} from "react-icons/io5";

const workspaceIconMap = {
  MEMBER_JOIN: IoPersonAddOutline,
  MEMBER_LEFT: IoPersonRemoveOutline,
  MEMBER_REMOVED: IoPersonRemoveOutline,
  MEMBER_REQUESTED_ACCESS: IoShieldOutline,
  TASK_INITIALIZED: IoSparklesOutline,
  TASK_CLOSED: IoCheckmarkOutline,
  EDIT_TASK_TITLE: IoPencilOutline,
  EDIT_TASK_DESC: IoPencilOutline,
  TASK_UPDATE_TOPIC: IoPricetagOutline,
  TASK_UPDATE_WORKFLOW_STATUS: IoCogOutline,
  TASK_CHANGE_ASSIGNEE: IoPlayForwardOutline,
  TASK_CHANGE_ASSIGNED_DATE: IoCalendarOutline,
  TASK_CHANGE_DUE_DATE: IoCalendarOutline,
  RELATION_INITIALIZED: IoSparklesOutline,
  RELATION_REMOVED: IoCloseOutline,
  CHECKLIST_INITIALIZED: IoCheckmarkOutline,
  CHECKLIST_REMOVED: IoCheckmarkOutline,
  CHECKLIST_TITLE_CHANGED: IoCheckmarkOutline,
  CHECKLIST_ITEM_CHECKED_STATUS_CHANGED: IoCheckmarkOutline,
  CHECKLIST_ITEM_LABEL_CHANGED: IoCheckmarkOutline,
  CHECKLIST_ITEM_REMOVED: IoCheckmarkOutline,
  CHECKLIST_ITEM_INITIALIZED: IoCheckmarkOutline,
  ATTACHMENT_ADDED: IoDocumentAttachOutline,
  ATTACHMENT_DELETED: IoTrashBinOutline,
  TASK_BOARD_ENTRY_INIT: IoReaderOutline,
  TASK_BOARD_ENTRY_REMOVED: IoReaderOutline,
  TASK_BOARD_ENTRY_ORDER_CHANGE: IoReaderOutline,
  TASK_NEW_COMMENT: IoChatboxOutline,
};

const decideWorkspaceActivityIcon = (type: WorkspaceActivityType) => {
  return workspaceIconMap[type] || IoCaretForward;
};

export default decideWorkspaceActivityIcon;
