import { rmSync } from "node:fs";
import { resolve } from "node:path";

const root = resolve(import.meta.dirname, "../..");
const targets = [
  ".turbo",
  "frontend/web/.next",
  "frontend/web/.turbo",
  "backend/fluento-api/target",
  "packages/ui/.turbo",
];

for (const target of targets) {
  const path = resolve(root, target);
  if (!path.startsWith(root)) {
    throw new Error(`Refusing to clean outside repository: ${path}`);
  }

  rmSync(path, { force: true, recursive: true });
  console.log(`Removed ${target}`);
}
