import {fileURLToPath} from "node:url";
import {defineConfig} from "rolldown";

const resolvePath = (relative: string) => fileURLToPath(new URL(relative, import.meta.url));

export default defineConfig({
    input: resolvePath("./entry.ts"),
    platform: "browser",
    resolve: {alias: {"@acemir/cssom": resolvePath("./cssomStub.ts")}},
    output: {
        file: resolvePath("../../../jinear-core/src/main/resources/note-crdt/note-crdt.bundle.js"),
        format: "iife",
        minify: true
    }
});
