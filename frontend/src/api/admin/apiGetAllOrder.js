import axiosClient from '../../states/API/axiosClient';

const apiGetAllOrder = {
    getAllOrder() {
        const url = '/admin/orders/';
        return axiosClient.get(url);
    },
};
export default apiGetAllOrder;
