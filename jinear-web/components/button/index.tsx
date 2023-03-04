import CircularProgress from "@mui/material/CircularProgress";
import cn from "classnames";
import Link from "next/link";
import React, { FC } from "react";
import styles from "./index.module.css";

export const ButtonVariants = {
  default: "default",
  filled: "filled",
  filled2: "filled2",
  contrast: "contrast",
  outline: "outline",
  hoverFilled: "hover-filled",
  hoverFilled2: "hover-filled2",
};

export const ButtonHeight = {
  default: "height-default",
  mid: "height-mid",
  short: "height-short",
};

interface LinkButtonProps {
  disabled?: boolean;
  children?: React.ReactNode;
  href: string;
  target: string;
  className?: string;
}

const LinkButton: FC<LinkButtonProps> = ({ href, children, ...props }) => {
  return (
    <Link href={href} passHref {...props}>
      {children}
    </Link>
  );
};

interface BaseButtonProps {
  children?: React.ReactNode;
  className?: string;
}

const BaseButton: FC<BaseButtonProps> = ({ children, ...props }) => {
  return (
    <button type="button" {...props}>
      {children}
    </button>
  );
};

interface ButtonProps {
  children?: React.ReactNode;
  variant?: string;
  heightVariant?: string;
  loading?: boolean;
  disabled?: boolean;
  withShadow?: boolean;
  href?: string;
  target?: string;
  progessClassname?: string;
  progessSize?: number;
  className?: string;
  onClick?: (event?: any) => void;
  onMouseDown?: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
  type?: string;
  form?: string;
  style?: any;
}

const Button: React.FC<ButtonProps> = ({
  variant = ButtonVariants.default,
  heightVariant = ButtonHeight.default,
  loading = false,
  disabled = false,
  href,
  target,
  progessClassname,
  progessSize = 15,
  className,
  children,
  ...props
}) => {
  const Comp = href ? LinkButton : BaseButton;
  return (
    // @ts-ignore
    <Comp
      {...props}
      disabled={disabled}
      href={href as string}
      target={target as string}
      className={cn(styles.button, styles?.[heightVariant], styles?.[variant], className)}
    >
      {loading ? (
        <CircularProgress
          className={cn([variant == "contrast" ? styles.progessContrast : styles.progess, progessClassname])}
          size={progessSize}
        />
      ) : (
        children
      )}
    </Comp>
  );
};

export default Button;
