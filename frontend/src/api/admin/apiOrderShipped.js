import axiosClient from '../../states/API/axiosClient';

const apiOrderShipped = {
    putOrderShipped(id) {
        const url = `/admin/orders/${id}/shipped`;
        return axiosClient.put(url);
    },
};
export default apiOrderShipped;
