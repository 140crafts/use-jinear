import Head from "next/head";
import React from "react";

interface TitleHandlerProps {}

const TitleHandler: React.FC<TitleHandlerProps> = ({}) => {
  return (
    <Head>
      <title>Jinear</title>
    </Head>
  );
};

export default TitleHandler;
