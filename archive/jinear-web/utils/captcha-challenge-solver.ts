import { CaptchaChallengeDto } from "@/be/jinear-core";

export const solveCaptchaChallenge = async (data: CaptchaChallengeDto, onProgress?: (currentNonce: number) => void) => {
  const results = [];
  for (const prefix of data.prefixes) {
    results.push(await solveChallenge(prefix, data.difficulty, onProgress));
  }
  return results;
};

async function solveChallenge(prefix: string, difficulty: number, onProgress?: (currentNonce: number) => void) {
  let nonce = 0;
  const target = "0".repeat(difficulty);
  while (true) {
    for (let i = 0; i < 500; i++) {
      const input = prefix + nonce;
      const hash = await sha256(input);
      if (hash.startsWith(target)) {
        return { prefix, nonce, hash };
      }
      nonce++;
    }
    if (onProgress) onProgress(nonce);
    await new Promise(r => setTimeout(r, 0));
  }
}

async function sha256(message: string) {
  const msgBuffer = new TextEncoder().encode(message);
  const hashBuffer = await crypto.subtle.digest("SHA-256", msgBuffer);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  const hashHex = hashArray.map(b => b.toString(16).padStart(2, "0")).join("");
  return hashHex;
}
