import {createRouter, createWebHistory} from 'vue-router';


const router = createRouter({

    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('../views/Login/Login.vue')
        },
        {
            path: '/department',
            component: () => import('@/components/Department/Department.vue')
        },
        {
            path: '/team',
            component: () => import('@/components/Department/Team.vue')
        },
        {
            path: '/review-list',
            component: () => import('@/components/TeamReview/ReviewList.vue')
        },
        {
            path: '/review-history',
            component: () => import('@/components/TeamReview/ReviewHistory.vue')
        }
    ]
})

export default router;