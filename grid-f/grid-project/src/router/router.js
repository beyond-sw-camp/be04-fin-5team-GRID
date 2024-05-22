import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/', 
            component:() => import('../views/MainView.vue')
        },
        {
            path: '/vacation/policy',
            component:() => import('../views/VacationPolicyView.vue')

        },
        {
            path: '/vacation/policy/modify',
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
            path: '/vacation/manage/delete',
            component:() => import('../views/VacationManageDeleteView.vue')
        },
        {
            path: '/vacation/history',
            component:() => import('../views/VacationHistoryMainView.vue')
        }
    ]
})

export default router;