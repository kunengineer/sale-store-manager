import { useState, useEffect, useCallback } from "react";

// ============================================================
// TOAST CONTEXT & HOOK
// ============================================================
import { createContext, useContext } from "react";

const ToastContext = createContext(null);

// eslint-disable-next-line react-refresh/only-export-components
export function useToast() {
  const ctx = useContext(ToastContext);
  if (!ctx) throw new Error("useToast must be used inside <ToastProvider>");
  return ctx;
}

// ============================================================
// TOAST CONFIG
// ============================================================
const CONFIGS = {
  success: {
    icon: "✦",
    label: "SUCCESS",
    bar: "#22d3a5",
    glow: "rgba(34,211,165,0.25)",
    badge: "#022c22",
    badgeText: "#22d3a5",
    particle: ["✦", "◆", "★"],
  },
  error: {
    icon: "✖",
    label: "ERROR",
    bar: "#f43f5e",
    glow: "rgba(244,63,94,0.25)",
    badge: "#2d0a14",
    badgeText: "#f43f5e",
    particle: ["✖", "✕", "×"],
  },
  warning: {
    icon: "◈",
    label: "WARNING",
    bar: "#fb923c",
    glow: "rgba(251,146,60,0.25)",
    badge: "#2d1500",
    badgeText: "#fb923c",
    particle: ["◈", "◇", "⬡"],
  },
  info: {
    icon: "◉",
    label: "INFO",
    bar: "#38bdf8",
    glow: "rgba(56,189,248,0.25)",
    badge: "#001d2d",
    badgeText: "#38bdf8",
    particle: ["◉", "○", "◎"],
  },
};

