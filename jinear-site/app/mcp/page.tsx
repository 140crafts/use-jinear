import { Fragment } from "react";
import { Metadata } from "next";
import BareNav from "@/components/homepage/bareNav/BareNav";
import BareFooter from "@/components/homepage/bareFooter/BareFooter";
import { buildMetadata } from "@/utils/seo";
import { MCP_DOCS_URL } from "@/utils/constants";
import VideoEmbed from "@/components/videoEmbed/VideoEmbed";
import {
  MCP_CLIENTS,
  MCP_LEDE,
  MCP_MANIFEST_PATH,
  MCP_NOTES,
  MCP_PAGE_TITLE,
  MCP_SCOPES,
  MCP_SERVER_URL,
  mcpToolSummary,
  mcpTools,
  mcpToolsForScope,
} from "@/lib/mcp";
import styles from "./index.module.scss";

export const metadata: Metadata = buildMetadata({
  title: "Connect Claude or ChatGPT",
  description:
    "Connect Claude or ChatGPT to Jinear over MCP and work with your tasks, notes and files from inside the assistant. Works on the hosted version and on your own server.",
  path: "/mcp/",
  ogTitle: "Jinear for Claude and ChatGPT",
  ogDescription:
    `Connect an AI assistant to Jinear over MCP. ${mcpTools.length} tools, per-resource permissions, and no way to delete a task, a board or a file.`,
});

export default function McpPage() {
  return (
    <div className={styles.page}>
      <div className={styles.wrap}>
        <BareNav />

        <header className={styles.ahead}>
          <h1 className={styles.title}>{MCP_PAGE_TITLE}</h1>
          <p className={styles.lede}>{MCP_LEDE}</p>
        </header>

        <article className={styles.body}>
          <h2>Connect it</h2>
          <p>
            You need a server address. On the hosted version it is{" "}
            <code>{MCP_SERVER_URL}</code>. On your own server it is your API domain with{" "}
            <code>/mcp</code> on the end, and every member finds it in their profile with a copy
            button.
          </p>

          {MCP_CLIENTS.map((client) => (
            <Fragment key={client.name}>
              <h3>In {client.name}</h3>
              <ol>
                {client.steps.map((step) => (
                  <li key={step}>{step}</li>
                ))}
              </ol>
              <VideoEmbed id={client.videoId} title={client.videoTitle} />
            </Fragment>
          ))}

          <p>
            Self-hosting? Your instance has to be reachable from the internet over HTTPS, because those
            assistants connect from their own servers rather than from your browser. The{" "}
            <a href={MCP_DOCS_URL} target="_blank" rel="noreferrer">
              setup guide
            </a>{" "}
            covers the configuration, and the proxy to use when your instance is on a private network.
          </p>

          <h2>What it can do</h2>
          <p>{mcpToolSummary()}</p>
          <ul>
            {MCP_NOTES.map((note) => (
              <li key={note.lead}>
                <strong>{note.lead}</strong>
                {note.rest}
              </li>
            ))}
          </ul>

          <h2>Every tool</h2>
          <p>
            Grouped by the permission it asks for. This list shows the hosted version. A self-hosted
            server can run a different version; it lists its exact tools at your API domain with{" "}
            <code>{MCP_MANIFEST_PATH}</code> on the end.
          </p>

          {MCP_SCOPES.map((entry) => {
            const scopeTools = mcpToolsForScope(entry.scope);
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
