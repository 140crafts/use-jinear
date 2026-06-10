import { createContext } from "react";

export const getTheme = () => {
  if (typeof window === "object") {
    const sysPref = window?.matchMedia && window?.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
    const theme = localStorage.getItem("THEME") || sysPref;
    return theme;
  }
  return "dark";
};
const ThemeContext = createContext({
  theme: getTheme(),
  toggleTheme: () => {},
});
export default ThemeContext;
