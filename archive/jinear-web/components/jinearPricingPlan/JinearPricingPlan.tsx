import React from "react";
import styles from "./JinearPricingPlan.module.css";
import cn from "classnames";

interface JinearPricingPlanProps {
  planName: string;
  price: string | React.ReactNode;
  description: string | React.ReactNode;
  featuresTitle: string | React.ReactNode;
  contrast?: boolean;
  children?: React.ReactNode;
}

const JinearPricingPlan: React.FC<JinearPricingPlanProps> = ({
                                                               planName,
                                                               price,
                                                               description,
                                                               featuresTitle,
                                                               contrast,
                                                               children
                                                             }) => {

  return (
    <div className={cn(styles.container, contrast && styles.contrast)}>
      <h2 className={styles.title}>{planName}</h2>
      <div className={"spacer-h-2"} />
      <h1 className={styles.price}>{price}</h1>
      <div className={"spacer-h-4"} />
      <span className={styles.description}>{description}</span>

      <hr className={cn(styles.dash, contrast && styles.dashContrast)} />

      <span>
        {featuresTitle}
      </span>
      <div className={"spacer-h-2"} />

      <div className={cn(styles.children, contrast && styles.childrenContrast)}>
        {children}
      </div>
    </div>
  );
};

export default JinearPricingPlan;