import styles from './AppPage.module.css'
import {useMeQuery} from "@/api/accountApi.ts";

export default function AppPage() {
    const {data, error, isLoading} = useMeQuery();

    return (
        <main className={styles.container}>
            <h1 className={styles.title}>Jinear</h1>
            <p className={styles.subtitle}>App shell — empty for now.</p>
            <p>{JSON.stringify(data)}</p>
        </main>
    )
}
