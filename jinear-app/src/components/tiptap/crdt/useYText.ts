import {useCallback, useEffect, useState} from "react";
import type * as Y from "yjs";

/**
 * Binds a named Y.Text field of a doc to React state. Remote and local edits both land through
 * the observer. Writes are applied as a minimal prefix/suffix diff, so a keystroke touches only the
 * characters that actually changed — a whole-value replace would make two concurrent replicas each
 * re-insert the entire string, and Yjs would keep both halves (a doubled title).
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
            const prev = text.toString();
            if (prev === next) return;
            // Longest common prefix, then longest common suffix of what's left — the changed span
            // is everything between them.
            let start = 0;
            while (start < prev.length && start < next.length && prev[start] === next[start]) start++;
            let end = 0;
            while (end < prev.length - start && end < next.length - start
            && prev[prev.length - 1 - end] === next[next.length - 1 - end]) end++;
            const removed = prev.length - start - end;
            if (removed > 0) text.delete(start, removed);
            const inserted = next.slice(start, next.length - end);
            if (inserted) text.insert(start, inserted);
        });
    }, [doc, field]);

    return [value, set];
};
