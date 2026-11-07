import React, {type ChangeEvent, useEffect, useRef, useState} from "react";
import cn from "classnames";
import {LuX} from "react-icons/lu";
import Modal from "../modal/Modal";
import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import useTranslation from "@/locales/useTranslation";
import {useAppDispatch, useTypedSelector} from "@/store";
import {useSearchWorkspaceQuery} from "@/store/api/workspaceSearchApi";
import {
    closeGlobalSearchModal,
    selectGlobalSearchModalVisible,
    selectGlobalSearchModalWorkspaceId,
    selectGlobalSearchModalWorkspaceUsername
} from "@/store/slice/modalSlice";
import {NAV_SKIP_ATTRIBUTE} from "@/keyboard/keymap";
import type {WorkspaceSearchType} from "@/model/be/jinear-core";
import SearchResultRows from "./searchResultRow/SearchResultRows";
import ScopedResultPage from "./scopedResultPage/ScopedResultPage";
import LoadMoreButton from "./scopedResultPage/LoadMoreButton";
import {type GlobalSearchTab, SEARCH_TABS, SEARCH_TYPES, sectionOf, TAB_LABELS, totalOf} from "./globalSearchSections";
import styles from "./GlobalSearchModal.module.css";

interface GlobalSearchModalProps {
}

const ALL_TAB_SECTION_LIMIT = 5;
const SEARCH_DEBOUNCE_MS = 300;

