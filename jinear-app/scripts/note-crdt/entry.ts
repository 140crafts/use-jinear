import {getSchema} from "@tiptap/core";
import {DOMParser, DOMSerializer, Node as ProseMirrorNode} from "@tiptap/pm/model";
import {updateYFragment, yXmlFragmentToProseMirrorRootNode} from "@tiptap/y-tiptap";
import {parseHTML} from "linkedom";
import * as Y from "yjs";
import {fromBase64, toBase64} from "../../src/components/tiptap/crdt/base64";
import {CRDT_FIELD, TITLE_FIELD} from "../../src/components/tiptap/crdt/constants";
import {buildEditorExtensions} from "../../src/components/tiptap/editorExtensions";

const schema = getSchema(buildEditorExtensions({history: false}));
const {document} = parseHTML("<!doctype html><html><body></body></html>");
const domDocument = document as unknown as Document;

const loadDoc = (states: string[]): Y.Doc => {
    const doc = new Y.Doc();
    states.forEach((state) => Y.applyUpdate(doc, fromBase64(state)));
    return doc;
};

const parseBody = (html: string): ProseMirrorNode => {
    const container = document.createElement("div");
    container.innerHTML = html;
    return DOMParser.fromSchema(schema).parse(container as unknown as HTMLElement);
};

const renderBody = (doc: Y.Doc): string => {
    const root = yXmlFragmentToProseMirrorRootNode(doc.getXmlFragment(CRDT_FIELD), schema);
    const fragment = DOMSerializer.fromSchema(schema).serializeFragment(root.content, {document: domDocument});
    const container = document.createElement("div");
    container.appendChild(fragment as never);
    return container.innerHTML;
};

const replaceTitle = (text: Y.Text, title: string) => {
    const current = text.toString();
    let prefix = 0;
    while (prefix < current.length && prefix < title.length && current[prefix] === title[prefix]) prefix++;
    let suffix = 0;
    while (
        suffix < current.length - prefix
        && suffix < title.length - prefix
        && current[current.length - 1 - suffix] === title[title.length - 1 - suffix]
    ) suffix++;
    text.delete(prefix, current.length - prefix - suffix);
    text.insert(prefix, title.slice(prefix, title.length - suffix));
};

const changesDoc = (doc: Y.Doc, change: () => void): boolean => {
    let changed = false;
    const onTransaction = (transaction: Y.Transaction) => {
        changed = changed || transaction.changed.size > 0;
    };
    doc.on("afterTransaction", onTransaction);
    change();
    doc.off("afterTransaction", onTransaction);
    return changed;
};

const describe = (doc: Y.Doc, update: Uint8Array, changed: boolean) => ({
    update: toBase64(update),
    changed,
    title: doc.getText(TITLE_FIELD).toString(),
    html: renderBody(doc)
});

const read = (statesJson: string): string => {
    const doc = loadDoc(JSON.parse(statesJson));
    return JSON.stringify({title: doc.getText(TITLE_FIELD).toString(), html: renderBody(doc)});
};

const edit = (statesJson: string, html: string | null, title: string | null): string => {
    const doc = loadDoc(JSON.parse(statesJson));
    const before = Y.encodeStateVector(doc);
    const changed = changesDoc(doc, () => doc.transact(() => {
        if (html !== null) {
            const meta = {mapping: new Map(), isOMark: new Map()};
            updateYFragment(doc, doc.getXmlFragment(CRDT_FIELD), parseBody(html), meta as never);
        }
        if (title !== null) replaceTitle(doc.getText(TITLE_FIELD), title);
    }));
    return JSON.stringify(describe(doc, Y.encodeStateAsUpdate(doc, before), changed));
};

(globalThis as unknown as {noteCrdt: unknown}).noteCrdt = {read, edit};
