import { readFileSync } from "fs";
import path from "path";
import { ImageResponse } from "next/og";
import { getComparison, getPublishedComparisonSlugs } from "@/lib/comparisons";

// Emitted as /compare/<slug>/og.png at `next build`: the social card for that comparison.
export const dynamic = "force-static";
export const dynamicParams = false;

const OG_SIZE = { width: 1200, height: 630 };

export function generateStaticParams() {
  return getPublishedComparisonSlugs().map((slug) => ({ slug }));
}

const font = (weight: number) => readFileSync(path.join(process.cwd(), "assets/fonts", `HankenGrotesk-${weight}.ttf`));

export function GET(_request: Request, { params }: { params: { slug: string } }) {
  const comparison = getComparison(params.slug);
  const [headline, subtitle] = (comparison?.title ?? "Jinear compared").split(/:\s*/);

  return new ImageResponse(
    (
      <div
        style={{
          width: "100%",
          height: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          padding: "72px 80px",
          background: "#fbfbfa",
          color: "#1c1a16",
          fontFamily: "Hanken Grotesk",
          borderLeft: "16px solid #d96a1a",
        }}
      >
        <div style={{ display: "flex", fontSize: 30, fontWeight: 800, letterSpacing: "0.2em" }}>
          JINEAR<span style={{ color: "#d96a1a" }}>.</span>
        </div>
        <div style={{ display: "flex", flexDirection: "column" }}>
          <div style={{ fontSize: 84, fontWeight: 800, lineHeight: 1.05, letterSpacing: "-0.03em" }}>{headline}</div>
          {subtitle ? <div style={{ fontSize: 40, fontWeight: 500, color: "#4f4b43", marginTop: 24 }}>{subtitle}</div> : null}
        </div>
        <div style={{ display: "flex", fontSize: 26, fontWeight: 500, color: "#8b857a" }}>jinear.co/compare</div>
      </div>
    ),
    {
      ...OG_SIZE,
      fonts: [
        { name: "Hanken Grotesk", data: font(500), weight: 500, style: "normal" },
        { name: "Hanken Grotesk", data: font(800), weight: 800, style: "normal" },
      ],
    }
  );
}
