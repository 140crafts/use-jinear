import Image from "next/image";
import React from "react";
import styles from "./FeatureCard.module.scss";

interface FeatureCardProps {
  title: string;
  text: string;
  imageUrl: string;
}

const FeatureCard: React.FC<FeatureCardProps> = ({ title, text, imageUrl }) => {
  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{title}</h2>
      <div className={styles.text}>{text}</div>
      <div className="spacer-h-2" />
      <div className={styles.imageContainer}>
        <Image alt={`image for ${title}`} src={imageUrl} fill={true} objectFit="contain" />
      </div>
    </div>
  );
};

export default FeatureCard;
