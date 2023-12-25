import axiosClient from '../axiosClient';

const apiAdminDashboard = {
    getSummaryStatisticsForSelectedDay(selectedDay) {
        const url = `/api/admin/dashboard/stats/selected-day?selectedDay=${selectedDay}`;
        return axiosClient.get(url);
    },
    getSummaryStatisticsForSelectedMonth(selectedMonth) {
        const url = `/api/admin/dashboard/stats/selected-month?selectedMonth=${selectedMonth}`;
        return axiosClient.get(url);
    },
    getStatisticalChartForSelectedDay(selectedDay) {
        const url = `/api/admin/dashboard/line-chart/selected-day?selectedDay=${selectedDay}`;
        return axiosClient.get(url);
    },
    getStatisticalChartForSelectedMonth(selectedMonth) {
        const url = `/api/admin/dashboard/line-chart/selected-month?selectedMonth=${selectedMonth}`;
        return axiosClient.get(url);
    },
};

export default apiAdminDashboard;
