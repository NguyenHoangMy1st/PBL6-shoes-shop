import axiosClient from '../../components/API/axiosClient';

const apiCreateOrder = {
    postCreateOrder(data) {
        console.log(data);
        const url = '/orders/';
        return axiosClient.post(url, data);
    },
};
export default apiCreateOrder;
