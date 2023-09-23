export const getOffset = (el: HTMLElement) => {
  const rect = el.getBoundingClientRect();
  return {
    left: rect.left + window.scrollX,
    top: rect.top + window.scrollY,
  };
};

export const getSize = (el: HTMLElement) => {
  const rect = el.getBoundingClientRect();
  return {
    width: rect.width,
    height: rect.height,
  };
};
