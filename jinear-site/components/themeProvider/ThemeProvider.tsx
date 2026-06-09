"use client";
import React, { createContext, useContext, useEffect, useState } from "react";

type Theme = "dark" | "light";

const getInitialTheme = (): Theme => {
  if (typeof window === "object") {
    const stored = localStorage.getItem("THEME");
    if (stored === "dark" || stored === "light") return stored;
    const prefersDark = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
    return prefersDark ? "dark" : "light";
  }
  return "dark";
};

const ThemeContext = createContext<{ theme: Theme; toggleTheme: () => void }>({
  theme: "dark",
  toggleTheme: () => {},
});

export function useTheme() {
  return useContext(ThemeContext).theme;
}

export function useThemeToggle() {
  return useContext(ThemeContext).toggleTheme;
}

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [theme, setTheme] = useState<Theme>("dark");

  // Resolve the real theme on mount (avoids SSR/CSR hydration mismatch).
  useEffect(() => {
    setTheme(getInitialTheme());
  }, []);

  useEffect(() => {
    const $html = document.querySelector("html");
    $html?.classList.remove("light", "dark");
    $html?.classList.add(theme);
    localStorage.setItem("THEME", theme);
  }, [theme]);

  const toggleTheme = () => setTheme((prev) => (prev === "dark" ? "light" : "dark"));

  return <ThemeContext.Provider value={{ theme, toggleTheme }}>{children}</ThemeContext.Provider>;
};

export default ThemeProvider;
