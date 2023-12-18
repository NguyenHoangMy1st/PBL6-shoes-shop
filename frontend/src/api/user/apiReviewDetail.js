import axiosClient from '../../components/API/axiosClient';

const apiReviewDetail = {
    getReviewDetail(id) {
        const url = `/reviews/product/${id?.id}`;
        return axiosClient.get(url);
    },
};
export default apiReviewDetail;
