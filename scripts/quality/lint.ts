import { spawnSync } from "node:child_process";
import { platform } from "node:os";
import { resolve } from "node:path";

const result = spawnSync("npm", ["run", "lint"], {
  cwd: resolve(import.meta.dirname, "../.."),
  stdio: "inherit",
  shell: platform() === "win32",
});

process.exit(result.status ?? 1);
