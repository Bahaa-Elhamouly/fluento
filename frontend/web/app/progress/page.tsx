import { formatPercent } from "@repo/utils";
import { AppShell } from "../../components/AppShell";
import styles from "../workspace.module.css";

const metrics = [
  { label: "Total XP", value: "2,480" },
  { label: "Completed lessons", value: "31" },
  { label: "Longest streak", value: "18 days" },
  { label: "Quiz pass rate", value: formatPercent(82) },
];

export default function ProgressPage() {
  return (
    <AppShell>
      <section className={styles.section}>
        <p className={styles.eyebrow}>Progress</p>
        <h1 className={styles.title}>Track XP, streaks, achievements, and recent lessons.</h1>
      </section>
      <section className={styles.metrics}>
        {metrics.map((metric) => (
          <article className={styles.metric} key={metric.label}>
            <strong>{metric.value}</strong>
            <span>{metric.label}</span>
          </article>
        ))}
      </section>
    </AppShell>
  );
}
