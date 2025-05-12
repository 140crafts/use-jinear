const getCssVariable = (property: string): string => {
  return getComputedStyle(document.body).getPropertyValue(property);
};

export default getCssVariable;
