import React from 'react';
import styles from './NoteHierarchyItem.module.css';
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {useToggle} from "@/hooks/useToggle.ts";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuChevronDown, LuChevronRight, LuFileText} from "react-icons/lu";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import NoteHierarchyList
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";
import cn from "classnames";

interface NoteHierarchyItemProps {
    workspace: WorkspaceDto,
    note: NoteDto;
    notebookId?: string;
    forDrafts: boolean;
    pathname: string;
}

const NoteHierarchyItem: React.FC<NoteHierarchyItemProps> = ({
                                                                 workspace,
                                                                 note,
                                                                 notebookId,
                                                                 forDrafts,
                                                                 pathname
                                                             }) => {
    const {t} = useTranslation();
    const [open, toggle] = useToggle(false);

    const path = `/${workspace.username}/notebook/${note?.notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${note?.noteId}`;
    const atPath = pathname?.indexOf(path) != -1;

    return (
        <div className={styles.noteButtonGroup}>
            <div className={cn(styles.noteRow, atPath && styles.noteAtPath)}>
                <Button
                    heightVariant={ButtonHeight.mid}
                    variant={ButtonVariants.hoverFilled2}
                    onClick={toggle}
                    className={cn(styles.iconButton,forDrafts && styles.hoverBgNone)}
                >
                    <LuFileText className={cn('icon', styles.fileIcon, forDrafts && styles.shown)}/>
                    {(open ? <LuChevronDown className={cn('icon', styles.dropButton, forDrafts && styles.hidden)}/> :
                        <LuChevronRight className={cn('icon', styles.dropButton, forDrafts && styles.hidden)}/>)}
                </Button>

                <Button
                    heightVariant={ButtonHeight.mid}
                    href={path}
                    className={cn(styles.noteButton)}
                >
                <span className={'line-clamp'}>
                    {shortenStringIfMoreThanMaxLength({
                        text: (note.title == null || note.title == '') ? t('untitledNote') : note.title,
                        maxLength: 29
                    })}
                </span>
                </Button>

            </div>

            {/*<Button*/}
            {/*    heightVariant={ButtonHeight.short}*/}
            {/*    variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}*/}
            {/*    href={path}*/}
            {/*    onClick={toggle}*/}
            {/*    className={styles.noteButton}*/}
            {/*>*/}

            {/*    <span className={'line-clamp'}>*/}
            {/*        {shortenStringIfMoreThanMaxLength({*/}
            {/*            text: (note.title == null || note.title == '') ? t('untitledNote') : note.title,*/}
            {/*            maxLength: 29*/}
            {/*        })}*/}
            {/*    </span>*/}
            {/*</Button>*/}

            {!forDrafts && open &&
                <div className={styles.childContainer}>
                    <NoteHierarchyList
                        workspace={workspace}
                        notebookId={notebookId}
                        parentNote={note}/>
                </div>
            }
        </div>
    );
}

export default NoteHierarchyItem;