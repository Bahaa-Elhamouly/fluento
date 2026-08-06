import { AppShell } from "../../components/AppShell";
import styles from "../workspace.module.css";

const modes = [
  { title: "Flashcards", description: "Review favorites and low-mastery terms." },
  { title: "Grammar drill", description: "Practice rules with instant correction." },
  { title: "Quiz checkpoint", description: "Submit answers and earn progress XP." },
  { title: "AI tutor", description: "Continue a guided multilingual conversation." },
];

export default function PracticePage() {
  return (
    <AppShell>
      <section className={styles.section}>
        <p className={styles.eyebrow}>Practice</p>
        <h1 className={styles.title}>One place for vocabulary, grammar, quizzes, and tutor work.</h1>
      </section>
      <section className={styles.grid}>
        {modes.map((mode) => (
          <article className={styles.item} key={mode.title}>
            <h2>{mode.title}</h2>
            <p>{mode.description}</p>
          </article>
        ))}
      </section>
    </AppShell>
  );
}
