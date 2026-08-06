import Link from "next/link";
import styles from "../app/workspace.module.css";

const navItems = [
  { href: "/", label: "Dashboard" },
  { href: "/courses", label: "Courses" },
  { href: "/lessons", label: "Lessons" },
  { href: "/practice", label: "Practice" },
  { href: "/progress", label: "Progress" },
  { href: "/admin", label: "Admin" },
];

export function AppShell({ children }: { children: React.ReactNode }) {
  return (
    <div className={styles.page}>
      <aside className={styles.sidebar}>
        <Link className={styles.brand} href="/">
          Fluento
        </Link>
        <nav className={styles.nav} aria-label="Primary navigation">
          {navItems.map((item) => (
            <Link key={item.href} href={item.href}>
              {item.label}
            </Link>
          ))}
        </nav>
      </aside>
      <main className={styles.main}>{children}</main>
    </div>
  );
}
