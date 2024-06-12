import { createRouter, createWebHistory } from 'vue-router';

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
            component: () => import('@/components/Department/Department.vue'),
            props: true
        },
        {
            path: '/team/:id',
            component: () => import('@/components/Department/Team.vue'),
            props: true
        },
        {
            path: '/performance-review/goal/add',
            component: () => import('../views/PerformanceReview/Goal/GoalAddView.vue')
        },
        {
            path: '/performance-review/goal',
            component: () => import('../views/PerformanceReview/Goal/GoalListView.vue')
        },
        {
            path: '/performance-review/goal/detail/:id',
            component: () => import('../views/PerformanceReview/Goal/GoalDetailView.vue')
        },
        {
            path: '/ad-time',
            component: () => import('../views/AdTime/AdTimeListView.vue')
        },
        {
            path: '/ad-time/add',
            component: () => import('../views/AdTime/AdTimeAddView.vue')
        },
        {
            path: '/hr',
            component: () => import('../views/HumanResources/Main.vue')
        },
        {
            path: '/hr/profile/:employeeNumber',
            component: () => import('../views/HumanResources/Profile.vue')
        },
        {
            path: '/hr/modify/list',
            component: () => import('../views/HumanResources/ModifyMulti.vue')
        },
        {
            path: '/hr/modify/:employeeNumber',
            name: 'Modify',
            component: () => import('../views/HumanResources/Modify.vue')
        },
        {
            path: '/hr/add/list',
            component: () => import('../views/HumanResources/AddMulti.vue')
        },
        {
            path: '/approval',
            component: () => import('@/views/Approval/AllApprovalListView.vue')
        },
        {
            path: '/approval/detail/:typeId/:approvalId',
            component: () => import('@/views/Approval/ApprovalDetailView.vue')
        },
        {
            path: '/vacation/policy',
            component: () => import('../views/Vacation/VacationPolicyView.vue')
        },
        {
            path: '/vacation/policy/modify/:id',
            component: () => import('../views/Vacation/VacationPolicyModifyView.vue')
        },
        {
            path: '/vacation/policy/regist',
            component: () => import('../views/Vacation/VacationPolicyRegistView.vue')
        },
        {
            path: '/vacation/manage',
            component: () => import('../views/Vacation/VacationManageMainView.vue')
        },
        {
            path: '/vacation/manage/regist',
            component: () => import('../views/Vacation/VacationManageRegistView.vue')
        },
        {
            path: '/vacation/manage/modify/:id',
            component: () => import('../views/Vacation/VacationManageDeleteView.vue')
        },
        {
            path: '/vacation/history',
            component: () => import('../views/Vacation/VacationHistoryMainView.vue')
        },
        {
            path: '/vacation/changeInfo',
            component: () => import('../views/Vacation/VacationChangeInfoMainView.vue')
        },
        {
            path: '/vacation/info',
            component: () => import('../views/Vacation/VacationInfoMainView.vue')
        },
        {
            path: '/vacation/userInfo',
            component:() => import('../views/Vacation/VacationInfoUserView.vue')
        },
        {
            path: '/performance-review',
            component: () => import('../views/PerformanceReview/Review/PerformanceReviewListView.vue')
        },
        {
            path: '/performance-review/detail/:id',
            component: () => import('../views/PerformanceReview/Review/PerformanceReviewDetailView.vue')
        },
        {
            path: '/performance-review/mid',
            component: () => import('../views/PerformanceReview/Review/MidPerformanceReviewAddView.vue')
        },
        {
            path: '/performance-review/final',
            component: () => import('../views/PerformanceReview/Review/FinalPerformanceReviewAddView.vue')
        },
        {
            path: '/performance-review/total',
            component: () => import('../views/PerformanceReview/Total/TotalPerformanceReviewListView.vue')
        },
        {
            path: '/performance-review/total/detail/:id',
            component: () => import('../views/PerformanceReview/Total/TotalPerformanceReviewDetailView.vue')
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
            path: '/rw',
            component: () => import('@/views/Approval/RWApprovalListView.vue')
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
            path: '/approval/detail/:typeId/:approvalId',
            component: () => import('@/views/Approval/ApprovalDetailView.vue')
        },
        {
            path: '/required',
            component: () => import('@/views/Approval/RequiredApprovalListView.vue')
        },
        {
            path: '/my',
            component: () => import('@/views/Approval/MyApprovalListView.vue')
        },
        {
            path: '/regist/main',
            component: () => import('@/views/Approval/RegistMainView.vue')
        },
        {
            path: '/regist/bt',
            component: () => import('@/views/Approval/RegistBTApprovalView.vue')
        },
        {
            path: '/regist/overtime',
            component: () => import('@/views/Approval/RegistOApprovalView.vue')
        },
        {
            path: '/regist/rw',
            component: () => import('@/views/Approval/RegistRWApprovalView.vue')
        },
        {
            path: '/regist/vacation',
            component: () => import('@/views/Approval/RegistVApprovalView.vue')
        },
        {
            path: '/team-review/history',
            component: () => import('@/components/TeamReview/ReviewHistory.vue')
        },
        {
            path: '/team-review/add',
            component: () => import('@/components/TeamReview/AddTeamReview.vue')
        },
        {
            path: '/team-review/list/:id',
            component: () => import('@/components/TeamReview/ReviewList.vue')
        },
        {
            path: '/work-calendar/:id',
            component: () => import('@/views/AdTime/WorkCalendarView.vue')
        },
        {
            path: '/team/member-list/:teamId',
            component: () => import('@/components/Department/TeamMemberList.vue')
        },
        {
            path: '/team-review/myreview/:id',
            component: () => import('@/components/TeamReview/MyReview.vue')
        },
        {
            path: '/work',
            component: () => import('@/views/Approval/AllWorkListView.vue')
        },
        {
            path: '/work-calendar/:id',
            component: () => import('@/views/AdTime/WorkCalendarView.vue')
        },
        {
            path: '/work-calendar',
            component: () => import('@/views/AdTime/WorkCalendarView.vue')
        },
        {
            path: '/main',
            component: () => import('@/views/Main/Main.vue')
        }
    ]
});

router.beforeEach((to, from, next) => {
    const accessToken = localStorage.getItem('access');
    const refreshToken = document.cookie.split('; ').find(row => row.startsWith('refresh='));

    const publicPaths = [
        /^\/$/,
        /^\/find\/id$/,
        /^\/find\/pwd$/,
        /^\/find\/id\/result$/,
        /^\/find\/pwd\/[A-Za-z0-9+/=]+\/result$/
    ];

    const isPublicPath = publicPaths.some(regex => regex.test(to.path));

    if (isPublicPath) {
        next();
    } else if (!accessToken && !refreshToken) {
        next({ path: '/' });
    } else {
        next();
    }
});

export default router;
