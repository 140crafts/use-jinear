const LOCALE_EN = "en-US";
const EMPTY_STRING = "";
const SPACE_STRING = " ";
const MASK_CHAR = "*";
const ACCENT_REGEX = /\\p{M}/;
const ALPHANUMERIC_REGEX = /[^A-Za-z0-9]/;
const USERNAME_REGEX = /[^A-Za-z0-9-_]/;
const ASCII_REGEX = /[\u{0080}-\u{10FFFF}]/gu;

const normalize = (str: string) => str?.normalize?.("NFD");

export const removeAccent = (str: string) =>
  normalize(str)
    ?.split(ACCENT_REGEX)
    ?.join(EMPTY_STRING)
    ?.toLocaleLowerCase?.(LOCALE_EN);

export const removeNonAlphaNumeric = (str: string) =>
  normalize(str)
    ?.split(ALPHANUMERIC_REGEX)
    ?.join(EMPTY_STRING)
    ?.toLocaleLowerCase?.(LOCALE_EN);

export const removeNonAscii = (str: string) =>
  normalize(str)
    ?.split(ASCII_REGEX)
    ?.join(EMPTY_STRING)
    ?.toLocaleLowerCase?.(LOCALE_EN);

export const removeUsernameNotAllowed = (str: string) =>
  normalize(str)
    ?.split(USERNAME_REGEX)
    ?.join(EMPTY_STRING)
    ?.toLocaleLowerCase?.(LOCALE_EN);

export const normalizeStrictly = (str: string) =>
  removeNonAlphaNumeric(removeNonAscii(removeAccent(str)));

export const normalizeUsername = (str: string) =>
  removeUsernameNotAllowed(removeNonAscii(removeAccent(str)));

export const normalizeUsernameReplaceSpaces = (str: string) =>
  normalizeUsername(str?.split(SPACE_STRING)?.join("-"));
