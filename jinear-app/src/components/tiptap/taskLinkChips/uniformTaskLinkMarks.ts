import {Mark, type Node as ProseMirrorNode} from "@tiptap/pm/model";
import type {EditorState, Transaction} from "@tiptap/pm/state";
import {Mapping} from "@tiptap/pm/transform";
import {ySyncPluginKey} from "@tiptap/y-tiptap";

export interface TaskLinkRange {
    from: number;
    to: number;
    href: string;
}

type MarkSet = readonly Mark[];

const textMarksIn = (doc: ProseMirrorNode, range: TaskLinkRange) => {
    const markSets: MarkSet[] = [];
    doc.nodesBetween(range.from, range.to, (node) => {
        if (node.isText) {
            markSets.push(node.marks);
        }
    });
    return markSets;
};

const unionOf = (markSets: MarkSet[]) =>
    markSets.reduce<MarkSet>((union, marks) => marks.reduce((set, mark) => mark.addToSet(set), union), Mark.none);

const marksBeforeChange = (oldDoc: ProseMirrorNode, transactions: readonly Transaction[], from: number) => {
    const mapping = new Mapping();
    transactions.forEach((transaction) => mapping.appendMapping(transaction.mapping));
    const oldFrom = mapping.invert().map(from, 1);
    const oldNode = oldFrom < oldDoc.content.size ? oldDoc.nodeAt(oldFrom) : null;
    return oldNode?.isText ? oldNode.marks : null;
};

const targetMarks = (markSets: MarkSet[], before: MarkSet | null) => {
    const changed = before ? markSets.find((marks) => !Mark.sameSet(marks, before)) : undefined;
    return changed ?? unionOf(markSets);
};

const applyMarks = (transaction: Transaction, range: TaskLinkRange, markSets: MarkSet[], target: MarkSet) => {
    unionOf(markSets)
        .filter((mark) => !mark.isInSet(target))
        .forEach((mark) => transaction.removeMark(range.from, range.to, mark));
    target.forEach((mark) => transaction.addMark(range.from, range.to, mark));
};

const isLocalDocChange = (transactions: readonly Transaction[]) =>
    transactions.some((transaction) => transaction.docChanged)
    && !transactions.some((transaction) => transaction.getMeta(ySyncPluginKey));

export const uniformTaskLinkMarks = (
    transactions: readonly Transaction[],
    oldState: EditorState,
    newState: EditorState,
    ranges: TaskLinkRange[]
) => {
    if (!isLocalDocChange(transactions)) {
        return null;
    }
    const transaction = newState.tr;
    ranges.forEach((range) => {
        const markSets = textMarksIn(newState.doc, range);
        const mixed = markSets.some((marks) => !Mark.sameSet(marks, markSets[0]));
        if (mixed) {
            const before = marksBeforeChange(oldState.doc, transactions, range.from);
            applyMarks(transaction, range, markSets, targetMarks(markSets, before));
        }
    });
    return transaction.docChanged ? transaction : null;
};
