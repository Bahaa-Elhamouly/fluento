import { spawnSync } from "node:child_process";
import { platform } from "node:os";
import { resolve } from "node:path";

const mvnw = platform() === "win32" ? "mvnw.cmd" : "./mvnw";
const result = spawnSync(mvnw, ["flyway:migrate"], {
  cwd: resolve(import.meta.dirname, "../../backend/fluento-api"),
  stdio: "inherit",
  shell: platform() === "win32",
});

process.exit(result.status ?? 1);
