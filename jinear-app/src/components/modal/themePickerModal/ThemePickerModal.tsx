import Button from "@/components/button";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import {useThemeSettings} from "@/components/themeProvider/ThemeProvider";
import useWindowSize from "@/hooks/useWindowSize";
import useTranslation, {type StringKeys} from "@/locales/useTranslation";
import {useAppDispatch, useTypedSelector} from "@/store";
import {closeThemePickerModal, selectThemePickerModalVisible} from "@/store/slice/modalSlice";
import {THEME_FONTS} from "@/theme/themeFonts";
import {ALL_THEMES} from "@/theme/themeRegistry";
import type {CaretShape, ThemeAppearance, ThemeDefinition, ThemeOrigin} from "@/theme/themeTypes";
import cn from "classnames";
import React, {type ChangeEvent, type KeyboardEvent, useCallback, useEffect, useRef, useState} from "react";
import {IoCheckmark} from "react-icons/io5";
import Modal from "../modal/Modal";
import ThemeSwatch from "./themeSwatch/ThemeSwatch";
import styles from "./ThemePickerModal.module.css";

interface ThemePickerModalProps {
}

type AppearanceFilter = "all" | ThemeAppearance;

const CARET_SHAPES: CaretShape[] = ["auto", "bar", "block", "underscore"];

const ORIGIN_LABEL_KEYS: Record<ThemeOrigin, StringKeys> = {
    jinear: "themePickerOriginJinear",
    vscode: "themePickerOriginVscode",
    jetbrains: "themePickerOriginJetbrains",
    popular: "themePickerOriginPopular",
};

const CARET_LABEL_KEYS: Record<CaretShape, StringKeys> = {
    auto: "themePickerCaretAuto",
    bar: "themePickerCaretBar",
    block: "themePickerCaretBlock",
    underscore: "themePickerCaretUnderscore",
};

const caretShapeSupported = () => typeof CSS != "undefined" && CSS.supports("caret-shape", "block");

const filterThemes = (query: string, filter: AppearanceFilter): ThemeDefinition[] => {
    const normalizedQuery = query.trim().toLowerCase();
    return ALL_THEMES.filter(theme =>
        (filter == "all" || theme.appearance == filter) &&
        (normalizedQuery == "" || `${theme.name} ${theme.origin}`.toLowerCase().includes(normalizedQuery))
    );
};

