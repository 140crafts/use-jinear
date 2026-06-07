export interface TaskAdditionalDataMap {
  [taskId: string]: TaskAdditionalData;
}

export interface TaskAdditionalData {
  hasUpdates: boolean;
}
