import React from "react";
import styles from "./CompareTable.module.scss";

interface CompareTableProps {
  competitor: string;
  /** One row per topic: [topic, Jinear, competitor]. */
  rows: [string, string, string][];
}

/**
 * Side-by-side table for comparison pages. MDX here has no GFM, so Markdown
 * pipe tables do not render; pages use <CompareTable competitor="..." rows={[...]} />.
 */
const CompareTable: React.FC<CompareTableProps> = ({ competitor, rows }) => {
  return (
    <div className={styles.scroll}>
      <table className={styles.table}>
        <thead>
          <tr>
            <th scope="col"></th>
            <th scope="col">Jinear</th>
            <th scope="col">{competitor}</th>
          </tr>
        </thead>
        <tbody>
          {rows.map(([topic, jinear, other]) => (
            <tr key={topic}>
              <th scope="row">{topic}</th>
              <td>{jinear}</td>
              <td>{other}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CompareTable;
