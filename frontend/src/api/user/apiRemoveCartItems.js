import axiosClient from '../../components/API/axiosClient';

const apiRemoveCartItems = {
    delRemoveCartItems(id) {
        const url = `/cart_items/${id}`;
        return axiosClient.delete(url);
    },
};
export default apiRemoveCartItems;
