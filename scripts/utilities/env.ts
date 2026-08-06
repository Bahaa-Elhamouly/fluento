const required = [
  "DB_URL",
  "DB_USERNAME",
  "DB_PASSWORD",
  "JWT_SECRET",
  "NEXT_PUBLIC_API_BASE_URL",
];

const missing = required.filter((key) => !process.env[key]);

if (missing.length > 0) {
  console.log(`Missing optional local environment values: ${missing.join(", ")}`);
  process.exit(0);
}

console.log("All Fluento environment values are present.");
