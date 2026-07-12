/**
 * container-detail.js – Container detail view with logs tab
 */
const ContainerDetail = (() => {
    let currentId = null;
    let rawLogs = '';

    function open(id, name) {
        currentId = id;
        rawLogs = '';
        const searchInput = document.getElementById('log-search');
        if (searchInput) searchInput.value = '';
        document.getElementById('detail-container-name').textContent = name || id.substring(0, 12);
        App.navigate('container-detail');
        loadLogs();
    }

    async function loadLogs() {
        if (!currentId) return;
        const output = document.getElementById('log-output');
        output.textContent = 'Loading logs…';

        try {
            const data = await App.api('GET', `/containers/${currentId}/logs`);
            rawLogs = (data && data.logs) ? data.logs : '(no log output)';
            renderLogs();
        } catch (err) {
            rawLogs = '';
            output.textContent = 'Error loading logs: ' + err.message;
        }
    }

    function filterLogs() {
        renderLogs();
    }

    function renderLogs() {
        const output = document.getElementById('log-output');
        const searchInput = document.getElementById('log-search');
        const term = searchInput ? searchInput.value.trim() : '';

        if (!term) {
            output.textContent = rawLogs;
        } else {
            const lines = rawLogs.split('\n');
            const lower = term.toLowerCase();
            const filtered = lines.filter(line => line.toLowerCase().includes(lower));
            output.textContent = filtered.length > 0
                ? filtered.join('\n')
                : '(no lines match "' + term + '")';
        }
        // Scroll to bottom
        output.scrollTop = output.scrollHeight;
    }

    function switchTab(tabName) {
        document.querySelectorAll('#view-container-detail .tab-card-tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('#view-container-detail .tab-pane').forEach(p => p.classList.remove('active'));

        const activeTab = document.querySelector(`#view-container-detail .tab-card-tab[data-tab="${tabName}"]`);
        const activePane = document.getElementById('tab-' + tabName);
        if (activeTab) activeTab.classList.add('active');
        if (activePane) activePane.classList.add('active');
    }

    return { open, loadLogs, filterLogs, switchTab };
})();
