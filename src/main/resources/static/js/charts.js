document.addEventListener("DOMContentLoaded", () => {
    createChart(
        document.getElementById('priority-chart').getContext('2d'),
        'pie',
        ticketPriorityCounts.map(item => item.priority),
        ticketPriorityCounts.map(item => item.count),
        'Ticket Priority Counts',
        {
            backgroundColor: ['rgba(255, 206, 86, 0.6)', 'rgba(255, 99, 132, 0.6)', 'rgba(54, 162, 235, 0.6)', 'rgba(75, 192, 192, 0.6)'],
            borderColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)'],
            borderWidth: 1,
        }
    );

    createChart(
        document.getElementById('status-chart').getContext('2d'),
        'bar',
        ticketStatusCounts.map(item => item.status),
        ticketStatusCounts.map(item => item.count),
        'Ticket Status Counts',
        {
            backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(75, 192, 192, 0.2)'],
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
            label: 'Counts',
        }
    );

    createChart(
        document.getElementById('year-chart').getContext('2d'),
        'line',
        ticketsByYearCounts.map(item => item.year),
        ticketsByYearCounts.map(item => item.count),
        'Ticket By Year',
        {
            backgroundColor: ['rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(255, 0, 0, 0.2)',
                'rgba(0, 255, 0, 0.2)',
                'rgba(0, 0, 255, 0.2)',
                'rgba(255, 255, 0, 0.2)',
                'rgba(255, 165, 0, 0.2)',
                'rgba(128, 0, 128, 0.2)',
                'rgba(0, 128, 128, 0.2)',
                'rgba(255, 192, 203, 0.2)',
                'rgba(0, 255, 255, 0.2)',
                'rgba(255, 255, 255, 0.2)',],
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
            label: 'Total tickets',
        }
    );
});

function createChart(ctx, chartType, labels, data, title, options) {
    const chartConfig = {
        type: chartType,
        data: {
            labels: labels,
            datasets: [{
                data: data,
                ...options
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: title,
                    font: {
                        size: 16
                    }
                }
            },
            scales: chartType === 'bar' ? {
                y: {
                    beginAtZero: true
                }
            } : {}
        }
    };

    new Chart(ctx, chartConfig);
}