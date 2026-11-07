import React, {useMemo} from "react";
import styles from "./SearchHighlight.module.css";

interface SearchHighlightProps {
    text?: string;
    query: string;
}

interface TextPart {
    value: string;
    matched: boolean;
}

const MARK_REGEX = /\p{M}/gu;
const WORD_CHAR_REGEX = /[\p{L}\p{N}]/u;
const TURKISH_I_MAP: Record<string, string> = {"ı": "i", "İ": "i"};

// One folded character per source character, so match positions map back to the original text.
const foldCharacter = (character: string) => {
    const mapped = TURKISH_I_MAP[character] ?? character;
    const stripped = mapped.normalize("NFD").replace(MARK_REGEX, "").toLowerCase();
    return stripped.charAt(0) || mapped;
};

const fold = (value: string) => Array.from(value).map(foldCharacter).join("");

const queryTokens = (query: string) => query.split(/\s+/).map(fold).filter(token => token.length > 0);

const matchedFlags = (characters: string[], tokens: string[]) => {
    const folded = characters.map(foldCharacter).join("");
    const flags = new Array<boolean>(characters.length).fill(false);
    for (let index = 0; index < characters.length; index++) {
        const wordStart = index == 0 || !WORD_CHAR_REGEX.test(characters[index - 1]);
        if (!wordStart) {
            continue;
        }
        for (const token of tokens) {
            if (folded.startsWith(token, index)) {
                flags.fill(true, index, index + token.length);
            }
        }
    }
    return flags;
};

const splitText = (text: string, query: string): TextPart[] => {
    const characters = Array.from(text);
    const tokens = queryTokens(query);
    if (tokens.length == 0) {
        return [{value: text, matched: false}];
    }
    const flags = matchedFlags(characters, tokens);
    const parts: TextPart[] = [];
    characters.forEach((character, index) => {
        const last = parts[parts.length - 1];
        if (last && last.matched == flags[index]) {
            last.value += character;
        } else {
            parts.push({value: character, matched: flags[index]});
        }
    });
    return parts;
};

const SearchHighlight: React.FC<SearchHighlightProps> = ({text = "", query}) => {
    const parts = useMemo(() => splitText(text, query), [text, query]);

    return (
        <>
            {parts.map((part, index) =>
                part.matched
                    ? <mark key={index} className={styles.mark}>{part.value}</mark>
                    : <React.Fragment key={index}>{part.value}</React.Fragment>
            )}
        </>
    );
};

export default SearchHighlight;
