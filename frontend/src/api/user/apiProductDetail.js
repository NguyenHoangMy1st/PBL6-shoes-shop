import axiosClient from '../../components/API/axiosClient';

const apiProductDetail = {
    getProductDetail(id) {
        const url = `/products/id/${id}`;
        return axiosClient.get(url);
    },
};
export default apiProductDetail;
