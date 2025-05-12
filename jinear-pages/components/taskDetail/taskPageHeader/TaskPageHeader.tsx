import Head from "next/head";
import React from "react";

interface TaskPageHeaderProps {
  taskTag: string;
  title: string;
}

const TaskPageHeader: React.FC<TaskPageHeaderProps> = ({ taskTag, title }) => {
  return (
    <Head>
      <title>{`[${taskTag}] ${title}`}</title>
    </Head>
  );
};

export default TaskPageHeader;
