import { useOnlineStatus } from '@/hooks/useOnlineStatus.ts'
import styles from './OfflineBanner.module.css'

export default function OfflineBanner() {
  const online = useOnlineStatus()
  if (online) return null
  return (
    <div className={styles.banner} role="status" aria-live="polite">
      You&apos;re offline, showing cached data.
    </div>
  )
}