// ============================================================
// SINGLE TOAST ITEM
// ============================================================
function ToastItem({ id, type, title, message, duration = 4000, onRemove }) {
  const [visible, setVisible] = useState(false);
  const [leaving, setLeaving] = useState(false);
  const [progress, setProgress] = useState(100);
  const cfg = CONFIGS[type];
  const [particles, setParticles] = useState(() =>
    Array.from({ length: 6 }, (_, i) => ({
      id: i,
      char: cfg.particle[i % cfg.particle.length],
      x: Math.random() * 80 - 10,
      y: Math.random() * -40 - 10,
    }))
  );

  // mount animation + clear particles
  useEffect(() => {
    requestAnimationFrame(() => setVisible(true));
    const t = setTimeout(() => setParticles([]), 900);
    return () => clearTimeout(t);
  }, []);

  const handleClose = useCallback(() => {
    setLeaving(true);
    setTimeout(() => onRemove(id), 400);
  }, [id, onRemove]);

  // progress bar + auto dismiss
  useEffect(() => {
    const start = Date.now();
    const interval = setInterval(() => {
      const elapsed = Date.now() - start;
      const remaining = Math.max(0, 100 - (elapsed / duration) * 100);
      setProgress(remaining);
      if (elapsed >= duration) {
        clearInterval(interval);
        handleClose();
      }
    }, 30);
    return () => clearInterval(interval);
  }, [duration, handleClose]);

  return (
    <div
      style={{
        position: "relative",
        marginBottom: "12px",
        transform: visible && !leaving
          ? "translateX(0) scale(1)"
          : leaving
          ? "translateX(120%) scale(0.9)"
          : "translateX(120%) scale(0.9)",
        opacity: visible && !leaving ? 1 : 0,
        transition: "transform 0.4s cubic-bezier(0.34,1.56,0.64,1), opacity 0.4s ease",
        willChange: "transform, opacity",
      }}
    >
      {/* Particles */}
      {particles.map((p) => (
        <span
          key={p.id}
          style={{
            position: "absolute",
            left: `${p.x}%`,
            top: `${p.y}px`,
            color: cfg.bar,
            fontSize: "10px",
            pointerEvents: "none",
            animation: "particleFly 0.9s ease-out forwards",
            zIndex: 10,
          }}
        >
          {p.char}
        </span>
      ))}

      {/* Card */}
      <div
        style={{
          width: "340px",
          background: "rgba(10,12,18,0.97)",
          border: `1px solid ${cfg.bar}44`,
          borderRadius: "14px",
          overflow: "hidden",
          boxShadow: `0 8px 32px rgba(0,0,0,0.5), 0 0 0 1px ${cfg.bar}22, inset 0 1px 0 rgba(255,255,255,0.05)`,
          backdropFilter: "blur(20px)",
        }}
      >
        {/* Glow top */}
        <div style={{
          position: "absolute",
          top: 0, left: "20%", right: "20%",
          height: "1px",
          background: `linear-gradient(90deg, transparent, ${cfg.bar}, transparent)`,
          borderRadius: "999px",
        }} />

        <div style={{ padding: "14px 16px 10px" }}>
          <div style={{ display: "flex", alignItems: "flex-start", gap: "12px" }}>
            {/* Icon badge */}
            <div style={{
              flexShrink: 0,
              width: "36px",
              height: "36px",
              borderRadius: "10px",
              background: cfg.badge,
              border: `1px solid ${cfg.bar}55`,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              fontSize: "16px",
              color: cfg.bar,
              boxShadow: `0 0 12px ${cfg.glow}`,
              animation: "iconPulse 2s ease-in-out infinite",
            }}>
              {cfg.icon}
            </div>

            {/* Text */}
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ display: "flex", alignItems: "center", gap: "8px", marginBottom: "3px" }}>
                <span style={{
                  fontSize: "9px",
                  fontWeight: 700,
                  letterSpacing: "0.15em",
                  color: cfg.bar,
                  fontFamily: "'Courier New', monospace",
                }}>
                  {cfg.label}
                </span>
                <div style={{
                  flex: 1,
                  height: "1px",
                  background: `linear-gradient(90deg, ${cfg.bar}44, transparent)`,
                }} />
              </div>
              <p style={{
                margin: 0,
                fontSize: "13px",
                fontWeight: 600,
                color: "#f1f5f9",
                fontFamily: "'Courier New', monospace",
                lineHeight: 1.3,
              }}>
                {title}
              </p>
              {message && (
                <p style={{
                  margin: "4px 0 0",
                  fontSize: "11px",
                  color: "#64748b",
                  fontFamily: "'Courier New', monospace",
                  lineHeight: 1.4,
                }}>
                  {message}
                </p>
              )}
            </div>

            {/* Close btn */}
            <button
              onClick={handleClose}
              style={{
                flexShrink: 0,
                background: "none",
                border: "1px solid #1e293b",
                borderRadius: "6px",
                width: "22px",
                height: "22px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                cursor: "pointer",
                color: "#475569",
                fontSize: "10px",
                transition: "all 0.2s",
              }}
              onMouseEnter={e => {
                e.currentTarget.style.borderColor = cfg.bar;
                e.currentTarget.style.color = cfg.bar;
                e.currentTarget.style.background = cfg.badge;
              }}
              onMouseLeave={e => {
                e.currentTarget.style.borderColor = "#1e293b";
                e.currentTarget.style.color = "#475569";
                e.currentTarget.style.background = "none";
              }}
            >
              ✕
            </button>
          </div>
        </div>

        {/* Progress bar */}
        <div style={{
          height: "2px",
          background: "#0f172a",
          position: "relative",
          overflow: "hidden",
        }}>
          <div style={{
            position: "absolute",
            left: 0, top: 0, bottom: 0,
            width: `${progress}%`,
            background: `linear-gradient(90deg, ${cfg.bar}88, ${cfg.bar})`,
            transition: "width 0.03s linear",
            boxShadow: `0 0 8px ${cfg.bar}`,
          }} />
        </div>
      </div>
    </div>
  );
}

// ============================================================
// PROVIDER + CONTAINER
// ============================================================
export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const show = useCallback((type, title, message, duration) => {
    const id = Date.now() + Math.random();
    setToasts((prev) => [...prev, { id, type, title, message, duration }]);
  }, []);

  const remove = useCallback((id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const toast = {
    success: (title, message, duration) => show("success", title, message, duration),
    error: (title, message, duration) => show("error", title, message, duration),
    warning: (title, message, duration) => show("warning", title, message, duration),
    info: (title, message, duration) => show("info", title, message, duration),
  };

  return (
    <ToastContext.Provider value={toast}>
      <style>{`
        @keyframes particleFly {
          0%   { transform: translate(0, 0) scale(1); opacity: 1; }
          100% { transform: translate(var(--px, 20px), -50px) scale(0); opacity: 0; }
        }
        @keyframes iconPulse {
          0%, 100% { box-shadow: 0 0 8px var(--glow); }
          50%       { box-shadow: 0 0 18px var(--glow); }
        }
      `}</style>

      {children}

      {/* Toast container */}
      <div style={{
        position: "fixed",
        bottom: "24px",
        right: "24px",
        zIndex: 9999,
        display: "flex",
        flexDirection: "column-reverse",
        pointerEvents: "none",
      }}>
        {toasts.map((t) => (
          <div key={t.id} style={{ pointerEvents: "auto" }}>
            <ToastItem {...t} onRemove={remove} />
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
}