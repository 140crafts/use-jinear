import {hashString} from "@/util/textUtil.ts";

function getRGB(c: string): number {
    return parseInt(c, 16) || 0;
}

function getsRGB(c: string): number {
    return getRGB(c) / 255 <= 0.03928 ? getRGB(c) / 255 / 12.92 : Math.pow((getRGB(c) / 255 + 0.055) / 1.055, 2.4);
}

function getLuminance(hexColor: string): number {
    return (
        0.2126 * getsRGB(hexColor.substr(1, 2)) + 0.7152 * getsRGB(hexColor.substr(3, 2)) + 0.0722 * getsRGB(hexColor.substr(-2))
    );
}

function getContrast(f: string, b: string): number {
    const L1 = getLuminance(f);
    const L2 = getLuminance(b);
    return (Math.max(L1, L2) + 0.05) / (Math.min(L1, L2) + 0.05);
}

export function getTextColor(bgColor: string): "#ffffff" | "#000000" {
    const whiteContrast = getContrast(bgColor, "#ffffff");
    const blackContrast = getContrast(bgColor, "#000000");

    return whiteContrast > blackContrast ? "#ffffff" : "#000000";
}

export const getHashedColor = ({text}: { text: string }) => {
    const BADGE_COLORS = [
        '#E5484D', // red
        '#E54D2E', // tomato
        '#F76B15', // orange
        '#978365', // gold
        '#30A46C', // green
        '#12A594', // teal
        '#00A2C7', // cyan
        '#3E63DD', // indigo
        '#6E56CF', // violet
        '#8E4EC6', // purple
        '#D6409F', // pink
        '#E93D82', // crimson
    ];
    const i = hashString(text);
    return BADGE_COLORS[i % BADGE_COLORS.length]
}