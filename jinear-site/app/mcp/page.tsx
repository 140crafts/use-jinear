import { Metadata } from "next";
import Link from "next/link";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { buildMetadata } from "@/utils/seo";
import { MCP_DOCS_URL } from "@/utils/constants";
import manifest from "@/lib/mcp-tools.generated.json";
import styles from "./index.module.scss";

export const metadata: Metadata = buildMetadata({
  title: "Connect Claude or ChatGPT",
  description:
    "Connect Claude or ChatGPT to Jinear over MCP and work with your tasks, projects, notes and files from inside the assistant. Works on the hosted version and on your own server.",
  path: "/mcp/",
  ogTitle: "Jinear for Claude and ChatGPT",
  ogDescription:
    "Connect an AI assistant to Jinear over MCP. 31 tools, per-resource permissions, no delete tools.",
});

interface Tool {
  name: string;
  title: string;
  description: string;
  annotations: { readOnlyHint: boolean };
}

// The scope each permission covers, in the order the consent screen lists them.
const SCOPE_COPY: { scope: string; label: string; text: string }[] = [
  { scope: "workspace:read", label: "Workspaces", text: "Workspaces, teams, members, topics and workflow statuses." },
  { scope: "tasks:read", label: "Read tasks", text: "Tasks, boards and comments." },
  { scope: "tasks:write", label: "Write tasks", text: "Creating and updating tasks, boards and comments." },
  { scope: "projects:read", label: "Read projects", text: "Projects and milestones." },
  { scope: "projects:write", label: "Write projects", text: "Creating and updating projects and milestones." },
  { scope: "calendar:read", label: "Read calendar", text: "Calendar events, including any synced from Google Calendar." },
  { scope: "notes:read", label: "Read notes", text: "Notebooks and notes." },
  { scope: "files:read", label: "Read files", text: "Files, folders and links to them." },
  { scope: "offline_access", label: "Stay connected", text: "Keeps the connection alive until you disconnect it." },
];

const tools = manifest.tools as Tool[];
const scopes = manifest.scopes as Record<string, string[]>;

const toolsForScope = (scope: string) =>
  tools.filter((tool) => (scopes[tool.name] ?? []).includes(scope));

export default function McpPage() {
  const readOnlyCount = tools.filter((tool) => tool.annotations.readOnlyHint).length;

  return (
    <div className={styles.page}>
      <div className={styles.wrap}>
        <BareNav />

        <Link className={styles.back} href="/">
          ← Home
        </Link>

        <header className={styles.ahead}>
          <p className={styles.eyebrow}>MCP</p>
          <h1 className={styles.title}>Connect Claude or ChatGPT to Jinear</h1>
          <p className={styles.lede}>
            Jinear speaks MCP, so an AI assistant can read and change your work directly. Ask Claude what
            is assigned to you this week, have it write up a task from a conversation, or let ChatGPT cite
            a note while it drafts. It uses your account, and it can only do what you allow.
          </p>
        </header>

        <article className={styles.body}>
          <h2>Connect it</h2>
          <p>
            You need a server address. On the hosted version it is{" "}
            <code>https://api.jinear.co/mcp</code>. On your own server it is your API domain with{" "}
            <code>/mcp</code> on the end, and every member finds it on their profile page with a copy
            button.
          </p>

          <h3>In Claude</h3>
          <ol>
            <li>Open Settings, then Connectors.</li>
            <li>Choose Add custom connector and paste the address.</li>
            <li>Sign in to Jinear when it asks, then allow the permissions you want to give.</li>
          </ol>

          <h3>In ChatGPT</h3>
          <ol>
            <li>Turn on Developer Mode in Settings.</li>
            <li>Add a connector and paste the same address.</li>
            <li>Sign in and allow, exactly as above.</li>
          </ol>

          <p>
            Self-hosting? Your instance has to be reachable from the internet over HTTPS, because those
            assistants connect from their own servers rather than from your browser. The{" "}
            <a href={MCP_DOCS_URL} target="_blank" rel="noreferrer">
              setup guide
            </a>{" "}
            covers the configuration, and the proxy to use when your instance is on a private network.
          </p>

          <h2>What it can do</h2>
          <p>
            {tools.length} tools, {readOnlyCount} of them read only. Permissions are per resource and
            split between reading and writing, so allowing task writes does not hand over your calendar.
          </p>
          <ul>
            <li>
              <strong>There are no delete tools.</strong> An assistant cannot remove a task, a project, a
              note or a file.
            </li>
            <li>
              <strong>Notes and the calendar are read only.</strong> A note body is a collaborative
              document owned by the editor, and a calendar write would mean writing to Google on your
              behalf.
            </li>
            <li>
              <strong>Every tool works inside one workspace</strong>, checked against your account on
              every call.
            </li>
            <li>
              <strong>You can disconnect at any time</strong>, from your profile page. It takes effect on
              the next call, not when a token expires.
            </li>
          </ul>

          <h2>Every tool</h2>
          <p>
            Grouped by the permission it asks for. This list is generated from the running server, so it
            cannot drift from what the assistant actually receives.
          </p>

          {SCOPE_COPY.map((entry) => {
            const scopeTools = toolsForScope(entry.scope);
            return (
              <section key={entry.scope} className={styles.scope}>
                <h3>
                  {entry.label} <code className={styles.scopeCode}>{entry.scope}</code>
                </h3>
                <p className={styles.scopeText}>{entry.text}</p>
                {scopeTools.length > 0 && (
                  <dl className={styles.toolList}>
                    {scopeTools.map((tool) => (
                      <div key={`${entry.scope}-${tool.name}`} className={styles.tool}>
                        <dt>
                          <code>{tool.name}</code>
                          {tool.annotations.readOnlyHint && (
                            <span className={styles.readOnly}>read only</span>
                          )}
                        </dt>
                        <dd>{tool.description}</dd>
                      </div>
                    ))}
                  </dl>
                )}
              </section>
            );
          })}
        </article>

        <BareFooter />
      </div>
    </div>
  );
}
