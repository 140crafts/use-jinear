import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import { useRouter } from "next/router";
import { useEffect } from "react";

export default function Home() {
  const authState = useTypedSelector(selectAuthState);
  const router = useRouter();

  useEffect(() => {
    if (authState == "NOT_LOGGED_IN") {
      router.replace("/login");
    } else if (authState == "LOGGED_IN") {
      router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [authState]);

  return null;
}
