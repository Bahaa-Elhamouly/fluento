import Link from "next/link";
import styles from "./page.module.css";

const learningStats = [
  { label: "Daily streak", value: "12" },
  { label: "Words mastered", value: "248" },
  { label: "Lessons queued", value: "6" },
];

const practiceItems = [
  "Shadow a 3-minute dialogue",
  "Review travel vocabulary",
  "Complete grammar checkpoint",
];

export default function Home() {
  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <Link className={styles.brand} href="/">
          Fluento
        </Link>
        <nav className={styles.nav} aria-label="Primary navigation">
          <Link href="/lessons">Lessons</Link>
          <Link href="/practice">Practice</Link>
          <Link href="/progress">Progress</Link>
        </nav>
      </header>

      <main className={styles.main}>
        <section className={styles.hero}>
          <div className={styles.heroCopy}>
            <p className={styles.eyebrow}>Personal language coach</p>
            <h1>Build fluency with focused daily practice.</h1>
            <p className={styles.lede}>
              Fluento turns lessons, vocabulary, and speaking drills into a
              clear practice plan that keeps learners moving every day.
            </p>
            <div className={styles.actions}>
              <Link className={styles.primaryAction} href="/practice">
                Start practice
              </Link>
              <Link className={styles.secondaryAction} href="/lessons">
                Browse lessons
              </Link>
            </div>
          </div>

          <div className={styles.practicePanel} aria-label="Today's practice">
            <div className={styles.panelHeader}>
              <span>Today</span>
              <strong>42 min</strong>
            </div>
            <ul>
              {practiceItems.map((item) => (
                <li key={item}>
                  <span aria-hidden="true" />
                  {item}
                </li>
              ))}
            </ul>
          </div>
        </section>

        <section className={styles.stats} aria-label="Learning progress">
          {learningStats.map((stat) => (
            <article key={stat.label}>
              <strong>{stat.value}</strong>
              <span>{stat.label}</span>
            </article>
          ))}
        </section>
      </main>
    </div>
  );
}
