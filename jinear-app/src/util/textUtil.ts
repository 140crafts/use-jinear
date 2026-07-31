export const shortenStringIfMoreThanMaxLength = (vo: {
    text: string;
    maxLength: number;
    shortenFromMiddle?: boolean;
}) => {
    const {text, maxLength, shortenFromMiddle = false} = vo;

    if (!text || text.length <= maxLength) return text;

    const ellipsis = "...";

    if (!shortenFromMiddle) {
        return text.substring(0, maxLength - ellipsis.length) + ellipsis;
    }

    const visible = maxLength - ellipsis.length;
    if (visible <= 0) return ellipsis;

    const front = Math.ceil(visible / 2);
    const back = visible - front;

    return text.substring(0, front) + ellipsis + (back > 0 ? text.substring(text.length - back) : "");
};


export const hashString = (value: string): number => {
    let hash = 2166136261; // FNV offset basis
    for (let i = 0; i < value.length; i++) {
        hash ^= value.charCodeAt(i);
        hash = Math.imul(hash, 16777619); // FNV prime
    }
    return hash >>> 0; // force unsigned 32-bit
}