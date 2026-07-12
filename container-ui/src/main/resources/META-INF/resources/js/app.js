/**
 * app.js – navigation, routing and shared utilities for Container UI
 */
const App = (() => {
    const views = ['containers', 'images', 'volumes', 'container-detail'];
    let currentView = 'containers';

    function navigate(viewName) {
        // Hide all views
        views.forEach(v => {
            const el = document.getElementById('view-' + v);
            if (el) el.classList.remove('active');
        });

        // Deactivate sidebar items
        document.querySelectorAll('.sidebar-item').forEach(item => item.classList.remove('active'));

        // Show target view
        const target = document.getElementById('view-' + viewName);
        if (target) target.classList.add('active');

        // Activate sidebar item (only for top-level views)
        const sidebarItem = document.querySelector(`.sidebar-item[data-view="${viewName}"]`);
        if (sidebarItem) sidebarItem.classList.add('active');

        // Stop container auto-refresh when leaving containers view
        if (currentView !== viewName && typeof ContainersView !== 'undefined') {
            ContainersView.stopAutoRefresh();
        }

        currentView = viewName;

        // Load data for the view
        switch (viewName) {
            case 'containers':
                ContainersView.load();
                ContainersView.startAutoRefresh();
                break;
            case 'images':          ImagesView.load(); break;
            case 'volumes':         VolumesView.load(); break;
            case 'container-detail': /* loaded externally */ break;
        }
    }

    function setConnectionStatus(state, label) {
        const el = document.getElementById('connection-status');
        el.className = '';
        el.classList.add(state);
        document.getElementById('status-label').textContent = label;
    }

    async function api(method, path, body) {
        const opts = { method, headers: { 'Content-Type': 'application/json' } };
        if (body) opts.body = JSON.stringify(body);
        const resp = await fetch('/api' + path, opts);
        if (!resp.ok) {
            const text = await resp.text();
            throw new Error(`${resp.status}: ${text}`);
        }
        if (resp.status === 204) return null;
        return resp.json();
    }

    async function apiText(method, path) {
        const resp = await fetch('/api' + path, { method });
        if (!resp.ok) throw new Error(`${resp.status}`);
        return resp.text();
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
    }

    function emptyState(message) {
        return `<div class="state-message">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="8" x2="12" y2="12"/>
                <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            <div>${escapeHtml(message)}</div>
        </div>`;
    }

    function errorState(message) {
        return `<div class="state-message" style="color:#dc3545;">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="8" x2="12" y2="12"/>
                <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            <div>${escapeHtml(message)}</div>
        </div>`;
    }

    // Initialize sidebar navigation
    document.querySelectorAll('.sidebar-item[data-view]').forEach(item => {
        item.addEventListener('click', () => navigate(item.dataset.view));
    });

    // Initial load
    window.addEventListener('DOMContentLoaded', () => {
        navigate('containers');
    });

    return { navigate, setConnectionStatus, api, apiText, escapeHtml, emptyState, errorState };
})();
