import { spawnSync } from "node:child_process";
import { platform } from "node:os";
import { resolve } from "node:path";

const root = resolve(import.meta.dirname, "../..");
const mvnw = platform() === "win32" ? "mvnw.cmd" : "./mvnw";
const backend = spawnSync(mvnw, ["test"], {
  cwd: resolve(root, "backend/fluento-api"),
  stdio: "inherit",
  shell: platform() === "win32",
});

if (backend.status !== 0) {
  process.exit(backend.status ?? 1);
}

const frontend = spawnSync("npm", ["run", "check-types"], {
  cwd: root,
  stdio: "inherit",
  shell: platform() === "win32",
});

process.exit(frontend.status ?? 1);
