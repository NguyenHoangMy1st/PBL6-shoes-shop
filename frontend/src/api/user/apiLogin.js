import axios from 'axios';

const apiLogin = {
    postLogin(data) {
        const url = 'http://localhost:5454/auth/signin';
        return axios.post(url, data);
    },
};
export default apiLogin;
