import { createCollection, type Entry, type EntryFrontmatter, type EntryMeta } from "./content";

// File-based comparison pages ("Jinear vs X"): one Markdown/MDX file per page
// under content/compare/, served at /compare/<slug>/ and kept out of the blog.

export interface ComparisonFrontmatter extends EntryFrontmatter {
  /** The other product's name, e.g. "Trello", or the alternative, e.g. "DIY with AI". */
  competitor: string;
  /** Set only when the alternative is a product; drives the "checked on" note and JSON-LD. */
  competitorUrl?: string;
  /** ISO date the competitor's pricing and features were last checked. Prices drift. */
  checkedDate?: string;
}

export type ComparisonMeta = EntryMeta<ComparisonFrontmatter>;
export type Comparison = Entry<ComparisonFrontmatter>;

const comparisons = createCollection<ComparisonFrontmatter>("compare");

export const getComparison = comparisons.getEntry;
export const getAllComparisonsWithContent = comparisons.getAllWithContent;
export const getPublishedComparisonSlugs = comparisons.getPublishedSlugs;

const COMPARE_TABLE = /<CompareTable\s+competitor="([^"]+)"\s+rows=\{(\[[\s\S]*?\])\}\s*\/>/g;

/**
 * The MDX body with each <CompareTable /> turned into a Markdown pipe table, for
 * the plain-text /llms-full.txt. Rows must be written as JSON (double quotes);
 * trailing commas are allowed.
 */
export function toPlainMarkdown(content: string): string {
  return content.replace(COMPARE_TABLE, (_match, competitor: string, rowsSource: string) => {
    const rows = JSON.parse(rowsSource.replace(/,(\s*[\]}])/g, "$1")) as [string, string, string][];
    const cell = (value: string) => value.replace(/\|/g, "\\|");
    return [
      `| | Jinear | ${competitor} |`,
      "|---|---|---|",
      ...rows.map((row) => `| ${row.map(cell).join(" | ")} |`),
    ].join("\n");
  });
}

/** Alphabetical by competitor: a reader scans this list for a name, not a date. */
export function getAllComparisons(): ComparisonMeta[] {
  return comparisons.getAll().sort((a, b) => a.competitor.localeCompare(b.competitor));
}
