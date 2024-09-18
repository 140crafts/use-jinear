import Logger from "@/utils/logger";

const logger = Logger("htmlUtils");
export const getOffset = (el: HTMLElement) => {
  const rect = el.getBoundingClientRect();
  return {
    left: rect.left + window.scrollX,
    top: rect.top + window.scrollY
  };
};

export const getSize = (el: HTMLElement) => {
  const rect = el.getBoundingClientRect();
  return {
    width: rect.width,
    height: rect.height
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
    setTimeout(function() {
      el.focus();
      el.click();
      // Remove the temp element
      document.body.removeChild(__tempEl__);
    }, timeout);
  }
};

export const scrollToBottom = (vo?: { behavior?: "auto" | "smooth" }) => {
  const behavior = vo?.behavior || "smooth";
  window.scrollTo({
    top: document.documentElement.scrollHeight - window.innerHeight,
    left: 0,
    behavior
  });
};

export const decideAndScrollToBottom = ({
                                          initialShouldScroll = true,
                                          limitRatio = 0.85,
                                          timeout = 500,
                                          callBack
                                        }:
                                          {
                                            initialShouldScroll?: boolean,
                                            limitRatio?: number,
                                            timeout?: number;
                                            callBack?: () => void
                                          }
) => {
  const currentScrollY = window.scrollY;
  const bottom = document.documentElement.scrollHeight - window.innerHeight;
  logger.log({ initialShouldScroll, isAboveLimit: (currentScrollY >= (bottom * limitRatio)) });
  const shouldScroll = (currentScrollY >= (bottom * limitRatio)) || initialShouldScroll;
  if (shouldScroll) {
    setTimeout(() => {
      scrollToBottom();
      callBack?.();
    }, timeout);
  }
};

export const isFullyVisible = ({ elem, tolerance = 0.5 }: { elem: HTMLElement, tolerance?: number }) => {
  const rect = elem.getBoundingClientRect();
  const windowHeight = (window.innerHeight || document.documentElement.clientHeight);
  const windowWidth = (window.innerWidth || document.documentElement.clientWidth);

  // Check with tolerance for being within the viewport
  const inViewVertically = rect.top <= windowHeight + tolerance && rect.bottom >= -tolerance;
  const inViewHorizontally = rect.left <= windowWidth + tolerance && rect.right >= -tolerance;
  const inViewport = inViewVertically && inViewHorizontally;

  // Check for CSS visibility, 'hidden' attribute, and dimensions
  const style = getComputedStyle(elem);
  const notHiddenByCSS = style.display !== "none" && style.visibility !== "hidden" && parseFloat(style.opacity) > 0;
  const notHiddenAttribute = !elem.hidden;
  const hasDimensions = elem.offsetWidth > 0 || elem.offsetHeight > 0 || elem.getClientRects().length > 0;

  return inViewport && notHiddenByCSS && notHiddenAttribute && hasDimensions;
};