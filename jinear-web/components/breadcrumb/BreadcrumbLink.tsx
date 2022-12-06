import Link from "next/link";
import React from "react";
import styles from "./BreadcrumbLink.module.css";

interface BreadcrumbLinkProps {
  label: string;
  url: string;
}

const BreadcrumbLink: React.FC<BreadcrumbLinkProps> = ({ label, url }) => {
  return (
    <Link href={url} className={styles.link}>
      {label}
    </Link>
  );
};

export default BreadcrumbLink;
