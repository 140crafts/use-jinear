import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import { useEffect } from "react";

export default function Home() {
  const authState = useTypedSelector(selectAuthState);
  const router = useRouter();

  useEffect(() => {
    if (authState == "NOT_LOGGED_IN") {
      router.replace("/login");
    } else if (authState == "LOGGED_IN") {
      router.replace("/");
    }
  }, [authState]);

  return null;
}
