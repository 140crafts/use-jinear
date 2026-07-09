import React from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText from "@/components/tiptap/CollaborativeRichText.tsx";
import {useRichTextCrdt} from "@/components/tiptap/crdt/useRichTextCrdt.ts";
import Logger from "@/util/logger.ts";
import {encodeDocState} from "@/components/tiptap/crdt/encodeState.ts";
import Button from "@/components/button";

interface NoteEditorBodyProps {
    richTextId?: string
}

const logger = Logger("NoteEditorBody");

const NoteEditorBody: React.FC<NoteEditorBodyProps> = ({richTextId}) => {
    const {doc, status} = useRichTextCrdt(richTextId, {format: "CRDT", enabled: true});

    return (
        doc ? <CollaborativeRichText doc={doc} className={styles.container}/> : null
    );
}

export default NoteEditorBody;