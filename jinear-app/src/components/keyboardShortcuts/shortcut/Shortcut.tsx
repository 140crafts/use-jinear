import React from "react";
import {SHORTCUT_ATTRIBUTE, type ShortcutId} from "@/keyboard/keymap";

interface ShortcutProps {
    id: ShortcutId;
    children: React.ReactNode;
}

const Shortcut: React.FC<ShortcutProps> = ({id, children}) => {
    return (
        <div {...{[SHORTCUT_ATTRIBUTE]: id}} style={{display: "contents"}}>
            {children}
        </div>
    );
};

export default Shortcut;
