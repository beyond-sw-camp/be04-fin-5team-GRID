import {createRouter, createWebHistory} from 'vue-router';


const router = createRouter({

    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('../views/Login.vue')
        },
        {
            path: '/2',
            component: () => import('../views/Login2.vue')
        }

    ]
})

export default router;