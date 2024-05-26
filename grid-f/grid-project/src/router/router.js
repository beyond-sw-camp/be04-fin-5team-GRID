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
        // {
        //     path: '/2',
        //     component: () => import('../views/Login2.vue')
        // },
        {
            path: '/department',
            component: () => import('@/components/Department/Department.vue')
        },
        {
            path: '/bt',
            component: () => import('@/views/Approval/BTApprovalListView.vue')
        },
        {
            path: '/overtime',
            component: () => import('@/views/Approval/OApprovalListView.vue')
        },
        {
            path: '/vacation',
            component: () => import('@/views/Approval/VApprovalListView.vue')
        },
        {
            path: '/approval',
            component: () => import('@/views/Approval/AllApprovalListView.vue')
        },
        {
            path: '/approval-detail/:typeId/:approvalId',
            component: () => import('@/views/Approval/ApprovalDetailView.vue')
        }
    ]
})

export default router;