import { readFileSync } from "node:fs";
import { resolve } from "node:path";

const root = resolve(import.meta.dirname, "../..");
const rootPackage = JSON.parse(
  readFileSync(resolve(root, "package.json"), "utf8"),
) as { name: string; packageManager?: string };

console.log(`${rootPackage.name} using ${rootPackage.packageManager ?? "npm"}`);
