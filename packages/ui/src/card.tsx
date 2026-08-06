import { type AnchorHTMLAttributes, type JSX, type ReactNode } from "react";

export function Card({
  className,
  title,
  children,
  href,
  ...props
}: AnchorHTMLAttributes<HTMLAnchorElement> & {
  className?: string;
  title: string;
  children: ReactNode;
  href: string;
}): JSX.Element {
  return (
    <a className={className} href={href} {...props}>
      <h2>{title}</h2>
      <p>{children}</p>
    </a>
  );
}
