import {activate, CLICKABLE_SELECTOR, isEditable, targetRect} from "@/keyboard/dom";
import {ACTIVE_ATTRIBUTE, NAV_SKIP_ATTRIBUTE} from "@/keyboard/keymap";

export type Direction = "up" | "down" | "left" | "right";

export const ARROW_DIRECTIONS: Record<string, Direction> = {
    ArrowUp: "up",
    ArrowDown: "down",
    ArrowLeft: "left",
    ArrowRight: "right",
};

const SINGLE_LINE_INPUT_TYPES = ["text", "search", "email", "url", "tel", "password"];

let active: HTMLElement | null = null;

const setActive = (element: HTMLElement | null) => {
    active?.removeAttribute(ACTIVE_ATTRIBUTE);
    active = element;
    if (element) {
        element.setAttribute(ACTIVE_ATTRIBUTE, "");
        element.scrollIntoView({block: "nearest"});
    }
};

export const clearActive = () => setActive(null);

const currentActive = (scope: Element) => (active?.isConnected && scope.contains(active) ? active : null);

export const arrowBelongsToFocus = (direction: Direction) => {
    const focused = document.activeElement;
    if (focused instanceof HTMLSelectElement) {
        return true;
    }
    if (!isEditable(focused)) {
        return false;
    }
    if (direction == "left" || direction == "right") {
        return true;
    }
    return !(focused instanceof HTMLInputElement && SINGLE_LINE_INPUT_TYPES.includes(focused.type));
};

const navigationItems = (scope: Element) =>
    Array.from(scope.querySelectorAll<HTMLElement>(CLICKABLE_SELECTOR)).filter(element => {
        if ((element as HTMLButtonElement).disabled || element == document.activeElement) {
            return false;
        }
        if (element.closest(`[${NAV_SKIP_ATTRIBUTE}]`)) {
            return false;
        }
        const rect = targetRect(element);
        return rect.width > 0 && rect.height > 0 && element.checkVisibility();
    });

const center = (rect: DOMRect) => ({x: rect.left + rect.width / 2, y: rect.top + rect.height / 2});

const nearest = (items: HTMLElement[], from: DOMRect, direction: Direction) => {
    const origin = center(from);
    let best: HTMLElement | null = null;
    let bestScore = Infinity;
    for (const item of items) {
        const point = center(targetRect(item));
        const dx = point.x - origin.x;
        const dy = point.y - origin.y;
        const vertical = direction == "up" || direction == "down";
        const forward = direction == "down" ? dy : direction == "up" ? -dy : direction == "right" ? dx : -dx;
        if (forward <= 1) {
            continue;
        }
        const score = forward + 2 * Math.abs(vertical ? dx : dy);
        if (score < bestScore) {
            bestScore = score;
            best = item;
        }
    }
    return best;
};

export const moveActive = (scope: Element, direction: Direction) => {
    const items = navigationItems(scope);
    if (items.length == 0) {
        return false;
    }
    const current = currentActive(scope);
    const focused = document.activeElement;
    const focusedField = isEditable(focused) && scope.contains(focused) ? (focused as HTMLElement) : null;

    if (current) {
        const next = nearest(items.filter(item => item != current), targetRect(current), direction);
        if (next) {
            setActive(next);
        } else if (focusedField && direction == "up") {
            clearActive();
        }
        return true;
    }

    const preset = items.find(item => item.hasAttribute("aria-current"));
    if (preset) {
        setActive(preset);
    } else if (focusedField) {
        setActive(nearest(items, targetRect(focusedField), direction));
    } else {
        setActive(direction == "down" || direction == "right" ? items[0] : items[items.length - 1]);
    }
    return true;
};

export const clickActive = (scope: Element) => {
    const current = currentActive(scope);
    if (!current) {
        return false;
    }
    clearActive();
    activate(current);
    return true;
};
