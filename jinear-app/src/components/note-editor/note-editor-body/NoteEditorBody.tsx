import React from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText from "@/components/tiptap/CollaborativeRichText.tsx";

interface NoteEditorBodyProps {

}

const NoteEditorBody: React.FC<NoteEditorBodyProps> = ({}) => {

    return (
        <CollaborativeRichText  className={styles.container}>

        </CollaborativeRichText>
    );
}

export default NoteEditorBody;