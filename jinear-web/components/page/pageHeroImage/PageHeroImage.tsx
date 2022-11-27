import Image from "next/image";
import React from "react";
import styles from "./PageHeroImage.module.css";

interface PageHeroImageProps {
  alt?: string;
  src: string;
  objectFit?: "contain" | "cover" | "fill" | "none" | "scale-down";
  layout?: "responsive" | "fill" | "intrinsic" | "fixed";
  fitWithBgBlur?: boolean;
}

const PageHeroImage: React.FC<PageHeroImageProps> = ({
  alt,
  src,
  objectFit,
  layout,
  fitWithBgBlur = false,
}) => {
  const _objectFit = objectFit || (fitWithBgBlur ? "scale-down" : "cover");
  const _layout = layout || (fitWithBgBlur ? "fill" : "fill");
  return (
    <div className={styles.container}>
      {fitWithBgBlur && (
        <>
          <Image
            alt={alt || ""}
            src={src}
            className={styles.blurredImg}
            objectFit={"fill"}
            layout="fill"
          />
          <div className={styles.imgBlur} />
        </>
      )}
      <Image
        className={styles.image}
        alt={alt || ""}
        src={src}
        sizes="(min-width: 525px) 95vw, 70vw"
        objectFit={_objectFit}
        layout={_layout}
        priority
      />
    </div>
  );
};

export default PageHeroImage;