const GlobalSearchModal: React.FC<GlobalSearchModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();
    const visible = useTypedSelector(selectGlobalSearchModalVisible);
    const workspaceId = useTypedSelector(selectGlobalSearchModalWorkspaceId);
    const workspaceUsername = useTypedSelector(selectGlobalSearchModalWorkspaceUsername);
    const inputRef = useRef<HTMLInputElement>(null);
    const listRef = useRef<HTMLDivElement>(null);
    const [input, setInput] = useState<string>("");
    const [searchValue, setSearchValue] = useState<string>("");
    const [tab, setTab] = useState<GlobalSearchTab>("ALL");
    const [extraPageCount, setExtraPageCount] = useState<number>(0);

    const {currentData: response, isFetching} = useSearchWorkspaceQuery(
        {body: {workspaceId: workspaceId || "", query: searchValue}, page: 0},
        {skip: !visible || workspaceId == null || searchValue == ""}
    );
    const result = response?.data;
    const hasAnyResult = SEARCH_TYPES.some(type => totalOf(result, type) > 0);

    useEffect(() => {
        setInput("");
        setSearchValue("");
        setTab("ALL");
        inputRef.current?.focus();
    }, [visible]);

    useEffect(() => {
        setExtraPageCount(0);
        listRef.current?.scrollTo({top: 0});
    }, [searchValue, tab]);

    useDebouncedEffect(() => setSearchValue(input.trim()), [input], SEARCH_DEBOUNCE_MS);

    const close = () => {
        dispatch(closeGlobalSearchModal());
    };

    const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
        setInput(e.target.value);
    };

    const clear = () => {
        setInput("");
        setSearchValue("");
        inputRef.current?.focus();
    };

    const loadMore = () => {
        setExtraPageCount(count => count + 1);
    };

    const renderTab = (searchTab: GlobalSearchTab) => {
        const total = searchTab == "ALL" ? 0 : totalOf(result, searchTab);
        return (
            <Button
                key={searchTab}
                className={cn(styles.tab, tab == searchTab && styles.activeTab)}
                variant={tab == searchTab ? ButtonVariants.contrast : ButtonVariants.hoverFilled2}
                onClick={() => setTab(searchTab)}
            >
                {t(TAB_LABELS[searchTab])}
                {total > 0 && <span className={styles.tabCount}>{total}</span>}
            </Button>
        );
    };

    const renderSection = (type: WorkspaceSearchType) => {
        if (totalOf(result, type) == 0) {
            return null;
        }
        return (
            <div key={type} className={styles.section}>
                <div className={styles.sectionHeader}>
                    <span className={styles.sectionTitle}>{t(TAB_LABELS[type])}</span>
                    <Button
                        className={styles.seeAll}
                        variant={ButtonVariants.link}
                        onClick={() => setTab(type)}
                        {...{[NAV_SKIP_ATTRIBUTE]: true}}
                    >
                        {isMobile ? t("globalSearchModalTabAll") : t("globalSearchModalSeeAll")}
                    </Button>
                </div>
                <SearchResultRows
                    type={type}
                    result={result}
                    limit={ALL_TAB_SECTION_LIMIT}
                    query={searchValue}
                    workspaceUsername={workspaceUsername}
                    isMobile={isMobile}
                    onSelect={close}
                />
            </div>
        );
    };

    const renderScoped = (type: WorkspaceSearchType) => (
        <div className={styles.section}>
            <SearchResultRows
                type={type}
                result={result}
                query={searchValue}
                workspaceUsername={workspaceUsername}
                isMobile={isMobile}
                onSelect={close}
            />
            {Array.from({length: extraPageCount}, (_, index) => index + 1).map(page => (
                <ScopedResultPage
                    key={page}
                    workspaceId={workspaceId || ""}
                    workspaceUsername={workspaceUsername}
                    query={searchValue}
                    type={type}
                    page={page}
                    isLast={page == extraPageCount}
                    isMobile={isMobile}
                    onLoadMore={loadMore}
                    onSelect={close}
                />
            ))}
            {extraPageCount == 0 && sectionOf(result, type)?.hasNext && <LoadMoreButton onClick={loadMore}/>}
        </div>
    );

    const renderMessage = () => {
        if (searchValue == "") {
            return t("globalSearchModalInitialState");
        }
        if (isFetching && result == null) {
            return <CircularLoading size={17}/>;
        }
        if (result && (tab == "ALL" ? !hasAnyResult : totalOf(result, tab) == 0)) {
            return t("globalSearchModalEmptyState");
        }
        return null;
    };

    const message = renderMessage();

    return (
        <Modal
            visible={visible}
            requestClose={close}
            width={isMobile ? "fullscreen" : "large"}
            contentContainerClass={styles.modalContent}
            bodyClass={cn(styles.container, isMobile && styles.mobile)}
        >
            <div className={styles.header}>
                <div className={styles.inputContainer}>
                    <input
                        ref={inputRef}
                        id="global-search-input"
                        name="query"
                        type="text"
                        autoFocus
                        className={styles.searchInput}
                        placeholder={t("globalSearchModalPlaceholder")}
                        value={input}
                        onChange={onTextChange}
                    />
                    {input != "" && (
                        <Button
                            className={styles.clearButton}
                            title={t("globalSearchModalClear")}
                            onClick={clear}
                            {...{[NAV_SKIP_ATTRIBUTE]: true}}
                        >
                            <LuX/>
                        </Button>
                    )}
                    {!isMobile && (
                        <Button className={styles.escButton} onClick={close} {...{[NAV_SKIP_ATTRIBUTE]: true}}>
                            esc
                        </Button>
                    )}
                </div>
                {isMobile && (
                    <Button
                        className={styles.cancelButton}
                        variant={ButtonVariants.link}
                        onClick={close}
                        {...{[NAV_SKIP_ATTRIBUTE]: true}}
                    >
                        {t("globalSearchModalCancel")}
                    </Button>
                )}
            </div>
            <div className={styles.tabs} {...{[NAV_SKIP_ATTRIBUTE]: true}}>
                {SEARCH_TABS.map(renderTab)}
            </div>
            <div ref={listRef} className={styles.list}>
                {searchValue != "" && result && (
                    tab == "ALL" ? SEARCH_TYPES.map(renderSection) : renderScoped(tab)
                )}
                {message && <div className={styles.messageContainer}>{message}</div>}
            </div>
        </Modal>
    );
};

export default GlobalSearchModal;
