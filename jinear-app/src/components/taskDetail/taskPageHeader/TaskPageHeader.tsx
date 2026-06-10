import { useEffect } from "react";

interface TaskPageHeaderProps {
  taskTag: string;
  title: string;
}

const TaskPageHeader: React.FC<TaskPageHeaderProps> = ({ taskTag, title }) => {
  useEffect(() => {
    const previousTitle = document.title;
    document.title = `[${taskTag}] ${title}`;

    return () => {
      document.title = previousTitle;
    };
  }, [taskTag, title]);

  return null;
};

export default TaskPageHeader;