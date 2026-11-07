import React from "react";
import type {NoteDto} from "@/model/be/jinear-core";
import Button, {ButtonVariants} from "@/components/button";
import {LuFileText, LuLock} from "react-icons/lu";
import SearchHighlight from "../searchHighlight/SearchHighlight";
import styles from "./SearchResultRow.module.css";

interface NoteSearchResultRowProps {
    note: NoteDto;
    query: string;
    workspaceUsername?: string;
    onSelect: () => void;
}

const NoteSearchResultRow: React.FC<NoteSearchResultRowProps> = ({note, query, workspaceUsername, onSelect}) => {
    const isPrivateNotebook = note.notebook?.visibility == "PRIVATE";

    return (
        <Button
            className={styles.row}
            variant={ButtonVariants.hoverFilled2}
            href={`/${workspaceUsername}/notebook/${note.notebookId}/note/${note.noteId}`}
            onClick={onSelect}
        >
            <LuFileText className={styles.icon}/>
            <div className={styles.textContainer}>
                <div className={styles.title}>
                    <SearchHighlight text={note.title} query={query}/>
                </div>
                {note.notebook?.title && (
                    <div className={styles.subtitle}>
                        <span className={styles.notebook}>
                            {isPrivateNotebook && <LuLock/>}
                            {note.notebook.title}
                        </span>
                    </div>
                )}
            </div>
        </Button>
    );
};

export default NoteSearchResultRow;
