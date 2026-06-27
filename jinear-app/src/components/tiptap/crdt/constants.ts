/**
 * Tunable cadence constants for the CRDT polling transport. Sane defaults — the backend is an opaque
 * ordered relay, so these only trade off latency vs. request volume, never correctness.
 */

/** Quiet period after the last local change before a push is flushed. */
export const PUSH_DEBOUNCE_MS = 400;

/** Ceiling so continuous typing still flushes roughly this often instead of starving the debounce. */
export const PUSH_MAX_INTERVAL_MS = 2000;

/** How often we poll the relay for remote updates while editing and the tab is visible. */
export const POLL_INTERVAL_MS = 2500;

/**
 * Compact (snapshot) once the update log grows this far past the last snapshot. Snapshotting folds the
 * log into a single full-state blob and lets the server prune superseded updates. Never debounced.
 */
export const SNAPSHOT_UPDATE_THRESHOLD = 75;

/** Origin tag used when applying remote updates, so the local update handler can skip echoing them. */
export const REMOTE_ORIGIN = "remote-crdt-sync";

/** Yjs fragment name shared by the transport, the seed helper and the Collaboration extension. */
export const CRDT_FIELD = "default";
