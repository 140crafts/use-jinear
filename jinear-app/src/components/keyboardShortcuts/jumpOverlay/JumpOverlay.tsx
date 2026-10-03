import React, {useEffect, useRef, useState} from "react";
import {createPortal} from "react-dom";
import {activate, normalizeKey} from "@/keyboard/dom";
import {findJumpTargets, type JumpTarget} from "@/keyboard/jumpTargets";
import {JUMP_LABEL_CHARACTERS} from "@/keyboard/keymap";
import styles from "../KeyboardShortcuts.module.css";

interface JumpOverlayProps {
    onClose: () => void;
}

const MODIFIER_KEYS = ["Alt", "Shift", "Meta", "Control", "CapsLock"];

const JumpOverlay: React.FC<JumpOverlayProps> = ({onClose}) => {
    const [targets] = useState<JumpTarget[]>(findJumpTargets);
    const [typed, setTyped] = useState("");
    const typedRef = useRef("");

    useEffect(() => {
        if (targets.length == 0) {
            onClose();
            return;
        }

        const updateTyped = (value: string) => {
            typedRef.current = value;
            setTyped(value);
        };

        const onKeyDown = (event: KeyboardEvent) => {
            if (event.metaKey || event.ctrlKey) {
                onClose();
                return;
            }
            event.preventDefault();
            event.stopImmediatePropagation();
            if (MODIFIER_KEYS.includes(event.key)) {
                return;
            }
            if (event.key == "Escape") {
                onClose();
                return;
            }
            if (event.key == "Backspace") {
                if (typedRef.current.length == 0) {
                    onClose();
                } else {
                    updateTyped(typedRef.current.slice(0, -1));
                }
                return;
            }
            const key = normalizeKey(event);
            if (!JUMP_LABEL_CHARACTERS.includes(key)) {
                return;
            }
            const next = typedRef.current + key;
            const exact = targets.find(target => target.label == next);
            if (exact) {
                onClose();
                activate(exact.element);
            } else if (targets.some(target => target.label.startsWith(next))) {
                updateTyped(next);
            }
        };

        window.addEventListener("keydown", onKeyDown, true);
        window.addEventListener("mousedown", onClose, true);
        window.addEventListener("wheel", onClose, true);
        window.addEventListener("scroll", onClose, true);
        window.addEventListener("resize", onClose);
        window.addEventListener("blur", onClose);
        return () => {
            window.removeEventListener("keydown", onKeyDown, true);
            window.removeEventListener("mousedown", onClose, true);
            window.removeEventListener("wheel", onClose, true);
            window.removeEventListener("scroll", onClose, true);
            window.removeEventListener("resize", onClose);
            window.removeEventListener("blur", onClose);
        };
    }, [targets, onClose]);

    return createPortal(
        <div className={styles.container}>
            {targets
                .filter(target => target.label.startsWith(typed))
                .map(target => (
                    <span key={target.label} className={styles.hint} style={{top: target.top, left: target.left}}>
                        <span className={styles.typed}>{typed}</span>
                        {target.label.slice(typed.length)}
                    </span>
                ))}
        </div>,
        document.body
    );
};

export default JumpOverlay;
