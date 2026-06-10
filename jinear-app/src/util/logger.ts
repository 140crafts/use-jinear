import {__DEV__} from "./constants";

export const getLoggerEnabledOnProd = () =>
    localStorage.getItem("DEBUG") != null || localStorage.getItem("debug") != null;

const isEnabled = () => __DEV__ || getLoggerEnabledOnProd();

const noop = () => {};

type Level = "log" | "warn" | "error";

const bound = (level: Level, tag: string): (...args: unknown[]) => void =>
    isEnabled() ? console[level].bind(console, `[${tag}]`) : noop;

const Logger = (tag = "NoTag") => ({
    tag,
    get log() { return bound("log", tag); },
    get warn() { return bound("warn", tag); },
    get error() { return bound("error", tag); },
});

export default Logger;
