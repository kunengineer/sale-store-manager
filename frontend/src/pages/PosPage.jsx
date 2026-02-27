import { PosTableColumn } from '../pos/PosTableColumn'
import { PosMenuColumn } from '../pos/PosMenuColumn'
import { PosOrderColumn } from '../pos/PosOrderColumn'
import { PosProvider } from '../pos/PosContext'

export function PosPage() {
  return (
    <PosProvider>
      <div className="flex h-[calc(100vh-3.5rem)] gap-2 overflow-hidden px-2 py-2">
        <PosTableColumn />
        <PosMenuColumn />
        <PosOrderColumn />
      </div>
    </PosProvider>
  )
}

