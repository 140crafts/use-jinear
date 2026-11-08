globalThis.console = {log() {}, info() {}, warn() {}, error() {}, debug() {}};
globalThis.crypto = {
    getRandomValues(target) {
        const bytes = globalThis.secureRandomBytes(target.byteLength);
        new Uint8Array(target.buffer, target.byteOffset, target.byteLength).set(bytes);
        return target;
    }
};
const BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
globalThis.atob = (value) => {
    const clean = value.replace(/=+$/, "");
    let binary = "";
    let buffer = 0;
    let bits = 0;
    for (let i = 0; i < clean.length; i++) {
        buffer = ((buffer << 6) | BASE64.indexOf(clean[i])) & 0xffff;
        bits += 6;
        if (bits >= 8) {
            bits -= 8;
            binary += String.fromCharCode((buffer >> bits) & 255);
        }
    }
    return binary;
};
globalThis.btoa = (binary) => {
    let encoded = "";
    for (let i = 0; i < binary.length; i += 3) {
        const a = binary.charCodeAt(i);
        const b = binary.charCodeAt(i + 1);
        const c = binary.charCodeAt(i + 2);
        encoded += BASE64[a >> 2] + BASE64[((a & 3) << 4) | ((b || 0) >> 4)];
        encoded += Number.isNaN(b) ? "=" : BASE64[((b & 15) << 2) | ((c || 0) >> 6)];
        encoded += Number.isNaN(c) ? "=" : BASE64[c & 63];
    }
    return encoded;
};
