/**
 * images.js – Images list view
 */
const ImagesView = (() => {
    let allImages = [];

    function renderTable(images) {
        if (!images || images.length === 0) {
            return App.emptyState('No images found.');
        }

        const rows = images.map(img => {
            const deleteBtn = `<button class="btn-action delete" title="Delete image" onclick="ImagesView.remove('${App.escapeHtml(img.id)}')">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"/>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
            </button>`;

            return `<tr class="no-hover">
                <td><span style="font-weight:500;">${App.escapeHtml(img.name)}</span></td>
                <td><span style="background:#e9ecef; padding:2px 8px; border-radius:4px; font-size:0.78rem; font-family:monospace;">${App.escapeHtml(img.tag)}</span></td>
                <td style="font-size:0.82rem; color:#6c757d;">${App.escapeHtml(img.created)}</td>
                <td style="font-size:0.82rem;">${App.escapeHtml(img.size)}</td>
                <td>${deleteBtn}</td>
            </tr>`;
        }).join('');

        return `<table class="data-table">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Tag</th>
                    <th>Created</th>
                    <th>Size</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>`;
    }

    function applyFilter() {
        const el   = document.getElementById('images-search');
        const term = el ? el.value.trim().toLowerCase() : '';
        const visible = term
            ? allImages.filter(img =>
                (img.name || '').toLowerCase().includes(term) ||
                (img.tag  || '').toLowerCase().includes(term))
            : allImages;
        document.getElementById('images-content').innerHTML = renderTable(visible);
    }

    function filter() {
        applyFilter();
    }

    async function load() {
        const content = document.getElementById('images-content');
        content.innerHTML = '<div class="state-message">Loading images…</div>';

        try {
            allImages = await App.api('GET', '/images');
            App.setConnectionStatus('connected', 'Connected');
            applyFilter();
        } catch (err) {
            App.setConnectionStatus('error', 'Connection error');
            content.innerHTML = App.errorState('Cannot load images: ' + err.message);
            allImages = [];
        }
    }

    async function remove(id) {
        if (!confirm('Delete this image?')) return;
        try {
            await App.api('DELETE', `/images/${encodeURIComponent(id)}`);
            await load();
        } catch (err) {
            alert('Failed to delete image: ' + err.message);
        }
    }

    return { load, filter, remove };
})();
