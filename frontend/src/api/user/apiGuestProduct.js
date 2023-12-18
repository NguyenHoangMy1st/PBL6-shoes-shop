import axiosClient from '../../components/API/axiosClient';
import axiosClientGuest from '../../components/API/axiosClientGuest';

const apiGuestProduct = {
    getAllProduct() {
        const url = '/guest/products/';
        return axiosClientGuest.get(url);
    },
};
export default apiGuestProduct;
