import axios from 'axios';

const apiRegister = {
    postRegister(data) {
        const url = 'http://localhost:5454/auth/signup';
        return axios.post(url, data);
    },
};
export default apiRegister;
