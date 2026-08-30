```
CLAUDE.md will load automatically

---

## Task
Managing team workflow statuses, create, remove, rename and change order of team workflow statuses

### Goal
Currently there's pre defined set of workflow statuses exists and they can be renamed. This is managed under 
TeamWorkflowSettings ([workspaceName]/tasks/[teamName]/settings). Backend endpoints must be ready under 
TeamWorkflowStatusController on jinear-core and corresponding query calls is probably added to teamWorkflowStatusApi on jinear-app.
Status order must be changed with dragging. Do not use any library keep it simple with native html drag and drop.

### Scope
jinear-app

### Requirements
- User should define new workflow statuses under any status group
- User should change statuses order within same group.
- User should delete workflow status. (Allow if workflow status group has more than one status. Last status of a group couldn't be deleted. Backend has that control.)

### Reference files
-

### Instructions
Start in planning mode. Read the reference files, map out changes per project, and present the plan before writing code.
```