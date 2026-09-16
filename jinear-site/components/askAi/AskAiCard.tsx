"use client";

import React, { useEffect, useRef, useState } from "react";
import { SiClaude, SiGooglegemini } from "react-icons/si";
// Simple Icons dropped the OpenAI mark, so ChatGPT's comes from Remix Icon.
import { RiOpenaiFill } from "react-icons/ri";
import { LuChevronDown, LuChevronUp, LuClipboard, LuClipboardCheck } from "react-icons/lu";
import { ASK_AI_PROMPT } from "@/utils/constants";
import styles from "./AskAiCard.module.scss";

const COPIED_RESET_MS = 2000;
const ACTIONS_ID = "ask-ai-actions";
const FOOTER_SELECTOR = "[data-site-footer]";
/** Breathing space kept between the card and the footer it docks above. */
const FOOTER_GAP = 12;
const ENCODED_PROMPT = encodeURIComponent(ASK_AI_PROMPT);

const ASSISTANTS = [
  { id: "claude", label: "Claude", href: `https://claude.ai/new?q=${ENCODED_PROMPT}`, Icon: SiClaude },
  { id: "chatgpt", label: "ChatGPT", href: `https://chatgpt.com/?prompt=${ENCODED_PROMPT}`, Icon: RiOpenaiFill },
  { id: "gemini", label: "Gemini", href: `https://www.google.com/search?udm=50&q=${ENCODED_PROMPT}`, Icon: SiGooglegemini },
];

const AskAiCard: React.FC = () => {
  const cardRef = useRef<HTMLElement>(null);
  const [copied, setCopied] = useState(false);
  // Collapsed while the card would overlap the reading column. Above that width
  // the stylesheet keeps the actions open, so this only drives narrow screens.
  const [expanded, setExpanded] = useState(false);

  // Dock above the footer instead of covering it: once the footer scrolls up
  // into the card, lift the card by exactly that overlap. This replaces
  // reserving blank space under the page, which needed a guessed height.
  useEffect(() => {
    const card = cardRef.current;
    if (!card) {
      return;
    }

    let frame = 0;

    const update = () => {
      frame = 0;
      const footer = document.querySelector(FOOTER_SELECTOR);
      if (!footer) {
        card.style.transform = "";
        return;
      }
      // offsetHeight and the resolved "bottom" ignore the transform, so the
      // measurement never feeds back into itself.
      const gutter = parseFloat(window.getComputedStyle(card).bottom) || 0;
      const restingBottom = window.innerHeight - gutter;
      const overlap = restingBottom + FOOTER_GAP - footer.getBoundingClientRect().top;
      card.style.transform = overlap > 0 ? `translateY(${-overlap}px)` : "";
    };

    const schedule = () => {
      if (!frame) {
        frame = window.requestAnimationFrame(update);
      }
    };

    update();
    window.addEventListener("scroll", schedule, { passive: true });
    window.addEventListener("resize", schedule);
    const observer = typeof ResizeObserver === "undefined" ? null : new ResizeObserver(schedule);
    observer?.observe(card);

    return () => {
      if (frame) {
        window.cancelAnimationFrame(frame);
      }
      window.removeEventListener("scroll", schedule);
      window.removeEventListener("resize", schedule);
      observer?.disconnect();
    };
  }, []);

  const onCopy = async () => {
    try {
      await navigator.clipboard.writeText(ASK_AI_PROMPT);
      setCopied(true);
      window.setTimeout(() => setCopied(false), COPIED_RESET_MS);
    } catch {
      // Clipboard access can be blocked (insecure context, denied permission).
      setCopied(false);
    }
  };

  return (
    <aside
      ref={cardRef}
      className={styles.card}
      data-expanded={expanded}
      aria-label="Ask an AI about Jinear"
    >
      <button
        className={styles.head}
        type="button"
        onClick={() => setExpanded((open) => !open)}
        aria-expanded={expanded}
        aria-controls={ACTIONS_ID}
      >
        <span className={styles.headText}>
          <span className={styles.title}>Ask an AI about Jinear</span>
          <span className={styles.text}>Opens your favourite assistant with a ready-made question.</span>
        </span>
        {expanded ? (
          <LuChevronDown className={styles.chevron} aria-hidden="true" />
        ) : (
          <LuChevronUp className={styles.chevron} aria-hidden="true" />
        )}
      </button>
      <div className={styles.actions} id={ACTIONS_ID}>
        {ASSISTANTS.map(({ id, label, href, Icon }) => (
          <a key={id} className={styles.btn} href={href} target="_blank" rel="noreferrer">
            <Icon className={styles.icon} aria-hidden="true" />
            {label}
          </a>
        ))}
        <button className={styles.btn} type="button" onClick={onCopy}>
          {copied ? (
            <LuClipboardCheck className={styles.icon} aria-hidden="true" />
          ) : (
            <LuClipboard className={styles.icon} aria-hidden="true" />
          )}
          {copied ? "Copied" : "Copy Prompt"}
        </button>
      </div>
    </aside>
  );
};

export default AskAiCard;
