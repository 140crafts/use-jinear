import {type PageDto} from "@/model/be/jinear-core";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {type ReactElement} from "react";
import Pagination from "../pagination/Pagination";
import styles from "./PaginatedList.module.scss";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface PaginatedListProps<T> {
    id: string;
    listTitle?: string;
    data?: PageDto<T>;
    isFetching: boolean;
    isLoading: boolean;
    page: number;
    setPage: React.Dispatch<React.SetStateAction<number>> | ((nextPage?: number) => void);
    renderItem: (data: T, index: number) => ReactElement;
    emptyLabel?: string;
    emptyComponent?: ReactElement;
    hidePaginationOnSinglePages?: boolean;
    containerClassName?: string;
    contentContainerClassName?: string;
    listTitleComponent?: ReactElement;
    listTitleClassName?: string;
    paginationPosition?: "top" | "bottom";
}

const PaginatedList = <T, >({
                                id,
                                listTitle,
                                data,
                                isFetching,
                                isLoading,
                                page,
                                setPage,
                                renderItem,
                                emptyLabel,
                                emptyComponent,
                                hidePaginationOnSinglePages = false,
                                containerClassName,
                                contentContainerClassName,
                                listTitleComponent,
                                listTitleClassName,
                                paginationPosition = "top"
                            }: PaginatedListProps<T>): ReactElement | null => {
    const {t} = useTranslation();

    const emptyOrSinglePage = data?.totalPages == 0 || data?.totalPages == 1;

    return (
        <div id={id} className={cn(styles.container, containerClassName)}>
            <div className={styles.header}>
                {listTitleComponent ? listTitleComponent : <h2 className={listTitleClassName}>{listTitle}</h2>}
                {!(hidePaginationOnSinglePages && emptyOrSinglePage) && data && paginationPosition == "top" && (
                    <Pagination
                        id={`${id}-paginator`}
                        className={styles.pagination}
                        pageNumber={data.number}
                        pageSize={data.size}
                        totalPages={data.totalPages}
                        totalElements={data.totalElements}
                        hasPrevious={data.hasPrevious}
                        hasNext={data.hasNext}
                        isLoading={isLoading || isFetching}
                        page={page}
                        setPage={setPage}
                    />
                )}
            </div>

            <div className={cn(styles.content, styles.gradientBg, contentContainerClassName)}>
                {data?.content.map(renderItem)}

                {data && !data.hasContent && (emptyComponent || emptyLabel) && (
                    <div className={styles.emptyStateContainer}>
                        {emptyComponent}
                        {emptyLabel && <div className={styles.emptyLabel}>{emptyLabel}</div>}
                    </div>
                )}
            </div>

            <div className={styles.header}>
                <div className="flex-1"/>
                {!(hidePaginationOnSinglePages && emptyOrSinglePage) && data && paginationPosition == "bottom" && (
                    <Pagination
                        id={`${id}-paginator`}
                        className={styles.pagination}
                        pageNumber={data.number}
                        pageSize={data.size}
                        totalPages={data.totalPages}
                        totalElements={data.totalElements}
                        hasPrevious={data.hasPrevious}
                        hasNext={data.hasNext}
                        isLoading={isLoading || isFetching}
                        page={page}
                        setPage={setPage}
                    />
                )}
            </div>
            {isFetching && !data && (
                <div className={styles.loading}>
                    <CircularLoading size={12}/>
                </div>
            )}
        </div>
    );
};

export default PaginatedList;
