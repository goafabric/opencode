/**
 * containers.js – Container list view
 */
const ContainersView = (() => {
    let autoRefreshTimer  = null;
    let statsRefreshTimer = null;
    let lastSnapshot      = '';
    let allContainers     = [];

    const REFRESH_INTERVAL = 2000;
    const STATS_INTERVAL   = 5000;

    function stateBadge(state) {
        const normalized = (state || '').toLowerCase();
        const label = normalized.charAt(0).toUpperCase() + normalized.slice(1);
        return `<span class="badge-state ${normalized}"><span class="dot"></span>${App.escapeHtml(label)}</span>`;
    }

    function actionButtons(container) {
        const id = App.escapeHtml(container.id);
        const isRunning = container.state === 'running';

        const startBtn = `<button class="btn-action start" title="Start" onclick="event.stopPropagation(); ContainersView.start('${id}')" ${isRunning ? 'disabled style="opacity:0.3;cursor:not-allowed;"' : ''}>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="5 3 19 12 5 21 5 3"/></svg>
        </button>`;

        const stopBtn = `<button class="btn-action stop" title="Stop" onclick="event.stopPropagation(); ContainersView.stop('${id}')" ${!isRunning ? 'disabled style="opacity:0.3;cursor:not-allowed;"' : ''}>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/></svg>
        </button>`;

        const deleteBtn = `<button class="btn-action delete" title="Delete" onclick="event.stopPropagation(); ContainersView.remove('${id}')">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/></svg>
        </button>`;

        return `<div style="display:flex; gap:2px;">${startBtn}${stopBtn}${deleteBtn}</div>`;
    }

    function renderTable(containers) {
        if (!containers || containers.length === 0) {
            return App.emptyState('No containers found.');
        }

        const rows = containers.map(c => {
            const id = App.escapeHtml(c.id);
            return `<tr onclick="ContainerDetail.open('${id}', '${App.escapeHtml(c.name || c.id.substring(0,12))}')">
                <td class="col-check" onclick="event.stopPropagation()">
                    <input type="checkbox" class="row-check" data-id="${id}" onchange="ContainersView.onCheckChange()">
                </td>
                <td><span style="font-weight:500;">${App.escapeHtml(c.name || c.id.substring(0,12))}</span></td>
                <td style="color:#6c757d; font-size:0.82rem;">${App.escapeHtml(c.image)}</td>
                <td style="font-size:0.82rem;">${App.escapeHtml(c.ports || '—')}</td>
                <td id="cpu-${c.id}" style="font-size:0.82rem; color:#adb5bd;">…</td>
                <td id="mem-${c.id}" style="font-size:0.82rem; color:#adb5bd;">…</td>
                <td id="state-${c.id}">${stateBadge(c.state)}</td>
                <td id="actions-${c.id}" onclick="event.stopPropagation()">${actionButtons(c)}</td>
            </tr>`;
        }).join('');

        return `<table class="data-table">
            <thead><tr>
                <th class="col-check"><input type="checkbox" class="row-check" id="containers-check-all" onchange="ContainersView.toggleAll(this)"></th>
                <th>Name</th><th>Image</th><th>Ports</th>
                <th>CPU %</th><th>Memory</th><th>Status</th><th>Actions</th>
            </tr></thead>
            <tbody>${rows}</tbody>
        </table>`;
    }

    function onCheckChange() {
        const checked = document.querySelectorAll('#containers-content .row-check[data-id]:checked').length;
        document.getElementById('containers-bulk-delete').disabled = checked === 0;
        const all = document.querySelectorAll('#containers-content .row-check[data-id]').length;
        const hdr = document.getElementById('containers-check-all');
        if (hdr) hdr.checked = checked > 0 && checked === all;
    }

    function toggleAll(hdr) {
        document.querySelectorAll('#containers-content .row-check[data-id]')
            .forEach(cb => cb.checked = hdr.checked);
        onCheckChange();
    }

    function getCheckedIds() {
        return [...document.querySelectorAll('#containers-content .row-check[data-id]:checked')]
            .map(cb => cb.dataset.id);
    }

    function getSearchTerm() {
        const el = document.getElementById('containers-search');
        return el ? el.value.trim().toLowerCase() : '';
    }

    function applyFilter() {
        const term = getSearchTerm();
        const visible = term
            ? allContainers.filter(c =>
                (c.name  || '').toLowerCase().includes(term) ||
                (c.image || '').toLowerCase().includes(term) ||
                (c.state || '').toLowerCase().includes(term) ||
                (c.ports || '').toLowerCase().includes(term))
            : allContainers;
        document.getElementById('containers-content').innerHTML = renderTable(visible);
        document.getElementById('containers-bulk-delete').disabled = true;
    }

    function filter() { applyFilter(); }

    function listSignature(containers) {
        return containers.map(c => c.id + c.state).join(',');
    }

    async function loadStats() {
        try {
            const statsList = await App.api('GET', '/containers/stats');
            statsList.forEach(s => {
                const cpuEl = document.getElementById('cpu-' + s.containerId);
                const memEl = document.getElementById('mem-' + s.containerId);
                if (cpuEl) { cpuEl.textContent = s.cpuPercent;  cpuEl.style.color = ''; }
                if (memEl) { memEl.textContent = s.memoryUsage; memEl.style.color = ''; }
            });
        } catch (_) {}
    }

    async function load() {
        const content = document.getElementById('containers-content');
        if (!content.querySelector('table')) {
            content.innerHTML = '<div class="state-message">Loading containers…</div>';
        }
        try {
            const containers = await App.api('GET', '/containers');
            App.setConnectionStatus('connected', 'Connected');
            const sig = listSignature(containers);
            if (sig !== lastSnapshot) {
                lastSnapshot  = sig;
                allContainers = containers;
                applyFilter();
            } else {
                containers.forEach(c => {
                    const stateEl   = document.getElementById('state-'   + c.id);
                    const actionsEl = document.getElementById('actions-' + c.id);
                    if (stateEl)   stateEl.innerHTML   = stateBadge(c.state);
                    if (actionsEl) actionsEl.innerHTML = actionButtons(c);
                });
            }
        } catch (err) {
            App.setConnectionStatus('error', 'Connection error');
            content.innerHTML = App.errorState('Cannot connect to container CLI: ' + err.message);
            lastSnapshot = ''; allContainers = [];
        }
    }

    function startAutoRefresh() {
        stopAutoRefresh();
        lastSnapshot      = '';
        autoRefreshTimer  = setInterval(load,      REFRESH_INTERVAL);
        statsRefreshTimer = setInterval(loadStats,  STATS_INTERVAL);
        loadStats();
    }

    function stopAutoRefresh() {
        if (autoRefreshTimer  !== null) { clearInterval(autoRefreshTimer);  autoRefreshTimer  = null; }
        if (statsRefreshTimer !== null) { clearInterval(statsRefreshTimer); statsRefreshTimer = null; }
    }

    async function start(id) {
        try { await App.api('POST', `/containers/${id}/start`); lastSnapshot = ''; await load(); }
        catch (err) { alert('Failed to start container: ' + err.message); }
    }

    async function stop(id) {
        try { await App.api('POST', `/containers/${id}/stop`); lastSnapshot = ''; await load(); }
        catch (err) { alert('Failed to stop container: ' + err.message); }
    }

    async function remove(id) {
        if (!confirm('Delete this container?')) return;
        try { await App.api('DELETE', `/containers/${id}`); lastSnapshot = ''; await load(); }
        catch (err) { alert('Failed to delete container: ' + err.message); }
    }

    async function bulkRemove() {
        const ids = getCheckedIds();
        if (ids.length === 0) return;
        if (!confirm(`Delete ${ids.length} container(s)?`)) return;
        try {
            await Promise.all(ids.map(id => App.api('DELETE', `/containers/${id}`)));
            lastSnapshot = '';
            await load();
        } catch (err) { alert('Failed to delete containers: ' + err.message); }
    }

    return { load, filter, start, stop, remove, bulkRemove, onCheckChange, toggleAll, startAutoRefresh, stopAutoRefresh };
})();
