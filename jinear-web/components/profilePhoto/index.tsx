"use client";

import Logger from "@/utils/logger";
import cn from "classnames";
import Image from "next/image";
import React, { CSSProperties } from "react";
import styles from "./index.module.css";
import { S3_BASE } from "@/utils/constants";

const cyrb53 = function(str: string | undefined, seed = 0) {
  if (!str) {
    return 0;
  }
  let h1 = 0xdeadbeef ^ seed,
    h2 = 0x41c6ce57 ^ seed;
  for (let i = 0, ch; i < str.length; i++) {
    ch = str.charCodeAt(i);
    h1 = Math.imul(h1 ^ ch, 2654435761);
    h2 = Math.imul(h2 ^ ch, 1597334677);
  }
  h1 = Math.imul(h1 ^ (h1 >>> 16), 2246822507) ^ Math.imul(h2 ^ (h2 >>> 13), 3266489909);
  h2 = Math.imul(h2 ^ (h2 >>> 16), 2246822507) ^ Math.imul(h1 ^ (h1 >>> 13), 3266489909);
  return 4294967296 * (2097151 & h2) + (h1 >>> 0);
};

export const getRandomBoringAvatar = (word: string) => {
  const all = [
    "AliciaDickerson",
    "AmeliaBoynton",
    "AmeliaEarhart",
    "BessieColeman",
    "CarrieChapman",
    "CorettaScott",
    "DaisyGatson",
    "ElizabethCady",
    "ElizabethPeratrovich",
    "EmmaWillard",
    "EstherMartinez",
    "EuniceKennedy",
    "FabiolaCabeza",
    "HarrietTubman",
    "LucyStone",
    "MahaliaJackson",
    "MargaretBourke",
    "MargaretBrent",
    "MargaretChase",
    "MariaMitchell",
    "MarryBaker",
    "MaryEdwards",
    "MaryRoebling",
    "MayaAngelou",
    "Sacagawea",
    "SarahWinnemucca",
    "SojournerTruth",
    "WillaCather"
  ];
  let index = all?.indexOf(word);
  index = index == -1 ? cyrb53(word) : index;
  return "/images/svg/boringavatars/" + all[index % all.length] + ".svg";
};

interface ProfilePhotoProps {
  boringAvatarKey: string;
  appFileId?: string;
  storagePath?: string;
  wrapperClassName?: string;
  imgClassName?: string;
  objectFit?: string;
  fill?: boolean;
  style?: CSSProperties;
  dataTooltipRight?: string;
}

const logger = Logger("ProfilePhoto");

const ProfilePhoto: React.FC<ProfilePhotoProps> = ({
                                                     boringAvatarKey = "",
                                                     appFileId,
                                                     storagePath,
                                                     wrapperClassName,
                                                     imgClassName,
                                                     objectFit,
                                                     fill,
                                                     style,
                                                     dataTooltipRight
                                                   }) => {
  return (
    <div className={cn(styles.wrapper, wrapperClassName)} data-tooltip-right={dataTooltipRight}>
      <Image
        alt="User profile photo"
        className={cn(styles.image, imgClassName)}
        style={style}
        src={storagePath ? S3_BASE + storagePath : getRandomBoringAvatar(boringAvatarKey)}
        sizes="(max-width: 320px) 90px, (max-width: 760px) 125px, 175px"
        // @ts-ignore
        objectFit={objectFit || "cover"}
        layout="fill"
        fill={fill}
      />
    </div>
  );
};

export default ProfilePhoto;
