export const shortenStringIfMoreThanMaxLength = (vo: { text: string; maxLength: number }) => {
  const { text, maxLength } = vo;
  return text?.length > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
};
