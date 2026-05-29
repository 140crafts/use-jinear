import { Link } from 'react-router-dom'
import styles from './AuthPage.module.css'

export default function RegisterPage() {
  return (
    <main className={styles.container}>
      <h1 className={styles.title}>Register</h1>
      <p className={styles.subtitle}>Create your Jinear account.</p>
      <Link to="/login" className={styles.link}>
        Already have an account? Login
      </Link>
    </main>
  )
}
