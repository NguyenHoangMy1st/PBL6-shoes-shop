import axiosClient from './axiosClient';

const apiOrderByID = {
    getOrderByID(id) {
        const url = `/order/${id}`;
        return axiosClient.get(url);
    },
};
export default apiOrderByID;
