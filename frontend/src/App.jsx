import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import './index.css'
import { AppShell } from './layout/AppShell'
import { PosPage } from './pages/PosPage'
import { StoreProvider } from './store/StoreContext'
import { OrdersPage } from './pages/OrdersPage'
import { ProductsPage } from './pages/ProductsPage'
import { AreasTablesPage } from './pages/AreasTablesPage'
import { StaffPage } from './pages/StaffPage'
import { CustomersPage } from './pages/CustomersPage'
import { ReportsPage } from './pages/ReportsPage'
import { SettingsPage } from './pages/SettingsPage'
import { DashboardPage } from './pages/DashboardPage'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'
import { AdminPage } from './pages/AdminPage'

function App() {
  return (
    <BrowserRouter>
      <StoreProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route
            path="/*"
            element={
              <AppShell>
                <Routes>
                  <Route
                    path="/"
                    element={<Navigate to="/app/dashboard" replace />}
                  />
                  <Route path="/app/dashboard" element={<DashboardPage />} />
                  <Route path="/app/pos" element={<PosPage />} />
                  <Route path="/app/orders" element={<OrdersPage />} />
                  <Route path="/app/products" element={<ProductsPage />} />
                  <Route
                    path="/app/areas-tables"
                    element={<AreasTablesPage />}
                  />
                  <Route path="/app/staff" element={<StaffPage />} />
                  <Route path="/app/customers" element={<CustomersPage />} />
                  <Route path="/app/reports" element={<ReportsPage />} />
                  <Route path="/app/settings" element={<SettingsPage />} />
                  <Route path="/app/admin" element={<AdminPage />} />
                </Routes>
              </AppShell>
            }
          />
        </Routes>
      </StoreProvider>
    </BrowserRouter>
  )
}

export default App
