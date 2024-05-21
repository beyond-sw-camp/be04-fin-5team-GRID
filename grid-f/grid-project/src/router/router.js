import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/', 
            component:() => import('../components/Main/Main.vue')
        },
        {
            path: '/policy',
            component:() => import('../components/Vacation/Policy/VacationPolicy.vue')

        },
        {
            path: '/policy/modify',
            component:() => import('../components/Vacation/Policy/VacationPolicyModify.vue')
        }
    ]
})

export default router;