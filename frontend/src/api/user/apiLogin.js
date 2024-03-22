import axios from 'axios';

const apiLogin = {
    postLogin(data) {
        const url = 'https://pbl6-shoes-shop-production-28ef.up.railway.app/auth/signin';
        return axios.post(url, data);
    },
};
export default apiLogin;
