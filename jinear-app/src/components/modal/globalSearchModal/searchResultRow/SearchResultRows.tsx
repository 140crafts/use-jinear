import React from "react";
import type {WorkspaceSearchDto, WorkspaceSearchType} from "@/model/be/jinear-core";
import TaskSearchResultRow from "./TaskSearchResultRow";
import NoteSearchResultRow from "./NoteSearchResultRow";
import MaterialSearchResultRow from "./MaterialSearchResultRow";

interface SearchResultRowsProps {
    type: WorkspaceSearchType;
    result?: WorkspaceSearchDto;
    limit?: number;
    query: string;
    workspaceUsername?: string;
    isMobile: boolean;
    onSelect: () => void;
}

const SearchResultRows: React.FC<SearchResultRowsProps> = ({type, result, limit, query, workspaceUsername, isMobile, onSelect}) => {
    if (type == "TASK") {
        return result?.tasks?.content?.slice(0, limit).map(task => (
            <TaskSearchResultRow
                key={task.taskId}
                task={task}
                query={query}
                workspaceUsername={workspaceUsername}
                isMobile={isMobile}
                onSelect={onSelect}
            />
        ));
    }
    if (type == "NOTE") {
        return result?.notes?.content?.slice(0, limit).map(note => (
            <NoteSearchResultRow
                key={note.noteId}
                note={note}
                query={query}
                workspaceUsername={workspaceUsername}
                onSelect={onSelect}
            />
        ));
    }
    return result?.materials?.content?.slice(0, limit).map(material => (
        <MaterialSearchResultRow
            key={material.materialId}
            material={material}
            query={query}
            workspaceUsername={workspaceUsername}
            isMobile={isMobile}
            onSelect={onSelect}
        />
    ));
};

export default SearchResultRows;
