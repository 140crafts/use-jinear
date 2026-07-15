import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import styles from "./NotesSectionSideMenu.module.css";
import {useParams} from "react-router-dom";
import useTranslation from "@/locals/useTranslation.ts";
import NotesActionButtons from "@/components/notebookSectionSideMenu/notesActionButtons/NotesActionButtons.tsx";
import NotebookList from "@/components/notebookSectionSideMenu/notebookList/NotebookList.tsx";

interface NotesSectionSideMenuProps {
}

const NotebookSectionSideMenu: React.FC<NotesSectionSideMenuProps> = ({}) => {
    const {t} = useTranslation();
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    return (
        <div className={styles.container}>
            {workspace && (
                <>
                    <NotesActionButtons workspace={workspace}/>
                    <NotebookList workspace={workspace}/>
                </>
            )}
        </div>
    );
};

export default NotebookSectionSideMenu;
