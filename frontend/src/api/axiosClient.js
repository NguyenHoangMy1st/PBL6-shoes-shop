import axios from 'axios';

const axiosClient = axios.create({
    baseURL: 'http://localhost:5454',
});

// Add a request interceptor
axiosClient.interceptors.request.use(
    function (config) {
        const access_token = sessionStorage.getItem('jwt');
        if (access_token) {
            config.headers.Authorization = `Bearer ${access_token}`;
        }
        // Do something before request is sent
        return config;
    },
    function (error) {
        // Do something with request error
        return Promise.reject(error);
    },
);

// Add a response interceptor
axiosClient.interceptors.response.use(
    function (response) {
        // Any status code that lie within the range of 2xx cause this function to trigger
        // Do something with response data
        return response;
    },
    function (error) {
        // Any status codes that falls outside the range of 2xx cause this function to trigger
        // Do something with response error
        return Promise.reject(error);
    },
);

export default axiosClient;
