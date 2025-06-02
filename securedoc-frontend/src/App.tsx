import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import DashboardPage from '@/pages/DashboardPage.tsx'
import MainPage from '@/pages/MainPage.tsx'
import RegisterPage from '@/pages/auth/RegisterPage.tsx'
import LoginPage from '@/pages/auth/LoginPage.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import ProtectedRoute from "@/lib/protected-route.tsx";
import {CookiesProvider} from "react-cookie";

function App() {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 1000 * 60 * 5,
        refetchInterval: 1000 * 60 * 10,
        refetchOnWindowFocus: true,
        enabled: true,
      },
    },
  })

  return (
    <QueryClientProvider client={queryClient}>
      <CookiesProvider>
        <BrowserRouter>
          <Routes>
            <Route path={'/'} element={<MainPage />} />
            {/* Authentication routes */}
            <Route path={'/login'} element={<LoginPage />} />
            <Route path={'/register'} element={<RegisterPage />} />
            {/* Dashboard routes */}
            <Route path={'/dashboard'} element={<ProtectedRoute> <DashboardPage /> </ProtectedRoute>} />
            <Route path={'*'} element={<Navigate to={'/'} replace />} />
          </Routes>
        </BrowserRouter>
      </CookiesProvider>
    </QueryClientProvider>
  )
}

export default App
