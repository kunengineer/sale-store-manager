import PropTypes from 'prop-types'

import { Sidebar } from '../layout/Sidebar'
import { Header } from '../layout/Header'
import { useStore } from '../store/StoreContext'

export function AppShell({ children }) {
  const { theme } = useStore()

  const shellClass =
    theme === 'dark' ? 'app-shell app-shell--dark' : 'app-shell app-shell--light'

  return (
    <div className={shellClass}>
      <Sidebar />
      <div className="flex flex-1 flex-col bg-[var(--bg)] text-[var(--text)]">
        <Header />
        <main className="flex-1 overflow-hidden bg-[var(--bg)] text-[var(--text)]">
          {children}
        </main>
      </div>
    </div>
  )
}

AppShell.propTypes = {
  children: PropTypes.node,
}

