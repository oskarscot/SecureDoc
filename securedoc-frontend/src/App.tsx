import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import DashboardPage from './pages/DashboardPage'
import MainPage from '@/pages/MainPage.tsx'
import RegisterPage from '@/pages/auth/RegisterPage.tsx'
import LoginPage from '@/pages/auth/LoginPage.tsx'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={'/'} element={<MainPage />} />
        {/* Authentication routes */}
        <Route path={'/login'} element={<LoginPage />} />
        <Route path={'/register'} element={<RegisterPage />} />
        {/* Dashboard routes */}
        <Route path={'/dashboard'} element={<DashboardPage />} />
        <Route path={'*'} element={<Navigate to={'/'} replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
