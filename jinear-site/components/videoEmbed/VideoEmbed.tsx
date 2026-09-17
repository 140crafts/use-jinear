"use client";

import { useState } from "react";
import styles from "./VideoEmbed.module.scss";

interface Props {
  id: string;
  title: string;
}

const poster = (id: string) => `https://img.youtube.com/vi/${id}/maxresdefault.jpg`;
// Not every upload has a maxres thumbnail; hqdefault always exists.
const fallbackPoster = (id: string) => `https://img.youtube.com/vi/${id}/hqdefault.jpg`;

/**
 * Click to load YouTube embed. Nothing is requested from YouTube until the viewer
 * asks for the video, so the page loads no third party script or cookie on its own.
 * The caption link keeps the video reachable without JavaScript.
 */
export default function VideoEmbed({ id, title }: Props) {
  const [playing, setPlaying] = useState(false);
  const [thumb, setThumb] = useState(() => poster(id));

  return (
    <figure className={styles.wrap}>
      <div className={styles.frame}>
        {playing ? (
          <iframe
            className={styles.player}
            src={`https://www.youtube-nocookie.com/embed/${id}?autoplay=1&rel=0`}
            title={title}
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          />
        ) : (
          <button
            type="button"
            className={styles.poster}
            onClick={() => setPlaying(true)}
            aria-label={`Play video: ${title}`}
          >
            <img
              className={styles.thumb}
              src={thumb}
              alt=""
              loading="lazy"
              onError={() => setThumb(fallbackPoster(id))}
            />
            <span className={styles.play} aria-hidden="true">
              <span className={styles.triangle} />
            </span>
          </button>
        )}
      </div>
      <figcaption className={styles.caption}>
        {title}{" "}
        <a href={`https://youtu.be/${id}`} target="_blank" rel="noopener noreferrer">
          Watch on YouTube
        </a>
      </figcaption>
    </figure>
  );
}
