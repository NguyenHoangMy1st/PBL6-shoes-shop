import axiosClient from '../../components/API/axiosClient';

const apiCart = {
    getAllCart() {
        const url = '/cart/';
        return axiosClient.get(url);
    },
};
export default apiCart;
