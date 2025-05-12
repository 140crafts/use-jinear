import React from "react";
import styles from "./FeatureCard.module.scss";
import { IconType } from "react-icons/lib";
import cn from "classnames";

interface FeatureCardIconInfo {
  title: string;
  text: string;
  Icon: IconType;
  iconColor?: string;
  id: string;
}

interface AlternativeToInfo {
  name: string;
  Icon: IconType;
  id: string;
}

interface FeatureCardProps {
  title1: string;
  title2?: string;
  text: string;
  imageUrl: string;
  featureCardIconInfoList?: FeatureCardIconInfo[];
  alternativeToInfoList?: AlternativeToInfo[];
  alternativeToLabel?:string
}

const FeatureCard: React.FC<FeatureCardProps> = ({
                                                   title1,
                                                   title2,
                                                   text,
                                                   imageUrl,
                                                   featureCardIconInfoList = [],
                                                   alternativeToInfoList = [],
                                                   alternativeToLabel
                                                 }) => {
  return (
    <div className={styles.container}>

      <div className={styles.heroContainer}>

        <div className={styles.heroTextContainer}>
          <div className={styles.heroTitle}>
            <span className={styles.line} dangerouslySetInnerHTML={{ __html: title1 }}></span>
            {title2 && <br />}
            <span className={styles.line}>{title2}</span>
          </div>

          <span className={styles.heroText}>{text}</span>
        </div>

        {featureCardIconInfoList.length != 0 && <div className={styles.heroIconListContainer}>
          {featureCardIconInfoList.map(iconInfo =>
            <div key={iconInfo.id} className={styles.iconFeatureContainer}>
              <iconInfo.Icon className={styles.featureIcon} color={iconInfo.iconColor} />
              <span className={styles.featureIconTitle}>{iconInfo.title}</span>
              <span className={styles.featureIconText}>{iconInfo.text}</span>
            </div>
          )}
        </div>}

      </div>

      <div className={"spacer-h-2"} />

      {/* eslint-disable-next-line @next/next/no-img-element */}
      <img src={imageUrl} alt={"feature"} />

      <div className={"spacer-h-2"} />

      {alternativeToInfoList.length != 0 && <div className={styles.alternativeToContainer}>
        <b>{alternativeToLabel} </b>
        {alternativeToInfoList.map(alternativeTo =>
          <div key={alternativeTo.id} className={styles.alternativeToElement}>
            <alternativeTo.Icon size={14} />
            <span className={cn(styles.alternativeToName, "noselect")}>{alternativeTo.name}</span>
          </div>
        )}
      </div>}

    </div>
  );
};

export default FeatureCard;
