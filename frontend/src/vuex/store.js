import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);
// 登录验证
export default new Vuex.Store({
    state: {
        user: null,
        token: null,
        projectId: null,
        permissions: []
    },
    mutations: {
        // 登录
        set_user(state, user) {
            state.user = user;
        },
        // 退出
        del_user(state) {
            state.user = null;
            localStorage.removeItem("user");
        },
        set_token(state, token) {
            state.token = token;
        },
        del_token(state) {
            state.token = null;
            localStorage.removeItem('token');
        },
        set_projectId(state, projectId) {
            state.projectId = projectId;
        },
        set_permissions(state, permissions) {
            console.log(permissions);
            state.permissions = permissions;
        },
    }
})