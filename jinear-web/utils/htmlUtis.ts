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

export const focusAndOpenKeyboard = (inputElementId: string, timeout?: number) => {
  if (!timeout) {
    timeout = 100;
  }
  const el = document.getElementById(inputElementId);
  if (el) {
    // Align temp input element approximately where the input element is
    // so the cursor doesn't jump around
    const elOffset = getOffset(el);
    const __tempEl__ = document.createElement("input");
    __tempEl__.style.position = "absolute";
    __tempEl__.style.top = `${elOffset.top}px`;
    __tempEl__.style.left = `${elOffset.left}px`;
    __tempEl__.style.height = "0";
    __tempEl__.style.opacity = "0";
    // Put this temp element as a child of the page <body> and focus on it
    document.body.appendChild(__tempEl__);
    __tempEl__.focus();
    // The keyboard is open. Now do a delayed focus on the target element
    setTimeout(function () {
      el.focus();
      el.click();
      // Remove the temp element
      document.body.removeChild(__tempEl__);
    }, timeout);
  }
};
