import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/', 
            component:() => import('../views/MainView.vue')
        },
        // {
        //     path: '/',
        //     component: () => import('../views/Login/Login.vue')
        // },
        {
            path: '/vacation/policy',
            component:() => import('../views/VacationPolicyView.vue')
        },
        {
            path: '/vacation/policy/modify/:id',
            component:() => import('../views/VacationPolicyModifyView.vue')
        },
        {
            path: '/vacation/policy/regist',
            component:() => import('../views/VacationPolicyRegistView.vue')
        },
        {
            path: '/vacation/manage',
            component:() => import('../views/VacationManageMainView.vue')
        },
        {
            path: '/vacation/manage/regist',
            component:() => import('../views/VacationManageRegistView.vue')
        },
        {
            path: '/vacation/manage/modify/:id',
            component:() => import('../views/VacationManageDeleteView.vue')
        },
        {
            path: '/vacation/history',
            component:() => import('../views/VacationHistoryMainView.vue')
        },
        {
            path: '/vacation/info',
            component:() => import('../views/VacationInfoMainView.vue')
        },
        
    ]
})

export default router;