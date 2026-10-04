import React, {useCallback, useEffect, useState} from "react";
import {createPortal} from "react-dom";
import {
    CLEAR_ATTRIBUTE,
    DISMISS_ATTRIBUTE,
    JUMP_MODE_KEY,
    SCOPE_ATTRIBUTE,
    SHORTCUT_ATTRIBUTE,
    SHORTCUT_MODIFIER,
    type ShortcutId,
    shortcuts
} from "@/keyboard/keymap";
import {activate, isEditable, isReachable, normalizeKey, resolveClickable, targetRect, topmostScope} from "@/keyboard/dom";
import {ARROW_DIRECTIONS, arrowBelongsToFocus, clearActive, clickActive, moveActive} from "@/keyboard/spatialNavigation";
import JumpOverlay from "./jumpOverlay/JumpOverlay";
import styles from "./KeyboardShortcuts.module.css";

interface ShortcutTarget {
    element: HTMLElement;
    key: string;
}

interface Hint {
    key: string;
    label: string;
    top: number;
    left: number;
}

const HINT_DELAY_MS = 150;

const activeScope = (): ParentNode | null => {
    const active = document.activeElement;
    const top = topmostScope();
    if (top && !top.contains(active)) {
        return top;
    }
    if (!isEditable(active)) {
        return document;
    }
    return active!.closest(`[${SCOPE_ATTRIBUTE}]`);
};

const findTargets = (scope: ParentNode, clear: boolean) => {
    const targets = new Map<string, ShortcutTarget>();
    scope.querySelectorAll<HTMLElement>(`[${SHORTCUT_ATTRIBUTE}]`).forEach(tagged => {
        const key = shortcuts[tagged.getAttribute(SHORTCUT_ATTRIBUTE) as ShortcutId];
        const element = clear ? tagged.querySelector<HTMLElement>(`[${CLEAR_ATTRIBUTE}]`) : resolveClickable(tagged);
        if (key && element && !targets.has(key) && isReachable(element)) {
            targets.set(key, {element, key});
        }
    });
    return targets;
};

const KEY_LABELS: Record<string, string> = {enter: "↵"};

const toHints = (targets: Map<string, ShortcutTarget>, clear: boolean): Hint[] =>
    Array.from(targets.values()).map(({element, key}) => {
        const rect = targetRect(element);
        const label = KEY_LABELS[key] ?? key;
        return {key, label: clear ? `⇧${label}` : label, top: rect.bottom, left: rect.right};
    });

const dismissTopmost = () => {
    const dismisser = topmostScope()?.querySelector<HTMLElement>(`[${DISMISS_ATTRIBUTE}]`);
    if (dismisser) {
        dismisser.click();
        return true;
    }
    return false;
};

const KeyboardShortcuts: React.FC = () => {
    const [hints, setHints] = useState<Hint[] | null>(null);
    const [jumpMode, setJumpMode] = useState(false);
    const closeJumpMode = useCallback(() => setJumpMode(false), []);

    useEffect(() => {
        let showTimer: number | undefined;
        let refreshFrame: number | undefined;
        let observer: MutationObserver | undefined;
        let clearMode = false;

        const refresh = () => {
            if (refreshFrame != null) {
                return;
            }
            refreshFrame = requestAnimationFrame(() => {
                refreshFrame = undefined;
                const scope = activeScope();
                setHints(scope ? toHints(findTargets(scope, clearMode), clearMode) : []);
            });
        };

        const show = () => {
            refresh();
            window.addEventListener("scroll", refresh, true);
            window.addEventListener("resize", refresh);
            observer = new MutationObserver(refresh);
            observer.observe(document.body, {childList: true, subtree: true, attributes: true});
        };

        const hide = () => {
            window.clearTimeout(showTimer);
            showTimer = undefined;
            if (refreshFrame != null) {
                cancelAnimationFrame(refreshFrame);
                refreshFrame = undefined;
            }
            window.removeEventListener("scroll", refresh, true);
            window.removeEventListener("resize", refresh);
            observer?.disconnect();
            observer = undefined;
            setHints(null);
        };

        const onKeyDown = (event: KeyboardEvent) => {
            const plain = !event.altKey && !event.ctrlKey && !event.metaKey && !event.shiftKey;
            const direction = ARROW_DIRECTIONS[event.key];
            if (plain && !event.defaultPrevented && (direction || event.key == "Enter")) {
                const top = topmostScope();
                if (top && direction && !(top.contains(document.activeElement) && arrowBelongsToFocus(direction)) && moveActive(top, direction)) {
                    event.preventDefault();
                }
                if (top && event.key == "Enter" && clickActive(top)) {
                    event.preventDefault();
                }
                return;
            }
            if (event.key == "Escape") {
                if (event.defaultPrevented) {
                    return;
                }
                hide();
                if (!dismissTopmost() && isEditable(document.activeElement)) {
                    (document.activeElement as HTMLElement).blur();
                }
                return;
            }
            if (event.key == "Shift" && event.altKey) {
                clearMode = true;
                if (observer) {
                    refresh();
                }
                return;
            }
            if (event.key == SHORTCUT_MODIFIER) {
                clearMode = event.shiftKey;
                if (!event.repeat && activeScope()) {
                    window.clearTimeout(showTimer);
                    showTimer = window.setTimeout(show, HINT_DELAY_MS);
                }
                return;
            }
            // ctrlKey + altKey is AltGr on Windows, used to type characters like @.
            const scope = activeScope();
            if (!event.altKey || event.ctrlKey || event.metaKey || !scope) {
                hide();
                return;
            }
            const key = normalizeKey(event);
            if (key == JUMP_MODE_KEY && !event.shiftKey) {
                event.preventDefault();
                hide();
                setJumpMode(true);
                return;
            }
            const target = findTargets(scope, event.shiftKey).get(key);
            if (target) {
                event.preventDefault();
                activate(target.element);
            }
        };

        const onKeyUp = (event: KeyboardEvent) => {
            if (event.key == SHORTCUT_MODIFIER) {
                hide();
            } else if (event.key == "Shift" && clearMode) {
                clearMode = false;
                if (observer) {
                    refresh();
                }
            }
        };

        window.addEventListener("keydown", onKeyDown);
        window.addEventListener("keyup", onKeyUp);
        window.addEventListener("mousedown", clearActive, true);
        window.addEventListener("blur", hide);
        document.addEventListener("visibilitychange", hide);
        return () => {
            hide();
            window.removeEventListener("keydown", onKeyDown);
            window.removeEventListener("keyup", onKeyUp);
            window.removeEventListener("mousedown", clearActive, true);
            window.removeEventListener("blur", hide);
            document.removeEventListener("visibilitychange", hide);
        };
    }, []);

    if (jumpMode) {
        return <JumpOverlay onClose={closeJumpMode}/>;
    }

    if (!hints) {
        return null;
    }

    return createPortal(
        <div className={styles.container}>
            {hints.map(hint => (
                <span key={hint.key} className={styles.hint} style={{top: hint.top, left: hint.left}}>
                    {hint.label}
                </span>
            ))}
        </div>,
        document.body
    );
};

export default KeyboardShortcuts;
