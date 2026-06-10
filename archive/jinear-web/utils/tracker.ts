"use client";
import { env } from "next-runtime-env";
import { __DEV__, API_ROOT } from "@/utils/constants";

const xThreadId_PROD = "01j13amkhshfacj6z15pcjvnt0";
const xThreadId_DEV = "01j12av4c241vtmt5ps6dqw8ch";
const xThreadId = __DEV__ ? xThreadId_DEV : xThreadId_PROD;

export const trackWaitlist = ({ message }: { message: string }) => {
  const token = env("NEXT_PUBLIC_SUGGEST_TOKEN")
  const headers = new Headers();
  headers.append("Content-Type", "application/json");
  headers.append("X-THREAD-ID", xThreadId);
  headers.append("Authorization", `Bearer ${token}`);

  const body = JSON.stringify({
    "body": `${message}`
  });

  fetch(`${API_ROOT}v1/robots/messaging/message/operation/thread`,
    {
      method: "POST",
      headers,
      body
    })
    .then((response) => response.json())
    .then((result) => console.log(result))
    .catch((error) => console.error(error));
};

