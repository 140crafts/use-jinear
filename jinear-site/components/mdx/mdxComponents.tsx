import type { FC, AnchorHTMLAttributes } from "react";
import { MDXRemote, type MDXRemoteProps } from "next-mdx-remote/rsc";
import VideoEmbed from "@/components/videoEmbed/VideoEmbed";
import CompareTable from "@/components/compareTable/CompareTable";

// `next-mdx-remote/rsc`'s MDXRemote is an async Server Component (returns a
// Promise), which React 18's JSX types reject (TS2786). It renders correctly at
// runtime (RSC awaits it), so alias it to a plain component type for the editor.
export const Mdx = MDXRemote as unknown as FC<MDXRemoteProps>;

// Shared by blog posts and comparison pages.
export const mdxComponents = {
  // Render external links (http/https) in a new tab; keep in-site links in-tab.
  a: ({ href, children, ...props }: AnchorHTMLAttributes<HTMLAnchorElement>) =>
    href?.startsWith("http") ? (
      <a href={href} target="_blank" rel="noopener noreferrer" {...props}>
        {children}
      </a>
    ) : (
      <a href={href} {...props}>
        {children}
      </a>
    ),
  // Lets a post drop in a video with <VideoEmbed id="..." title="..." />.
  VideoEmbed,
  CompareTable,
};
