import {useCallback, useEffect, useState} from "react";
import type * as Y from "yjs";

/**
 * Binds a named Y.Text field of a doc to React state. Remote and local edits both land through
 * the observer. Writes are whole-value replaces — last-writer-wins per edit, fine for titles.
 */
export const useYText = (doc: Y.Doc | null | undefined, field: string): [string, (next: string) => void] => {
    const [value, setValue] = useState<string>("");

    useEffect(() => {
        if (!doc) {
            setValue("");
            return;
        }
        const text = doc.getText(field);
        const read = () => setValue(text.toString());
        read();
        text.observe(read);
        return () => text.unobserve(read);
    }, [doc, field]);

    const set = useCallback((next: string) => {
        if (!doc) return;
        const text = doc.getText(field);
        doc.transact(() => {
            text.delete(0, text.length);
            text.insert(0, next);
        });
    }, [doc, field]);

    return [value, set];
};
