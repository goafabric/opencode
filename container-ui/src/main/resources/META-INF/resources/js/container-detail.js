/**
 * container-detail.js – Container detail view with logs tab
 */
const ContainerDetail = (() => {
    let currentId = null;

    function open(id, name) {
        currentId = id;
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
            const logs = (data && data.logs) ? data.logs : '(no log output)';
            output.textContent = logs;
            // Scroll to bottom
            output.scrollTop = output.scrollHeight;
        } catch (err) {
            output.textContent = 'Error loading logs: ' + err.message;
        }
    }

    function switchTab(tabName) {
        document.querySelectorAll('#view-container-detail .tab-card-tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('#view-container-detail .tab-pane').forEach(p => p.classList.remove('active'));

        const activeTab = document.querySelector(`#view-container-detail .tab-card-tab[data-tab="${tabName}"]`);
        const activePane = document.getElementById('tab-' + tabName);
        if (activeTab) activeTab.classList.add('active');
        if (activePane) activePane.classList.add('active');
    }

    return { open, loadLogs, switchTab };
})();
