/**
 * volumes.js – Volumes list view
 */
const VolumesView = (() => {
    let allVolumes = [];

    function renderTable(volumes) {
        if (!volumes || volumes.length === 0) {
            return App.emptyState('No volumes found.');
        }

        const rows = volumes.map(vol => {
            const safeName    = App.escapeHtml(vol.name);
            const encodedName = encodeURIComponent(vol.name);
            const deleteBtn = `<button class="btn-action delete" title="Delete volume" onclick="VolumesView.remove('${encodedName}')">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"/>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
            </button>`;

            return `<tr class="no-hover">
                <td class="col-check">
                    <input type="checkbox" class="row-check" data-id="${encodedName}" onchange="VolumesView.onCheckChange()">
                </td>
                <td><span style="font-weight:500;">${safeName}</span></td>
                <td style="font-size:0.82rem; color:#6c757d;">${App.escapeHtml(vol.created)}</td>
                <td style="font-size:0.82rem;">${App.escapeHtml(vol.size)}</td>
                <td>${deleteBtn}</td>
            </tr>`;
        }).join('');

        return `<table class="data-table">
            <thead><tr>
                <th class="col-check"><input type="checkbox" class="row-check" id="volumes-check-all" onchange="VolumesView.toggleAll(this)"></th>
                <th>Name</th><th>Created</th><th>Size</th><th>Actions</th>
            </tr></thead>
            <tbody>${rows}</tbody>
        </table>`;
    }

    function onCheckChange() {
        const checked = document.querySelectorAll('#volumes-content .row-check[data-id]:checked').length;
        document.getElementById('volumes-bulk-delete').disabled = checked === 0;
        const all = document.querySelectorAll('#volumes-content .row-check[data-id]').length;
        const hdr = document.getElementById('volumes-check-all');
        if (hdr) hdr.checked = checked > 0 && checked === all;
    }

    function toggleAll(hdr) {
        document.querySelectorAll('#volumes-content .row-check[data-id]')
            .forEach(cb => cb.checked = hdr.checked);
        onCheckChange();
    }

    function getCheckedIds() {
        return [...document.querySelectorAll('#volumes-content .row-check[data-id]:checked')]
            .map(cb => cb.dataset.id);
    }

    function applyFilter() {
        const el   = document.getElementById('volumes-search');
        const term = el ? el.value.trim().toLowerCase() : '';
        const visible = term
            ? allVolumes.filter(vol => (vol.name || '').toLowerCase().includes(term))
            : allVolumes;
        document.getElementById('volumes-content').innerHTML = renderTable(visible);
        document.getElementById('volumes-bulk-delete').disabled = true;
    }

    function filter() { applyFilter(); }

    async function load() {
        const content = document.getElementById('volumes-content');
        content.innerHTML = '<div class="state-message">Loading volumes…</div>';
        try {
            allVolumes = await App.api('GET', '/volumes');
            App.setConnectionStatus('connected', 'Connected');
            applyFilter();
        } catch (err) {
            App.setConnectionStatus('error', 'Connection error');
            content.innerHTML = App.errorState('Cannot load volumes: ' + err.message);
            allVolumes = [];
        }
    }

    async function remove(encodedName) {
        if (!confirm('Delete this volume?')) return;
        try { await App.api('DELETE', `/volumes/${encodedName}`); await load(); }
        catch (err) { alert('Failed to delete volume: ' + err.message); }
    }

    async function bulkRemove() {
        const ids = getCheckedIds();
        if (ids.length === 0) return;
        if (!confirm(`Delete ${ids.length} volume(s)?`)) return;
        try {
            await Promise.all(ids.map(id => App.api('DELETE', `/volumes/${id}`)));
            await load();
        } catch (err) { alert('Failed to delete volumes: ' + err.message); }
    }

    return { load, filter, remove, bulkRemove, onCheckChange, toggleAll };
})();
