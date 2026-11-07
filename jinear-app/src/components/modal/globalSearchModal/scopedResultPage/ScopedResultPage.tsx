import React from "react";
import type {WorkspaceSearchType} from "@/model/be/jinear-core";
import {useSearchWorkspaceQuery} from "@/store/api/workspaceSearchApi";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import SearchResultRows from "../searchResultRow/SearchResultRows";
import LoadMoreButton from "./LoadMoreButton";
import {sectionOf} from "../globalSearchSections";
import styles from "./ScopedResultPage.module.css";

interface ScopedResultPageProps {
    workspaceId: string;
    workspaceUsername?: string;
    query: string;
    type: WorkspaceSearchType;
    page: number;
    isLast: boolean;
    isMobile: boolean;
    onLoadMore: () => void;
    onSelect: () => void;
}

const ScopedResultPage: React.FC<ScopedResultPageProps> = ({
                                                               workspaceId,
                                                               workspaceUsername,
                                                               query,
                                                               type,
                                                               page,
                                                               isLast,
                                                               isMobile,
                                                               onLoadMore,
                                                               onSelect
                                                           }) => {
    const {currentData: response, isFetching} = useSearchWorkspaceQuery({
        body: {workspaceId, query, types: [type]},
        page
    });
    const section = sectionOf(response?.data, type);

    return (
        <>
            <SearchResultRows
                type={type}
                result={response?.data}
                query={query}
                workspaceUsername={workspaceUsername}
                isMobile={isMobile}
                onSelect={onSelect}
            />
            {isFetching && response == null && (
                <div className={styles.loading}><CircularLoading size={17}/></div>
            )}
            {isLast && section?.hasNext && <LoadMoreButton onClick={onLoadMore}/>}
        </>
    );
};

export default ScopedResultPage;
