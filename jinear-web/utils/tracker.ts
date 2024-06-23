import { __DEV__ } from "@/utils/constants";

const DEV_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMWoxMjV2ZmVmbm0yMTk2c3h4YnJnemFhNiIsImlzX3JvYm90Ijp0cnVlLCJleHAiOjQ4NzI3Mzc5OTAsImlhdCI6MTcxOTEzNzk5MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ST0JPVCJdfQ.BZYk_Cnd5cvDIBS94qY4E_KAJHyUAlZMmJ4eG65hMjPuG3RlC0U-q5PdYYnwsPay5PS7xSWcWPSTYgXIJrjznQ";
const PROD_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMWoxM2FjMmtjbmFyZTJhZGhxNjR0NnE5NyIsImlzX3JvYm90Ijp0cnVlLCJleHAiOjQ4NzI3NzIyMDUsImlhdCI6MTcxOTE3MjIwNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ST0JPVCJdfQ.Hp4rcSk-D_zgMAKx0GpRaMJhuid-WnIwMiMunVnZ8vJTaSshVJYSkO3kDtTkgb5UDpAkoDznJ8hBDveB386aYQ";
const token = __DEV__ ? DEV_TOKEN : PROD_TOKEN;

const xThreadId_PROD = "01j12av4c241vtmt5ps6dqw8ch";
const xThreadId_DEV = "01j12av4c241vtmt5ps6dqw8ch";
const xThreadId = __DEV__ ? xThreadId_DEV : xThreadId_PROD;

const root = __DEV__ ? "http://localhost:8085/" : "https://api.jinear.co/";

export const trackWaitlist = ({ message }: { message: string }) => {
  const headers = new Headers();
  headers.append("Content-Type", "application/json");
  headers.append("X-THREAD-ID", xThreadId);
  headers.append("Authorization", `Bearer ${token}`);

  const body = JSON.stringify({
    "body": `${message}`
  });

  fetch(`${root}v1/robots/messaging/message/operation/thread`,
    {
      method: "POST",
      headers,
      body
    })
    .then((response) => response.json())
    .then((result) => console.log(result))
    .catch((error) => console.error(error));
};