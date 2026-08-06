import { formatMinutes, formatPercent } from "@repo/utils";
import Link from "next/link";
import { AppShell } from "../components/AppShell";
import styles from "./workspace.module.css";

const metrics = [
  { label: "Current streak", value: "12 days" },
  { label: "Daily goal", value: formatMinutes(30) },
  { label: "XP earned", value: "2,480" },
  { label: "Course progress", value: formatPercent(64) },
];

const nextLessons = [
  {
    title: "German foundations",
    description: "Greetings, polite phrases, and sentence rhythm.",
    tag: "DE",
    progress: 72,
  },
  {
    title: "Spanish essentials",
    description: "Travel vocabulary with fill-in-the-blank checks.",
    tag: "ES",
    progress: 45,
  },
  {
    title: "French basics",
    description: "Articles, introductions, and listening practice.",
    tag: "FR",
    progress: 28,
  },
];

export default function Home() {
  return (
    <AppShell>
      <section className={styles.hero}>
        <div className={styles.section}>
          <p className={styles.eyebrow}>Learner dashboard</p>
          <h1 className={styles.title}>A focused workspace for daily fluency.</h1>
          <p className={styles.lede}>
            Continue structured lessons, review saved vocabulary, complete quiz
            checkpoints, and keep AI tutor conversations connected to your
            learning plan.
          </p>
          <div className={styles.actions}>
            <Link className={styles.buttonPrimary} href="/practice">
              Start practice
            </Link>
            <Link className={styles.buttonSecondary} href="/courses">
              Browse courses
            </Link>
          </div>
        </div>

        <div className={styles.panel}>
          <p className={styles.eyebrow}>Today</p>
          <h2>Recommended plan</h2>
          <div className={styles.list}>
            <article className={styles.item}>
              <div className={styles.itemHeader}>
                <h3>Review 12 saved words</h3>
                <span className={styles.tag}>Vocabulary</span>
              </div>
              <p>Focus on words marked low mastery in your current language.</p>
            </article>
            <article className={styles.item}>
              <div className={styles.itemHeader}>
                <h3>Complete one quiz</h3>
                <span className={styles.tag}>Assessment</span>
              </div>
              <p>Pass the lesson check to add XP and preserve your streak.</p>
            </article>
          </div>
        </div>
      </section>

      <section className={styles.metrics} aria-label="Learning metrics">
        {metrics.map((metric) => (
          <article className={styles.metric} key={metric.label}>
            <strong>{metric.value}</strong>
            <span>{metric.label}</span>
          </article>
        ))}
      </section>

      <section className={styles.grid} aria-label="Courses in progress">
        {nextLessons.map((lesson) => (
          <article className={styles.item} key={lesson.title}>
            <div className={styles.itemHeader}>
              <h2>{lesson.title}</h2>
              <span className={styles.tag}>{lesson.tag}</span>
            </div>
            <p>{lesson.description}</p>
            <div className={styles.progressTrack}>
              <div
                className={styles.progressFill}
                style={{ width: `${lesson.progress}%` }}
              />
            </div>
          </article>
        ))}
      </section>
    </AppShell>
  );
}
