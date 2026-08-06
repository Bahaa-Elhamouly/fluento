import { expect, test } from "@playwright/test";

test.describe("Fluento web shell", () => {
  test("renders dashboard navigation", async ({ page }) => {
    await page.goto("/");

    await expect(page.getByRole("link", { name: "Fluento" })).toBeVisible();
    await expect(page.getByRole("link", { name: "Courses" })).toBeVisible();
    await expect(page.getByRole("link", { name: "Practice" })).toBeVisible();
    await expect(
      page.getByRole("heading", {
        name: "A focused workspace for daily fluency.",
      }),
    ).toBeVisible();
  });

  test("opens primary MVP pages", async ({ page }) => {
    const pages = [
      { path: "/courses", heading: "German, Spanish, and French foundations." },
      { path: "/lessons", heading: "Continue with short, measurable lessons." },
      {
        path: "/practice",
        heading: "One place for vocabulary, grammar, quizzes, and tutor work.",
      },
      {
        path: "/progress",
        heading: "Track XP, streaks, achievements, and recent lessons.",
      },
      { path: "/admin", heading: "Operational controls for the Fluento MVP." },
    ];

    for (const item of pages) {
      await page.goto(item.path);
      await expect(page.getByRole("heading", { name: item.heading })).toBeVisible();
    }
  });
});
