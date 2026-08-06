import { SUPPORTED_LANGUAGES } from "@repo/constants";
import { AppShell } from "../../components/AppShell";
import styles from "../workspace.module.css";

const courseDescriptions: Record<string, string> = {
  de: "A structured path through pronunciation, greetings, core grammar, and everyday reading.",
  es: "Build practical vocabulary, sentence patterns, and listening confidence for common situations.",
  fr: "Learn essentials for introductions, articles, simple questions, and daily conversation.",
};

export default function CoursesPage() {
  return (
    <AppShell>
      <section className={styles.section}>
        <p className={styles.eyebrow}>Course catalog</p>
        <h1 className={styles.title}>German, Spanish, and French foundations.</h1>
        <p className={styles.lede}>
          These cards represent the initial production languages backed by the
          Spring catalog APIs and Flyway seed data.
        </p>
      </section>
      <section className={styles.grid}>
        {SUPPORTED_LANGUAGES.map((language) => (
          <article className={styles.item} key={language.code}>
            <div className={styles.itemHeader}>
              <h2>{language.name} Foundations</h2>
              <span className={styles.tag}>Beginner</span>
            </div>
            <p>{language.nativeName}</p>
            <p>{courseDescriptions[language.code]}</p>
          </article>
        ))}
      </section>
    </AppShell>
  );
}
