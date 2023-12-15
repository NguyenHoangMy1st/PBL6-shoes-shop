import axiosClient from '../../components/API/axiosClient';

const apiAddProduct = {
    postAddProduct(data) {
        console.log(data);
        const url = '/admin/products/';
        return axiosClient.post(url, data);
    },
};
export default apiAddProduct;
