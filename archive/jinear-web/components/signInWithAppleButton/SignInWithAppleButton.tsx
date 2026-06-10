"use client";
import Button, { ButtonVariants } from "@/components/button";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import Logger from "@/utils/logger";
import useTranslation from "@/locals/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import toast from "react-hot-toast";
import { IoLogoApple } from "react-icons/io5";
import { useLoginWithAppleMutation } from "@/api/authApi";
import { format } from "date-fns";
import { useFeatureFlag } from "@/hooks/useFeatureFlag";

const logger = Logger("SignInWithAppleButton");

declare global {
  interface Window {
    AppleID?: {
      auth: {
        init: (config: AppleSignInConfig) => void;
        signIn: () => Promise<AppleSignInResponse>;
      };
    };
  }
}

interface AppleSignInConfig {
  clientId: string;
  scope: string;
  redirectURI: string;
  state?: string;
  nonce?: string;
  usePopup?: boolean;
}

interface AppleSignInResponse {
  authorization: {
    code: string;
    id_token: string;
    state?: string;
  };
  user?: {
    email?: string;
    name?: {
      firstName?: string;
      lastName?: string;
    };
  };
}

interface SignInWithAppleButtonProps {
  className?: string;
  disabled?: boolean;
  iconClassName?: string;
}

const SignInWithAppleButton: React.FC<SignInWithAppleButtonProps> = ({
                                                                       className,
                                                                       iconClassName,
                                                                       disabled
                                                                     }) => {
  const { t } = useTranslation();
  const [loginWithApple, { isLoading, isSuccess, isError, error }] = useLoginWithAppleMutation();

  useEffect(() => {
    if (isError) {
      logger.log({ m: "Login with Apple failed", error });
      toast(t("loginErrorToast"));
    }
  }, [isError, error, t]);

  const handleAppleSignIn = async () => {
    if (!window.AppleID) {
      toast("Apple Sign In is not available");
      logger.log("Apple Sign In SDK not loaded");
      return;
    }
    try {
      logger.log("Starting Apple Sign In flow");
      const response = await window.AppleID.auth.signIn();
      logger.log({
        hasCode: !!response.authorization?.code,
        hasIdToken: !!response.authorization?.id_token,
        hasUser: !!response.user
      });

      // Send the authorization code and id_token to backend
      await loginWithApple({
        code: response.authorization.code,
        timeZone: format(new Date(), "OOOO")
      });
    } catch (err: any) {
      // User cancelled or error occurred
      if (err?.error !== "popup_closed_by_user") {
        logger.log({ m: "Apple Sign In error", err });
        toast(t("loginErrorToast"));
      } else {
        logger.log("User closed Apple Sign In popup");
      }
    }
  };

  return (
    <Button
      onClick={handleAppleSignIn}
      disabled={disabled || isLoading}
      loading={isLoading}
      variant={ButtonVariants.outline}
      className={className}
    >
      <IoLogoApple className={iconClassName} />
      <div>{t("loginScreenLoginWithApple")}</div>
    </Button>
  );
};

export default SignInWithAppleButton;