const ThemePickerModal: React.FC<ThemePickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectThemePickerModalVisible);
    const {isMobile} = useWindowSize();
    const {preferences, selectTheme, previewTheme, setFontId, setCaretShape} = useThemeSettings();

    const listRef = useRef<HTMLDivElement | null>(null);
    const [query, setQuery] = useState<string>("");
    const [filter, setFilter] = useState<AppearanceFilter>("all");
    const [activeThemeId, setActiveThemeId] = useState<string>(preferences.themeId);

    const themes = filterThemes(query, filter);
    const activeTheme: ThemeDefinition | undefined = themes.find(item => item.id == activeThemeId) ?? themes[0];

    const scrollToTheme = (themeId: string) =>
        listRef.current?.querySelector(`[data-theme-id="${themeId}"]`)?.scrollIntoView({block: "nearest"});

    useEffect(() => {
        if (visible) {
            setQuery("");
            setFilter("all");
            setActiveThemeId(preferences.themeId);
        }
    }, [visible]);

    // BaseModal mounts its children a render after `visible` turns true, so scroll once the list itself mounts.
    // The callback must stay stable: a new function on every render would rerun it and scroll on each hover preview.
    const attachList = useCallback((element: HTMLDivElement | null) => {
        listRef.current = element;
        element?.querySelector("[data-current-theme]")?.scrollIntoView({block: "center"});
    }, []);

    const close = () => {
        previewTheme(null);
        dispatch(closeThemePickerModal());
    };

    const apply = (themeId: string) => {
        selectTheme(themeId);
        dispatch(closeThemePickerModal());
    };

    const highlight = (themeId: string) => {
        setActiveThemeId(themeId);
        previewTheme(themeId);
    };

    const onKeyDown = (event: KeyboardEvent<HTMLDivElement>) => {
        if (event.key == "Escape") {
            event.preventDefault();
            close();
            return;
        }
        if ((event.target as HTMLElement).tagName == "SELECT" || !activeTheme) {
            return;
        }
        if (event.key == "ArrowDown" || event.key == "ArrowUp") {
            event.preventDefault();
            const step = event.key == "ArrowDown" ? 1 : -1;
            const next = themes[(themes.indexOf(activeTheme) + step + themes.length) % themes.length];
            highlight(next.id);
            scrollToTheme(next.id);
        } else if (event.key == "Enter") {
            event.preventDefault();
            apply(activeTheme.id);
        }
    };

    const onQueryChange = (event: ChangeEvent<HTMLInputElement>) => {
        setQuery(event.target.value);
    };

    const onFilterChange = (value: string) => {
        setFilter(value as AppearanceFilter);
    };

    return (
        <Modal
            visible={visible}
            title={t("themePickerModalTitle")}
            bodyClass={styles.body}
            requestClose={close}
            width={isMobile ? "fullscreen" : "large"}
            hasTitleCloseButton={true}
        >
            <div className={styles.container} onKeyDown={onKeyDown}>
                <div className={styles.searchBar}>
                    <input
                        autoFocus={true}
                        type={"text"}
                        className={styles.searchInput}
                        placeholder={t("themePickerSearchPlaceholder")}
                        value={query}
                        onChange={onQueryChange}
                    />
                    <SegmentedControl
                        id={"theme-picker-appearance-filter"}
                        name={"theme-picker-appearance-filter"}
                        defaultIndex={0}
                        callback={onFilterChange}
                        segments={[
                            {label: t("themePickerFilterAll"), value: "all"},
                            {label: t("themePickerFilterLight"), value: "light"},
                            {label: t("themePickerFilterDark"), value: "dark"},
                        ]}
                    />
                </div>

                <div className={styles.themeList} ref={attachList} onMouseLeave={() => previewTheme(null)}>
                    {themes.map((item, index) => (
                        <React.Fragment key={item.id}>
                            {(index == 0 || themes[index - 1].origin != item.origin) &&
                                <div className={styles.originTitle}>{t(ORIGIN_LABEL_KEYS[item.origin])}</div>}
                            <Button
                                className={cn(styles.themeRow, item.id == activeTheme?.id && styles.activeThemeRow)}
                                data-theme-id={item.id}
                                data-current-theme={item.id == preferences.themeId || undefined}
                                onMouseOver={() => item.id != activeTheme?.id && highlight(item.id)}
                                onClick={() => apply(item.id)}
                            >
                                <ThemeSwatch theme={item}/>
                                <span className={styles.themeName}>{item.name}</span>
                                {item.id == preferences.themeId && <IoCheckmark size={15} className={styles.currentIcon}/>}
                            </Button>
                        </React.Fragment>
                    ))}
                    {themes.length == 0 && <div className={styles.emptyState}>{t("themePickerEmptyState")}</div>}
                </div>

                <div className={styles.options}>
                    <div className={styles.selectRow}>
                        <label htmlFor={"theme-picker-font"}>{t("themePickerFontLabel")}</label>
                        <select id={"theme-picker-font"} value={preferences.fontId}
                                onChange={event => setFontId(event.target.value)}>
                            {THEME_FONTS.map(font => <option key={font.id} value={font.id}>{font.name}</option>)}
                        </select>
                    </div>
                    {caretShapeSupported() &&
                        <div className={styles.selectRow}>
                            <label htmlFor={"theme-picker-caret"}>{t("themePickerCaretLabel")}</label>
                            <select id={"theme-picker-caret"} value={preferences.caretShape}
                                    onChange={event => setCaretShape(event.target.value as CaretShape)}>
                                {CARET_SHAPES.map(shape => <option key={shape} value={shape}>{t(CARET_LABEL_KEYS[shape])}</option>)}
                            </select>
                        </div>}
                    {!isMobile && <span className={styles.hint}>{t("themePickerKeyboardHint")}</span>}
                </div>
            </div>
        </Modal>
    );
};

export default ThemePickerModal;
