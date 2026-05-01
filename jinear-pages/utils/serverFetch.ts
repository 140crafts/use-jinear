const SERVER_API_ROOT =
  process.env.NEXT_PUBLIC_API_ROOT ||
  (process.env.NODE_ENV === "development"
    ? "http://localhost:8085/"
    : "https://api.jinear.co/");

const REVALIDATE_SECONDS = 300;

async function safeFetchJson<T>(path: string): Promise<T | null> {
  try {
    const res = await fetch(`${SERVER_API_ROOT}${path}`, {
      next: { revalidate: REVALIDATE_SECONDS },
    });
    if (!res.ok) return null;
    return (await res.json()) as T;
  } catch {
    return null;
  }
}

export async function fetchPublicProjectInfoByDomain(
  domain: string
): Promise<any | null> {
  if (!domain) return null;
  return safeFetchJson<any>(
    `v1/project/public-feed/custom-domain/${encodeURIComponent(domain)}/info`
  );
}

export async function fetchPublicProjectInfo(
  projectId: string
): Promise<any | null> {
  if (!projectId) return null;
  return safeFetchJson<any>(`v1/project/public-feed/${projectId}/info`);
}

export async function fetchProjectFeedPage(
  projectId: string,
  page: number = 0
): Promise<any | null> {
  if (!projectId) return null;
  return safeFetchJson<any>(`v1/project/public-feed/${projectId}?page=${page}`);
}

export async function fetchProjectFeedPost(
  projectId: string,
  postId: string
): Promise<any | null> {
  if (!projectId || !postId) return null;
  return safeFetchJson<any>(`v1/project/public-feed/${projectId}/${postId}`);
}

function walkTiptapText(node: any): string {
  if (!node) return "";
  if (typeof node === "string") return node;
  if (typeof node.text === "string") return node.text;
  if (Array.isArray(node.content)) {
    return node.content.map(walkTiptapText).join(" ");
  }
  return "";
}

export function extractRichTextPlain(value: string | null | undefined): string {
  if (!value) return "";
  const trimmed = value.trim();
  if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
    try {
      const parsed = JSON.parse(trimmed);
      const text = walkTiptapText(parsed).replace(/\s+/g, " ").trim();
      if (text) return text;
    } catch {
      // fall through to HTML strip
    }
  }
  return trimmed
    .replace(/<[^>]+>/g, " ")
    .replace(/\s+/g, " ")
    .trim();
}
