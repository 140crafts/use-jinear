import cn from "classnames";
import Link from "next/link";
import React from "react";
import styles from "./BreadcrumbLink.module.css";

interface BreadcrumbLinkProps {
  label: string;
  url: string;
}

const MAX_WIDTH = 59;
const PART_SIZE = (MAX_WIDTH - 3) / 2;

const BreadcrumbLink: React.FC<BreadcrumbLinkProps> = ({ label, url }) => {
  const isTooLong = label?.length > MAX_WIDTH;
  return (
    <Link
      href={url}
      className={cn(styles.link, "line-clamp")}
      data-tooltip-multiline={isTooLong ? label : undefined}
    >
      {isTooLong
        ? `${label.substring(0, PART_SIZE)}...${label.substring(
            label.length - PART_SIZE,
            label.length
          )}`
        : label}
    </Link>
  );
};

export default BreadcrumbLink;
