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
        },
        {
            path: '/department',
            component: () => import('@/components/Department/Department.vue')
        },        
        {
            path: '/addteamreview',
            component: () => import('@/components/TeamReview/AddTeamReview.vue')
        },
        {
          path: '/team',
          component: () => import('@/components/Department/Team.vue')
      }

    ]
})

export default router;