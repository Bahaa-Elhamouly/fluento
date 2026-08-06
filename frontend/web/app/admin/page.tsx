import { AppShell } from "../../components/AppShell";
import styles from "../workspace.module.css";

const adminAreas = [
  "Users",
  "Courses",
  "Lessons",
  "Vocabulary",
  "Grammar",
  "Quizzes",
  "Subscriptions",
  "Analytics",
];

export default function AdminPage() {
  return (
    <AppShell>
      <section className={styles.section}>
        <p className={styles.eyebrow}>Admin</p>
        <h1 className={styles.title}>Operational controls for the Fluento MVP.</h1>
        <p className={styles.lede}>
          The backend now exposes admin APIs for user management, content CRUD,
          subscription activation, and analytics overview.
        </p>
      </section>
      <section className={styles.grid}>
        {adminAreas.map((area) => (
          <article className={styles.item} key={area}>
            <h2>{area}</h2>
            <p>Manage {area.toLowerCase()} from the admin workspace.</p>
          </article>
        ))}
      </section>
    </AppShell>
  );
}
