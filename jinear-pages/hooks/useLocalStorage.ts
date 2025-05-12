"use client";

export const useLocalStorage = ({
  key,
  parser,
  defaultValue,
}: {
  key: string;
  parser?: (val: string | null) => any;
  defaultValue?: any;
}) => {
  if (typeof window === "object") {
    const value = localStorage.getItem(key);
    return value ? (parser ? parser(value) : value) : defaultValue;
  }
};

export const useSetLocalStorage = ({
  key,
  value,
  converter,
}: {
  key: string;
  value: string | any;
  converter?: (value: any) => string;
}) => {
  if (typeof window === "object") {
    localStorage.setItem(key, converter ? converter(value) : value);
  }
};
