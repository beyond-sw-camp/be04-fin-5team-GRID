import { createStore } from 'vuex';

const store = createStore({
    state() {
        return {
            email: ''
        };
    },
    mutations: {
        setEmail(state, email) {
            state.email = email;
        }
    },
    actions: {
        updateEmail({ commit }, email) {
            commit('setEmail', email);
        }
    }
});

export default store;
