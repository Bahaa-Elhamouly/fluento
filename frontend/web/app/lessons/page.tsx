import { AppShell } from "../../components/AppShell";
import styles from "../workspace.module.css";

const lessons = [
  { title: "Greetings and introductions", type: "Reading", minutes: 8 },
  { title: "Travel vocabulary", type: "Vocabulary", minutes: 12 },
  { title: "Articles and gender", type: "Grammar", minutes: 10 },
  { title: "Listening checkpoint", type: "Listening", minutes: 9 },
];

export default function LessonsPage() {
  return (
    <AppShell>
      <section className={styles.section}>
        <p className={styles.eyebrow}>Lesson queue</p>
        <h1 className={styles.title}>Continue with short, measurable lessons.</h1>
      </section>
      <section className={styles.list}>
        {lessons.map((lesson) => (
          <article className={styles.item} key={lesson.title}>
            <div className={styles.itemHeader}>
              <h2>{lesson.title}</h2>
              <span className={styles.tag}>{lesson.type}</span>
            </div>
            <p>{lesson.minutes} minutes estimated</p>
          </article>
        ))}
      </section>
    </AppShell>
  );
}
