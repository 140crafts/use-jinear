import manifest from "./mcp-tools.generated.json";
import { MCP_DOCS_URL, SITE_URL } from "@/utils/constants";

export interface McpTool {
  name: string;
  title: string;
  description: string;
  annotations: { readOnlyHint: boolean };
}

export interface McpScope {
  scope: string;
  label: string;
  text: string;
}

export interface McpClient {
  name: string;
  steps: string[];
  videoId: string;
  videoTitle: string;
}

export interface McpNote {
  lead: string;
  rest: string;
}

export const MCP_PAGE_TITLE = "Connect Claude or ChatGPT to Jinear";
export const MCP_SERVER_URL = "https://api.jinear.co/mcp";
export const MCP_MANIFEST_PATH = "/v1/mcp/manifest";

export const MCP_LEDE =
  "Jinear speaks MCP, so an AI assistant can read and change your work directly. Ask Claude what is assigned to you this week, have it write up a task from a conversation, or let ChatGPT cite a note while it drafts. It uses your account, and it can only do what you allow.";

export const MCP_CLIENTS: McpClient[] = [
  {
    name: "Claude",
    steps: [
      "Open Settings, then Connectors.",
      "Choose Add custom connector and paste the address.",
      "Sign in to Jinear when it asks, then allow the permissions you want to give.",
    ],
    videoId: "0VYQehxs-ps",
    videoTitle: "Connecting Claude to Jinear",
  },
  {
    name: "ChatGPT",
    steps: [
      "Turn on Developer Mode in Settings.",
      "Add a connector and paste the same address.",
      "Sign in and allow, exactly as above.",
    ],
    videoId: "EIe6UpnrBWQ",
    videoTitle: "Connecting ChatGPT to Jinear",
  },
];

export const MCP_NOTES: McpNote[] = [
  {
    lead: "Deleting is limited.",
    rest: " An assistant can delete a note, a checklist, a checklist item or a reminder. It cannot delete a task, a board or a file.",
  },
  {
    lead: "Note edits merge.",
    rest: " An assistant edits a note the same way a second person would, so its changes and the changes of anyone who has the note open come together.",
  },
  {
    lead: "The calendar and file storage are read only.",
    rest: " A calendar write would mean writing to Google on your behalf. An assistant can attach a file to a task, but it cannot upload into your files or change them.",
  },
  {
    lead: "Every tool works inside one workspace",
    rest: ", checked against your account on every call.",
  },
  {
    lead: "You can disconnect at any time",
    rest: ", from your profile. It takes effect on the next call, not when a token expires.",
  },
];

export const MCP_SCOPES: McpScope[] = [
  { scope: "workspace:read", label: "Workspaces", text: "Workspaces, teams, members, topics and workflow statuses." },
  { scope: "tasks:read", label: "Read tasks", text: "Tasks, boards and comments." },
  { scope: "tasks:write", label: "Write tasks", text: "Creating and updating tasks, boards, comments, checklists, reminders and topics, and removing checklists and reminders." },
  { scope: "calendar:read", label: "Read calendar", text: "Calendar events, including any synced from Google Calendar." },
  { scope: "notes:read", label: "Read notes", text: "Notebooks and notes." },
  { scope: "notes:write", label: "Write notes", text: "Creating, editing, moving and deleting notes." },
  { scope: "files:read", label: "Read files", text: "Files, folders and links to them." },
  { scope: "offline_access", label: "Stay connected", text: "Keeps the connection alive until you disconnect it." },
];

export const mcpTools = manifest.tools as McpTool[];

const toolScopes = manifest.scopes as Record<string, string[]>;

export const mcpReadOnlyToolCount = mcpTools.filter((tool) => tool.annotations.readOnlyHint).length;

export const mcpToolsForScope = (scope: string) =>
  mcpTools.filter((tool) => (toolScopes[tool.name] ?? []).includes(scope));

export const mcpToolSummary = () =>
  `${mcpTools.length} tools, ${mcpReadOnlyToolCount} of them read only. Permissions are per resource and split between reading and writing, so allowing task writes does not hand over your calendar.`;

const oneLine = (value: string) => value.replace(/\s+/g, " ").trim();

const numbered = (steps: string[]) => steps.map((step, index) => `${index + 1}. ${step}`).join("\n");

const noteLines = () => MCP_NOTES.map((note) => `- **${note.lead}**${note.rest}`).join("\n");

const serverAddressText = () =>
  `You need a server address. On the hosted version it is \`${MCP_SERVER_URL}\`. On your own server it is your API domain with \`/mcp\` on the end, and every member finds it in their profile with a copy button.`;

const selfHostingText = () =>
  `Self-hosting? Your instance has to be reachable from the internet over HTTPS, because those assistants connect from their own servers rather than from your browser. The [setup guide](${MCP_DOCS_URL}) covers the configuration, and the proxy to use when your instance is on a private network.`;

const clientSteps = (headingLevel: string, withVideo: boolean) =>
  MCP_CLIENTS.map((client) => {
    const video = withVideo ? `\n\nVideo: [${client.videoTitle}](https://youtu.be/${client.videoId})` : "";
    return `${headingLevel} In ${client.name}\n\n${numbered(client.steps)}${video}`;
  }).join("\n\n");

const toolLine = (tool: McpTool) =>
  `- \`${tool.name}\`${tool.annotations.readOnlyHint ? " (read only)" : ""}: ${oneLine(tool.description)}`;

const scopeSection = (entry: McpScope) => {
  const lines = mcpToolsForScope(entry.scope).map(toolLine).join("\n");
  const head = `### ${entry.label} (\`${entry.scope}\`)\n\n${entry.text}`;
  return lines ? `${head}\n\n${lines}` : head;
};

export function mcpSummaryMarkdown(): string {
  return `${MCP_LEDE}

${serverAddressText()}

${clientSteps("###", false)}

${selfHostingText()}

${mcpToolSummary()}

${noteLines()}

The full tool list, grouped by permission, is on the [MCP page](${SITE_URL}/mcp/) and in [llms-full.txt](${SITE_URL}/llms-full.txt).`;
}

export function mcpFullMarkdown(): string {
  return `# ${MCP_PAGE_TITLE}
URL: ${SITE_URL}/mcp/

${MCP_LEDE}

## Connect it

${serverAddressText()}

${clientSteps("###", true)}

${selfHostingText()}

## What it can do

${mcpToolSummary()}

${noteLines()}

## Every tool

Grouped by the permission it asks for. This list shows the hosted version. A self-hosted server can run a different version; it lists its exact tools at your API domain with \`${MCP_MANIFEST_PATH}\` on the end.

${MCP_SCOPES.map(scopeSection).join("\n\n")}`;
}
