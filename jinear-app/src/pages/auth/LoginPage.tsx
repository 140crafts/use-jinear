import {useSearchParams} from 'react-router-dom'
import styles from './AuthPage.module.css'
import LoginWithMailForm from "@/components/form/login-with-mail-form/LoginWithMailForm.tsx";
import FormLogo from "@/components/form-logo/FormLogo.tsx";

export default function LoginPage() {
    const [searchParams] = useSearchParams();
    const email = searchParams?.get("email");

    return (
        <div className={styles.container}>
            <LoginWithMailForm className={styles.form} initialEmail={email as string | undefined}/>
            <FormLogo/>
        </div>
    )
}
