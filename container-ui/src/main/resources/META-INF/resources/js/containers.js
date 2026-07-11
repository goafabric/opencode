/**
 * containers.js – Container list view
 */
const ContainersView = (() => {

    function stateBadge(state) {
        const normalized = (state || '').toLowerCase();
        const label = normalized.charAt(0).toUpperCase() + normalized.slice(1);
        return `<span class="badge-state ${normalized}"><span class="dot"></span>${App.escapeHtml(label)}</span>`;
    }

    function actionButtons(container) {
        const id = App.escapeHtml(container.id);
        const isRunning = container.state === 'running';

        const startBtn = `<button class="btn-action start" title="Start" onclick="event.stopPropagation(); ContainersView.start('${id}')" ${isRunning ? 'disabled style="opacity:0.3;cursor:not-allowed;"' : ''}>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polygon points="5 3 19 12 5 21 5 3"/>
            </svg>
        </button>`;

        const stopBtn = `<button class="btn-action stop" title="Stop" onclick="event.stopPropagation(); ContainersView.stop('${id}')" ${!isRunning ? 'disabled style="opacity:0.3;cursor:not-allowed;"' : ''}>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
            </svg>
        </button>`;

        const deleteBtn = `<button class="btn-action delete" title="Delete" onclick="event.stopPropagation(); ContainersView.remove('${id}')">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="3 6 5 6 21 6"/>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
            </svg>
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
                <td><span style="font-weight:500;">${App.escapeHtml(c.name || c.id.substring(0,12))}</span></td>
                <td style="color:#6c757d; font-size:0.82rem;">${App.escapeHtml(c.image)}</td>
                <td style="font-size:0.82rem;">${App.escapeHtml(c.ports || '—')}</td>
                <td style="font-size:0.82rem;">${App.escapeHtml(c.cpuPercent || '—')}</td>
                <td style="font-size:0.82rem;">${App.escapeHtml(c.memoryUsage || '—')}</td>
                <td>${stateBadge(c.state)}</td>
                <td onclick="event.stopPropagation()">${actionButtons(c)}</td>
            </tr>`;
        }).join('');

        return `<table class="data-table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Image</th>
                    <th>Ports</th>
                    <th>CPU %</th>
                    <th>Memory</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>`;
    }

    async function load() {
        const content = document.getElementById('containers-content');
        content.innerHTML = '<div class="state-message">Loading containers…</div>';

        try {
            const containers = await App.api('GET', '/containers');
            App.setConnectionStatus('connected', 'Connected');
            content.innerHTML = renderTable(containers);
        } catch (err) {
            App.setConnectionStatus('error', 'Connection error');
            content.innerHTML = App.errorState('Cannot connect to Docker socket: ' + err.message);
        }
    }

    async function start(id) {
        try {
            await App.api('POST', `/containers/${id}/start`);
            await load();
        } catch (err) {
            alert('Failed to start container: ' + err.message);
        }
    }

    async function stop(id) {
        try {
            await App.api('POST', `/containers/${id}/stop`);
            await load();
        } catch (err) {
            alert('Failed to stop container: ' + err.message);
        }
    }

    async function remove(id) {
        if (!confirm('Delete this container?')) return;
        try {
            await App.api('DELETE', `/containers/${id}`);
            await load();
        } catch (err) {
            alert('Failed to delete container: ' + err.message);
        }
    }

    return { load, start, stop, remove };
})();
