import React, {type KeyboardEvent, useEffect, useMemo, useRef} from 'react';
import cn from "classnames";
import {format, isToday, isYesterday} from "date-fns";
import styles from './NoteHistoryVersionStrip.module.css';
import type {RichTextVersionDto} from "@/model/be/jinear-core";
import useTranslation from "@/locals/useTranslation.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import ProfilePhoto from "@/components/profilePhoto";

interface NoteHistoryVersionStripProps {
    versions: RichTextVersionDto[];
    activeVersionId?: string;
    latestVersionId?: string;
    hasNextPage: boolean;
    isFetchingNextPage: boolean;
    isLoading: boolean;
    onSelect: (richTextVersionId: string) => void;
    onLoadMore: () => void;
}

interface VersionDayGroup {
    dayKey: string;
    date: Date;
    versions: RichTextVersionDto[];
}

interface VersionEditorRun {
    runKey: string;
    version: RichTextVersionDto;
    versions: RichTextVersionDto[];
}

const STEP_BY_KEY: Record<string, number> = {
    ArrowLeft: -1,
    ArrowRight: 1
};

export const versionDateOf = (version: RichTextVersionDto) => new Date(version.lastUpdatedDate ?? version.createdDate);

const groupByDay = (versions: RichTextVersionDto[]) => versions.reduce<VersionDayGroup[]>((groups, version) => {
    const date = versionDateOf(version);
    const dayKey = format(date, "yyyy-MM-dd");
    const last = groups[groups.length - 1];
    if (last?.dayKey === dayKey) {
        last.versions.push(version);
        return groups;
    }
    return [...groups, {dayKey, date, versions: [version]}];
}, []);

const groupByEditorRun = (versions: RichTextVersionDto[]) => versions.reduce<VersionEditorRun[]>((runs, version) => {
    const last = runs[runs.length - 1];
    if (last?.version.accountId === version.accountId) {
        last.versions.push(version);
        return runs;
    }
    return [...runs, {runKey: version.richTextVersionId, version, versions: [version]}];
}, []);

const scrollVerticalWheelHorizontally = (event: WheelEvent) => {
    const strip = event.currentTarget as HTMLElement;
    const isVertical = Math.abs(event.deltaY) > Math.abs(event.deltaX);
    if (!isVertical || strip.scrollWidth <= strip.clientWidth) {
        return;
    }
    event.preventDefault();
    strip.scrollLeft += event.deltaY;
};

const NoteHistoryVersionStrip: React.FC<NoteHistoryVersionStripProps> = ({
                                                                             versions,
                                                                             activeVersionId,
                                                                             latestVersionId,
                                                                             hasNextPage,
                                                                             isFetchingNextPage,
                                                                             isLoading,
                                                                             onSelect,
                                                                             onLoadMore
                                                                         }) => {
    const {t, dateFnsLocale} = useTranslation();
    const stripRef = useRef<HTMLDivElement>(null);
    const groups = useMemo(() => groupByDay(versions), [versions]);
    const hasVersions = versions.length > 0;

    useEffect(() => {
        const strip = stripRef.current;
        strip?.addEventListener("wheel", scrollVerticalWheelHorizontally, {passive: false});
        return () => strip?.removeEventListener("wheel", scrollVerticalWheelHorizontally);
    }, [isLoading, hasVersions]);

    useEffect(() => {
        const chip = stripRef.current?.querySelector(`[data-version-id="${activeVersionId}"]`);
        chip?.scrollIntoView({block: "nearest", inline: "nearest", behavior: "smooth"});
    }, [activeVersionId]);

    const dayLabel = (date: Date) => {
        if (isToday(date)) {
            return t("noteHistoryToday");
        }
        if (isYesterday(date)) {
            return t("noteHistoryYesterday");
        }
        return format(date, t("dateFormatShortMonthReadable"), {locale: dateFnsLocale});
    };

    const moveSelection = (event: KeyboardEvent<HTMLDivElement>) => {
        const step = STEP_BY_KEY[event.key];
        if (!step) {
            return;
        }
        event.preventDefault();
        const index = versions.findIndex(version => version.richTextVersionId === activeVersionId);
        const next = versions[index + step];
        if (next) {
            onSelect(next.richTextVersionId);
        }
    };

    if (isLoading) {
        return <div className={styles.placeholder}><CircularLoading size={21}/></div>;
    }

    if (!hasVersions) {
        return <div className={styles.placeholder}>{t("noteHistoryEmpty")}</div>;
    }

    return (
        <div ref={stripRef} className={styles.strip} tabIndex={0} onKeyDown={moveSelection}>
            <div className={styles.track}>
                {groups.map(group => (
                    <div key={group.dayKey} className={styles.group}>
                        <div className={styles.dayLabel}>{dayLabel(group.date)}</div>
                        <div className={styles.runs}>
                            {groupByEditorRun(group.versions).map(run => (
                                <div key={run.runKey} className={styles.run}>
                                    <ProfilePhoto
                                        boringAvatarKey={run.version.accountId}
                                        url={run.version.account?.profilePicture?.url}
                                        wrapperClassName={styles.profilePhoto}
                                    />
                                    <span className={styles.username}>{run.version.account?.username}</span>
                                    <div className={styles.times}>
                                        {run.versions.map(version => (
                                            <button
                                                key={version.richTextVersionId}
                                                type={"button"}
                                                tabIndex={-1}
                                                data-version-id={version.richTextVersionId}
                                                className={cn(
                                                    styles.time,
                                                    version.richTextVersionId === latestVersionId && styles.latest,
                                                    version.richTextVersionId === activeVersionId && styles.active
                                                )}
                                                onClick={() => onSelect(version.richTextVersionId)}
                                            >
                                                {format(versionDateOf(version), t("timeFormat"))}
                                                {version.richTextVersionId === latestVersionId &&
                                                    <span className={styles.currentBadge}>{t("noteHistoryCurrentVersion")}</span>}
                                            </button>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
                {hasNextPage && (
                    <div className={styles.group}>
                        <div className={styles.dayLabel}/>
                        <div className={styles.runs}>
                            <button
                                type={"button"}
                                tabIndex={-1}
                                className={styles.loadMore}
                                disabled={isFetchingNextPage}
                                onClick={onLoadMore}
                            >
                                {isFetchingNextPage ? <CircularLoading size={14}/> : t("noteHistoryLoadOlder")}
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default NoteHistoryVersionStrip;
