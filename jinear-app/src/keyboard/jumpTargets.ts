import {CLICKABLE_SELECTOR, isReachable, targetRect} from "@/keyboard/dom";
import {JUMP_LABEL_CHARACTERS} from "@/keyboard/keymap";

export interface JumpTarget {
    element: HTMLElement;
    label: string;
    top: number;
    left: number;
}

const createLabels = (count: number) => {
    const characters = JUMP_LABEL_CHARACTERS;
    let length = 1;
    while (Math.pow(characters.length, length) < count) {
        length++;
    }
    return Array.from({length: count}, (_, index) => {
        let label = "";
        let rest = index;
        for (let i = 0; i < length; i++) {
            label = characters[rest % characters.length] + label;
            rest = Math.floor(rest / characters.length);
        }
        return label;
    });
};

export const findJumpTargets = (): JumpTarget[] => {
    const elements = Array.from(document.querySelectorAll<HTMLElement>(CLICKABLE_SELECTOR))
        .filter(isReachable)
        .map(element => ({element, rect: targetRect(element)}))
        .sort((a, b) => a.rect.top - b.rect.top || a.rect.left - b.rect.left);
    const labels = createLabels(elements.length);
    return elements.map(({element, rect}, index) => ({
        element,
        label: labels[index],
        top: rect.bottom,
        left: rect.right,
    }));
};
