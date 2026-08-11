#!/usr/bin/env node
/**
 * Submit the live sitemap's URLs to IndexNow (Bing, Yandex, Seznam, Naver).
 *
 * Run this AFTER the new build is actually serving on the site. IndexNow tells
 * search engines "recrawl these now", so pinging before the server has pulled the
 * new image just makes them recrawl the old content.
 *
 *   node scripts/indexnow.mjs            # verify key, read live sitemap, submit
 *   node scripts/indexnow.mjs --dry-run  # print what would be submitted
 *
 * Env overrides (for staging):
 *   SITE_URL       default https://jinear.co
 *   INDEXNOW_KEY   default the committed key below
 *
 * The key is public by design: IndexNow proves ownership by having you host it at
 * https://<host>/<key>.txt, so there is nothing secret to leak here.
 */

const SITE_URL = (process.env.SITE_URL || "https://jinear.co").replace(/\/$/, "");
const KEY = process.env.INDEXNOW_KEY || "a8ee1f955b4c4073b45836d8b598e619";
const ENDPOINT = "https://api.indexnow.org/IndexNow";

const host = new URL(SITE_URL).host;
const keyLocation = `${SITE_URL}/${KEY}.txt`;
const dryRun = process.argv.includes("--dry-run");

function fail(message) {
  console.error(`✗ ${message}`);
  process.exit(1);
}

/** IndexNow rejects the whole batch if the key file isn't reachable, so check first. */
async function verifyKeyFile() {
  let res;
  try {
    res = await fetch(keyLocation);
  } catch (err) {
    fail(`could not reach ${keyLocation}: ${err.message}`);
  }
  if (!res.ok) {
    fail(
      `${keyLocation} returned HTTP ${res.status}. The key file must be deployed ` +
        `(it lives at public/${KEY}.txt) before IndexNow will accept submissions.`
    );
  }
  const body = (await res.text()).trim();
  if (body !== KEY) {
    fail(`${keyLocation} does not contain the key (got ${JSON.stringify(body.slice(0, 80))}).`);
  }
  console.log(`✓ key file verified at ${keyLocation}`);
}

/**
 * Read the LIVE sitemap rather than a local out/ build: it guarantees we only
 * submit URLs that are actually deployed, and lets this run on a CI runner that
 * never built the site.
 */
async function readSitemapUrls() {
  const sitemapUrl = `${SITE_URL}/sitemap.xml`;
  let res;
  try {
    res = await fetch(sitemapUrl);
  } catch (err) {
    fail(`could not reach ${sitemapUrl}: ${err.message}`);
  }
  if (!res.ok) fail(`${sitemapUrl} returned HTTP ${res.status}`);

  const xml = await res.text();
  const urls = [...xml.matchAll(/<loc>([^<]+)<\/loc>/g)].map((m) => m[1].trim());
  if (urls.length === 0) fail(`no <loc> entries found in ${sitemapUrl}`);

  // IndexNow rejects a batch containing any URL outside the declared host.
  const foreign = urls.filter((u) => new URL(u).host !== host);
  if (foreign.length > 0) fail(`sitemap contains URLs outside ${host}: ${foreign.join(", ")}`);

  return urls;
}

async function main() {
  await verifyKeyFile();
  const urlList = await readSitemapUrls();
  const payload = { host, key: KEY, keyLocation, urlList };

  console.log(`\n${urlList.length} URL(s) from ${SITE_URL}/sitemap.xml:`);
  for (const url of urlList) console.log(`  ${url}`);

  if (dryRun) {
    console.log("\n--dry-run: nothing submitted. Payload:");
    console.log(JSON.stringify(payload, null, 2));
    return;
  }

  const res = await fetch(ENDPOINT, {
    method: "POST",
    headers: { "Content-Type": "application/json; charset=utf-8" },
    body: JSON.stringify(payload),
  });
  const body = await res.text();

  // 200 = accepted, 202 = accepted but key still being validated. Both are fine.
  if (res.status !== 200 && res.status !== 202) {
    fail(`IndexNow returned HTTP ${res.status}${body ? `: ${body}` : ""}`);
  }
  console.log(`\n✓ submitted ${urlList.length} URL(s), IndexNow returned HTTP ${res.status}`);
}

main().catch((err) => fail(err.stack || err.message));
