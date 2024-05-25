import {createRouter, createWebHistory} from 'vue-router';


const router = createRouter({

    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('../views/Login/Login.vue')
        },
        {

            path: '/find/id',
            component: () => import('../views/Login/FindId.vue')
        },
        {
            path: '/find/pwd',
            component: () => import('../views/Login/FindPwd.vue')
        },
        {
            path: '/find/id/result',
            component: () => import('../views/Login/FindIdResult.vue')
        },
        {
            path: '/find/pwd/:email/result',
            component: () => import('../views/Login/FindPwdResult.vue')
        },
        {
            path: '/department',
            component: () => import('@/components/Department/Department.vue')
        },
        {
            path: '/hr',
            component: () => import('../views/HumanResources/Main.vue')
        },
        {
            path: '/hr/profile/:id',
            component: () => import('../views/HumanResources/Profile.vue')
        },
        {
            path: '/hr/modify/list',
            component: () => import('../views/HumanResources/Modify.vue')
        },
        {
            path: '/hr/add/list',
            component: () => import('../views/HumanResources/AddMulti.vue')
        },
        {
            path: '/hr/add',
            component: () => import('../views/HumanResources/Add.vue')
        }
    ]
})

export default router;