import React, { useEffect, useRef, useState } from "react";
import styles from "./VideoHeroSection.module.scss";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuAlarmClock, LuBox, LuCalendarSearch, LuCheckSquare, LuFile, LuList, LuRss, LuUsers } from "react-icons/lu";
import { SiGooglecalendar } from "react-icons/si";
import useTranslation from "@/locals/useTranslation";
import { useTheme } from "@/components/themeProvider/ThemeProvider";
import Logger from "@/utils/logger";

interface VideoHeroSectionProps {
  title1: string;
  title2?: string;
  text?: string;
}

const videos = {
  "feature-projects": {
    light: "1018031703",
    dark: "1018031575",
    Icon: LuBox,
    label: "homePageFeatureTitle_NewProject",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/project-init/project-init-dark.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/project-init/project-init-light.mov"
  },
  "feature-project-feed-post": {
    light: "1018032005",
    dark: "1018031810",
    Icon: LuRss,
    label: "homePageFeatureTitle_NewProjectFeedPost",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/project-feed-post/project-feed-post-dark.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/project-feed-post/project-feed-post-light.mov"
  },
  "feature-new-team": {
    light: "988098284",
    dark: "988098247",
    Icon: LuUsers,
    label: "homePageFeatureTitle_NewTeam",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/new-team/dark%206.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/new-team/light%206.mov"
  },
  "feature-tasks": {
    light: "988098899",
    dark: "988098825",
    Icon: LuCheckSquare,
    label: "homePageFeatureTitle_Task",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/task-olusturma-kaydirma/dark.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/task-olusturma-kaydirma/light.mov"
  },
  "feature-task-filter": {
    light: "988098780",
    dark: "988098743",
    Icon: LuList,
    label: "homePageFeatureTitle_ListAndFilterYourTasks",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/task-listeleme/dark%205.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/task-listeleme/light%205.mov"
  },
  "feature-reminders": {
    light: "988098452",
    dark: "988098405",
    Icon: LuAlarmClock,
    label: "homePageFeatureTitle_Reminder",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/reminder/dark%204.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/reminder/light%204.mov"
  },
  "feature-files-and-comments": {
    light: "988098180",
    dark: "988098131",
    Icon: LuFile,
    label: "homePageFeatureTitle_FilesAndComments",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/file-upload-and-comment/dark%207.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/file-upload-and-comment/light%207.mov"
  },
  "feature-calendar-share": {
    light: "988098610",
    dark: "988098554",
    Icon: LuCalendarSearch,
    label: "homePageFeatureTitle_Share",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/takvim-apple-calendar-a-ekleme/dark%203.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/takvim-apple-calendar-a-ekleme/light%203.mov"
  },
  "feature-google-calendar-support": {
    light: "988098695",
    dark: "988098661",
    Icon: SiGooglecalendar,
    label: "homePageFeatureTitle_GoogleCalendar",
    srcDark: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/takvim-ekleme/dark%202.mov",
    srcLight: "https://storage.googleapis.com/jinear-b0/web-assets/videos/home-page/takvim-ekleme/light%202.mov"
  }
};

const logger = Logger("VideoHeroSection");

const VideoHeroSection: React.FC<VideoHeroSectionProps> = ({ title1, title2, text }) => {
  const { t } = useTranslation();
  const theme = useTheme();
  const videoRef = useRef<HTMLVideoElement>(null);
  const sourceRef = useRef<HTMLSourceElement>(null);

  const [selection, setSelection] = useState<keyof typeof videos>("feature-projects");

  useEffect(() => {
    if (selection && videoRef.current && sourceRef.current) {
      const selected = videos[selection];
      sourceRef.current.src = `${theme == "light" ? selected.srcLight : selected.srcDark}#t=0.1`;
      videoRef.current.load();
    }
  }, [theme, videoRef, sourceRef, selection]);

  return (
    <div className={styles.container}>

      <div className={styles.heroTextContainer}>
        <div className={styles.heroTitle}>
          <span className={styles.line} dangerouslySetInnerHTML={{ __html: title1 }}></span>
          {title2 && <br />}
          <span className={styles.line}>{title2}</span>
        </div>

        <span className={styles.heroText}>{text}</span>
      </div>

      <div id={"feature-projects"} className={styles.heroVideoContainer}>
        <video
          className={styles.video}
          muted={true}
          autoPlay={true}
          controls={true}
          loop={true}
          playsInline
          ref={videoRef}>
          <source ref={sourceRef} />
        </video>
      </div>

      <div className={styles.featuresNavbar}>
        {Object.keys(videos).map(
          videoKey => {
            const key = videoKey as keyof typeof videos;
            const video = videos[key];
            const Icon = video.Icon;
            const isSelected = selection == key;
            // @ts-ignore
            const label = t(video.label);
            return <Button
              key={videoKey}
              className={styles.navButton}
              variant={isSelected ? ButtonVariants.filled2 : ButtonVariants.outline}
              heightVariant={ButtonHeight.short}
              onClick={() => setSelection(key)}>
              <Icon className={"icon"} />
              <b>{label}</b>
            </Button>;
          })}
      </div>

    </div>
  );
};

export default VideoHeroSection;