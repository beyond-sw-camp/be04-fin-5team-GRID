// store/index.js
import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state: {
        user: null,
        email: ''
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
        setEmail(state, email) {
            state.email = email;
        },
        resetState(state) {
            state.user = null;
            state.email = '';
        }
    },
    actions: {
        async fetchUserByEmail({ commit }, email) {
            try {
                const response = await axios.get(`http://grid-backend-env.eba-p6dfcnta.ap-northeast-2.elasticbeanstalk.com/users/mail/${email}`);
                commit('setUser', response.data.result);
                commit('setEmail', email);
                console.log('유저 정보 fetch: ', response.data.result);
            } catch (error) {
                console.error('사용자 정보를 가져오는 데 실패했습니다:', error);
            }
        },
        updateEmail({ commit }, email) {
            commit('setEmail', email);
        },
        resetState({ commit }) {
            commit('resetState');
        }
    },
    getters: {
        user: state => state.user,
        email: state => state.email
    }
});