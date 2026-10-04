import {SCOPE_ATTRIBUTE} from "@/keyboard/keymap";

export const CLICKABLE_SELECTOR = [
    "a[href]",
    "button",
    "input:not([type=hidden])",
    "textarea",
    "select",
    "summary",
    "[contenteditable=true]",
    "[role=button]",
    "[role=link]",
    "[role=tab]",
    "[role=menuitem]",
    "[role=option]",
    "[role=checkbox]",
    "[role=switch]",
].join(",");

export const isEditable = (element: Element | null) => {
    if (!(element instanceof HTMLElement)) {
        return false;
    }
    if (element.isContentEditable || element.tagName == "TEXTAREA") {
        return true;
    }
    return element.tagName == "INPUT" && !["checkbox", "radio", "button", "submit"].includes((element as HTMLInputElement).type);
};

export const normalizeKey = (event: KeyboardEvent) => {
    const key = event.key.toLowerCase();
    if (/^[a-z0-9]$/.test(key)) {
        return key;
    }
    if (event.code.startsWith("Key")) {
        return event.code.slice(3).toLowerCase();
    }
    if (event.code.startsWith("Digit")) {
        return event.code.slice(5);
    }
    return key;
};

export const targetRect = (element: HTMLElement) => element.getClientRects()[0] ?? element.getBoundingClientRect();

export const isReachable = (element: HTMLElement) => {
    if ((element as HTMLButtonElement).disabled) {
        return false;
    }
    const rect = targetRect(element);
    if (rect.width == 0 || rect.height == 0) {
        return false;
    }
    if (rect.bottom <= 0 || rect.right <= 0 || rect.top >= window.innerHeight || rect.left >= window.innerWidth) {
        return false;
    }
    const x = Math.min(Math.max(rect.left + rect.width / 2, 0), window.innerWidth - 1);
    const y = Math.min(Math.max(rect.top + rect.height / 2, 0), window.innerHeight - 1);
    const hit = document.elementFromPoint(x, y);
    return hit != null && element.contains(hit);
};

export const activate = (element: HTMLElement) => {
    if (isEditable(element) || element.tagName == "SELECT") {
        element.focus();
        return;
    }
    element.click();
};

export const resolveClickable = (element: HTMLElement) => {
    if (element.matches(CLICKABLE_SELECTOR)) {
        return element;
    }
    return Array.from(element.querySelectorAll<HTMLElement>(CLICKABLE_SELECTOR))
        .find(child => !(child as HTMLButtonElement).disabled);
};

export const topmostScope = () =>
    document.elementFromPoint(window.innerWidth / 2, window.innerHeight / 2)?.closest(`[${SCOPE_ATTRIBUTE}]`) ?? null;
