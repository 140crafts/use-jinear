# Updates and anonymous statistics

A self-hosted Jinear instance can send one small report a day to `https://api.jinear.co`.
There are two tiers, and each one is a separate choice. Both are **off** unless you turn
them on. This page lists every field that can leave your server.

## The two tiers

| Tier | What it sends | Installer default | Setting |
|---|---|---|---|
| Update check | A random instance id and the version number | Yes | `TELEMETRY_UPDATE_CHECK` |
| Usage report | The update check, plus which features are on and rough size ranges | Yes | `TELEMETRY_USAGE_REPORT` |

The usage report is how I learn which features people use most. Self-hosted instances are
invisible to me, so without it I cannot tell which parts of Jinear to improve. You can
turn it off at any time, and turning it off clears what I stored.

In return, the instance gets the latest release number. The admin panel
(Admin > Instance settings > General) then shows your version, and it tells you when a
new version is available.

## What is sent

One `POST https://api.jinear.co/v1/instance-report`, 10 minutes after jinear-core starts
and then every 24 hours. This is the full body. There are no other fields and no extra
headers with data about you.

```json
{
  "instanceId": "0d7c7a4e-3b1f-4c2a-9e55-6f1b2a3c4d5e",
  "version": "v0.1.1681",
  "usage": {
    "storageProvider": "MINIO",
    "enabledInstanceFlags": ["FORGOT_PASSWORD", "MCP_SERVER", "REGISTER_WITH_MAIL", "WORKSPACE_INIT"],
    "mcpEnabled": true,
    "oauthEnabled": true,
    "pushNotificationsEnabled": false,
    "mailConfigured": true,
    "managementEnabled": true,
    "accounts": "SIX_TO_TWENTY_FIVE",
    "workspaces": "ONE",
    "teams": "TWO_TO_FIVE",
    "tasksCreatedLast30Days": "TWENTY_SIX_TO_HUNDRED",
    "javaVersion": "21",
    "postgresVersion": "15",
    "osArch": "amd64"
  }
}
```

When only the update check is on, `usage` is `null`.

| Field | Meaning |
|---|---|
| `instanceId` | A random UUID. Your instance creates it once and keeps it in the `instance_info` table. It is not derived from anything about your server. |
| `version` | The jinear-core release tag. |
| `storageProvider` | `MINIO` or `GCLOUD`. |
| `enabledInstanceFlags` | The instance flags that are on in the admin panel (sign-in methods, workspace creation, Google Calendar, MCP). |
| `mcpEnabled`, `oauthEnabled` | Whether the MCP server and its OAuth server run. |
| `pushNotificationsEnabled` | Whether Firebase push is configured. |
| `mailConfigured` | Whether an SMTP host other than `localhost` is set. The host itself is not sent. |
| `managementEnabled` | Whether the admin panel is on. |
| `accounts`, `workspaces`, `teams` | A size range, never an exact count: `ZERO`, `ONE`, `TWO_TO_FIVE`, `SIX_TO_TWENTY_FIVE`, `TWENTY_SIX_TO_HUNDRED`, `HUNDRED_ONE_TO_FIVE_HUNDRED`, `OVER_FIVE_HUNDRED`. |
| `tasksCreatedLast30Days` | The same kind of range, for tasks created in the last 30 days. It shows whether an instance is in use. |
| `javaVersion`, `postgresVersion`, `osArch` | Major Java version, major Postgres version and CPU architecture. They help me plan upgrades. |

## What is never sent

- IP addresses, hostnames or domains
- Names, emails or any other account data
- Workspace, team, task, note or file content, or their titles
- Exact counts
- Your SMTP, storage or OAuth settings

## What I keep

The receiver keeps one row per `instanceId`: the last version, the last report date and the
last usage fields. The table has no IP column, and the receiver does not read the request
IP. If you turn the usage report off, the next report clears the stored usage fields.

<!-- TODO(cagdas): confirm and state the access log retention of the web server in front of api.jinear.co -->

## Turning it on or off

**New installs:** the installer asks both questions. Your answers go to `.env`.

**Existing installs** send nothing after an update, until you opt in. Add these lines:

1. In `.env`:
   ```
   TELEMETRY_UPDATE_CHECK=true
   TELEMETRY_USAGE_REPORT=false
   DO_NOT_TRACK=
   ```
2. In `docker-compose.yaml`, under `jinear-core` > `environment`:
   ```yaml
   TELEMETRY_UPDATE_CHECK: ${TELEMETRY_UPDATE_CHECK:-false}
   TELEMETRY_USAGE_REPORT: ${TELEMETRY_USAGE_REPORT:-false}
   DO_NOT_TRACK: ${DO_NOT_TRACK:-}
   ```
3. In `.config/application.properties`:
   ```properties
   jinear.telemetry.update-check.enabled=${TELEMETRY_UPDATE_CHECK:false}
   jinear.telemetry.usage-report.enabled=${TELEMETRY_USAGE_REPORT:false}
   ```

Then run `docker compose up -d`.

To turn everything off, set both values to `false`, or set `DO_NOT_TRACK=1`.
`DO_NOT_TRACK=1` wins over both settings.

Each run writes one log line in jinear-core: `Send instance report has started.
updateCheckEnabled: ..., usageReportEnabled: ..., doNotTrack: ...`. Check it to see what
your instance does.

## Limits

- The version is the jinear-core version. A release that changes only the web app does
  not show an update notice.
- Development builds and branch builds never send a report. Only release versions
  (`v0.1.NNNN`) do.
