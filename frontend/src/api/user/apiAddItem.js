import axiosClient from '../../components/API/axiosClient';

const apiAddItem = {
    putAddItem(data) {
        const url = '/cart/add';
        return axiosClient.put(url, data);
    },
};
export default apiAddItem;
