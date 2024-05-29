import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state: {
        user: null,
        email: localStorage.getItem('email') || '' // 로컬 스토리지에서 이메일 읽기
    },
    mutations: {
        setUser(state, user) {
            state.user = user;
        },
        setEmail(state, email) {
            state.email = email;
            localStorage.setItem('email', email); // 이메일을 로컬 스토리지에 저장
        },
        resetState(state) {
            state.user = null;
            state.email = '';
            localStorage.removeItem('email');
        }
    },
    actions: {
        async fetchUserByEmail({ commit }, email) {
            try {
                const response = await axios.get(`http://localhost:8080/users/mail/${email}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('access')}`
                    }
                });
                commit('setUser', response.data.result);
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
