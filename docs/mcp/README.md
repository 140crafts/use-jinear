# Connecting Claude or ChatGPT to Jinear

Jinear can run an MCP server. An AI assistant that speaks MCP, such as Claude or ChatGPT,
connects to it and then works with your tasks, boards, notes, files and calendar
inside the assistant, using your own account and only the permissions you allow.

This page covers a self-hosted instance. On the hosted version at `jinear.co` the server
is already running and you can skip to [Connect from Claude](#connect-from-claude).

---

## Before you start

**Your instance must be reachable from the internet over HTTPS with a valid certificate.**

Claude and ChatGPT connect from their own servers, not from your browser. An instance on a
private network, a VPN or `localhost` cannot be added as a custom connector at all, and
there is no setting that changes this. If that describes your instance, read
[Instances on a private network](#instances-on-a-private-network) instead.

You also need:

- An administrator account on the instance, to turn the feature on.
- Jinear running from the installer, or the equivalent configuration by hand.

---

## Step 1: Turn on the server

The installer asks about this. If you answered yes, `MCP_ENABLED=true` is already in your
`.env` and you can go to step 2.

To turn it on afterwards:

```bash
cd ~/jinear
nano .env          # set MCP_ENABLED=true
docker compose up -d
```

Check that `OAUTH_JWT_SECRET` in `.env` is not empty. The installer generates it. If you are
configuring by hand, generate one:

```bash
openssl rand -base64 64 | tr -dc 'a-zA-Z0-9' | head -c 64
```

### The three URLs

You do not set these yourself. `docker-compose.yaml` derives them from `DOMAIN` and
`API_DOMAIN`, because a mismatch between them is the failure that produces every support
question. For a domain of `jinear.example.com` with an API domain of
`api.jinear.example.com`, they come out as:

| Variable | Value | Why it must be this |
|----------|-------|---------------------|
| `OAUTH_ISSUER_URL` | `https://api.jinear.example.com` | The origin that serves `/.well-known/*`. The API origin, not the app origin |
| `MCP_RESOURCE_URL` | `https://api.jinear.example.com/mcp` | Exactly what you paste into your client, path included. The specification compares the two character for character |
| `OAUTH_CONSENT_URL` | `https://jinear.example.com/oauth/consent?request_id={requestId}` | The consent screen, which is on the app where you sign in. Keep the `{requestId}` placeholder |
| `OAUTH_JWT_SECRET` | generated | Signs the access tokens the OAuth server issues. Deliberately not `JWT_SECRET` |

The `OAUTH_` half configures the authorization server that issues tokens; the `MCP_` half
configures the resource those tokens open. There is one enabled flag, `MCP_ENABLED`,
because MCP is the only resource today.

If you run your own reverse proxy, two paths must reach `jinear-core` on the API domain:

- `/mcp`
- `/.well-known/*`

The bundled Caddy configuration proxies the whole API host, so this is already true. Only
a path-based proxy of your own can get this wrong.

---

## Step 2: Turn on the instance flag

`MCP_ENABLED` decides whether the server exists. A second switch decides whether members
may use it, and an administrator keeps that one.

1. Sign in as the instance admin and open `https://your-domain.com/admin`.
2. Go to **General**, then instance flags.
3. Turn on **AI Assistant Connections**.

Turning this off later stops new connections. Assistants that are already connected keep
working until a member disconnects them.

---

## Step 3: Find your server address

Every member finds it on their own profile page, under **AI Assistants**, with a copy
button. It is:

```
https://api.your-domain.com/mcp
```

The same page lists the assistants that member has connected and lets them disconnect one.

---

## Connect from Claude

1. Open **Settings**, then **Connectors**.
2. Choose **Add custom connector**.
3. Paste your server address.
4. Claude sends you to Jinear. Sign in if you are not signed in already.
5. Read what the assistant is asking for and choose **Allow**.

You go back to Claude, and the Jinear tools appear.

## Connect from ChatGPT

1. Turn on **Developer Mode** in **Settings**.
2. Add a connector and paste the same server address.
3. Sign in and allow, exactly as above.

---

## Instances on a private network

If your instance has no public address, use Claude Desktop with the `mcp-remote` proxy. It
runs on your own machine, so it reaches the instance the same way your browser does.

Add this to your Claude Desktop configuration file:

```json
{
  "mcpServers": {
    "jinear": {
      "command": "npx",
      "args": ["-y", "mcp-remote", "https://jinear.internal/mcp"]
    }
  }
}
```

Replace the URL with your own `MCP_RESOURCE_URL`. Restart Claude Desktop, and the sign in
and consent screens open in your browser.

This path needs Node.js on the machine running Claude Desktop. It does not work for
ChatGPT, which has no equivalent local proxy.

---

## What an assistant can do

Permissions are per resource and split between reading and writing. You choose them on the
consent screen, and the assistant gets nothing you did not allow.

| Permission | What it covers |
|------------|----------------|
| `workspace:read` | Workspaces, teams, members, topics, workflow statuses |
| `tasks:read` | Tasks, boards, comments |
| `tasks:write` | Creating and updating tasks, boards and comments |
| `calendar:read` | Calendar events |
| `notes:read` | Notebooks and notes |
| `files:read` | Files, folders and links to them |
| `offline_access` | Staying connected until you disconnect |

Some deliberate limits:

- **There are no delete tools.** An assistant cannot remove a task, a note or a file.
- **Notes and the calendar are read only.** A note body is a collaborative document owned
  by the editor, and a calendar write would mean writing to Google on your behalf.
- **Every tool works inside one workspace**, which the assistant passes in and which is
  checked against your account on every call.
- **Projects are not covered.** Projects are a deprecated feature, so no tool reads or
  writes them.

---

## Turning it off

| To stop | Do this |
|---------|---------|
| New connections, keeping existing ones | Turn off the instance flag in the admin panel |
| One assistant, for one member | That member disconnects it on their profile page |
| One client application, for everybody | Revoke it under **Admin > AI Assistants** |
| Everything | Set `MCP_ENABLED=false` in `.env` and run `docker compose up -d` |

---

## Troubleshooting

**The client says it cannot reach the server.**
Check that `https://api.your-domain.com/.well-known/oauth-protected-resource/mcp` answers
in a browser. If it does not, the API host is not reachable or your proxy is blocking
`/.well-known`.

**The client connects but no tools appear.**
The address you pasted must match `MCP_RESOURCE_URL` exactly, including the `/mcp` path
and with no trailing slash.

**Signing in works but the page says the request is not available.**
The connection request expired, which happens after ten minutes. Start the connection
again from your client.

**The consent page does not load.**
`OAUTH_CONSENT_URL` must point at the app domain, not the API domain, and must keep the
`{requestId}` placeholder.

**Everything refuses with "The MCP server is turned off on this instance."**
One of the two switches is off. Check `MCP_ENABLED` in `.env` and the **AI Assistant
Connections** flag in the admin panel.

**Check the logs:**

```bash
cd ~/jinear
docker compose logs -f jinear-core | grep MCP
```
