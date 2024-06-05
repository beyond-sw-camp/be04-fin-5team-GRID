import axios from 'axios';

axios.defaults.baseURL = 'http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com';

// axios 인터셉터 설정 (필요한 경우)
axios.interceptors.request.use(config => {
    // 요청이 제대로 설정되었는지 확인
    console.log('Request URL:', config.url);
    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(response => {
    return response;
}, error => {
    return Promise.reject(error);
});