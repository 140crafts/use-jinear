"use client";
import cn from "classnames";
import React from "react";
import { IoMoon, IoMoonOutline } from "react-icons/io5";
import Button, { ButtonVariants } from "../button";
import ClientOnly from "../clientOnly/ClientOnly";
import { useTheme, useThemeToggle } from "../themeProvider/ThemeProvider";
import styles from "./ThemeToggle.module.css";

interface ThemeToggleProps {
  variant?: keyof typeof ButtonVariants | string;
  iconSize?: number;
  buttonStyle?: string;
}

const ThemeToggle: React.FC<ThemeToggleProps> = ({ variant = ButtonVariants.default, buttonStyle, iconSize = 13 }) => {
  const theme = useTheme();
  const toggleTheme = useThemeToggle();
  return (
    <ClientOnly>
      <Button variant={variant} onClick={toggleTheme} className={cn(styles.iconButton, buttonStyle)}>
        {theme == "dark" ? <IoMoonOutline size={iconSize} /> : <IoMoon size={iconSize} />}
      </Button>
    </ClientOnly>
  );
};

export default ThemeToggle;
