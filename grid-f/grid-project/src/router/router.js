import {createRouter, createWebHistory} from 'vue-router';


const router = createRouter({

    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('../views/Login.vue')
        },
        {
            path: '/department',
            component: () => import('@/components/Department/Department.vue')
        },
        {
            path: '/hr',
            component: () => import('@/views/HumanResources/Main.vue')
        }

    ]
})

export default router;