import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import LoginPage from '@/pages/auth/LoginPage.tsx'
import RegisterPage from '@/pages/auth/RegisterPage.tsx'
import AppPage from '@/pages/app/AppPage.tsx'
import OfflineBanner from '@/components/offline-banner/OfflineBanner.tsx'

export default function App() {
  return (
    <BrowserRouter>
      <OfflineBanner />
      <Routes>
        <Route path="/" element={<Navigate to="/app" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/app" element={<AppPage />} />
        <Route path="*" element={<Navigate to="/app" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
