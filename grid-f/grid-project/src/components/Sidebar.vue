<template>
    <div class="card flex justify-content-center">
        <PanelMenu :model="items" class="w-full md:w-20rem">
            <template #item="{ item }">
                <a v-ripple class="flex align-items-center px-3 py-2 cursor-pointer">
                    <span :class="[item.icon, 'text-primary']" />
                    <span :class="['ml-2', { 'font-semibold': item.items }]">{{ item.label }}</span>
                    <Badge v-if="item.badge" class="ml-auto" :value="item.badge" />
                    <span v-if="item.shortcut" class="ml-auto border-1 surface-border border-round surface-100 text-xs p-1">{{ item.shortcut }}</span>
                </a>
            </template>
        </PanelMenu>
    </div>

</template>

<script setup>
import { ref } from "vue";

const items = ref([
    {
        label: 'Mail',
        icon: 'pi pi-envelope',
        badge: 5,
        items: [
            {
                label: 'Compose',
                icon: 'pi pi-file-edit',
                shortcut: '⌘+N'
            },
            {
                label: 'Inbox',
                icon: 'pi pi-inbox',
                badge: 5
            },
            {
                label: 'Sent',
                icon: 'pi pi-send',
                shortcut: '⌘+S'
            },
            {
                label: 'Trash',
                icon: 'pi pi-trash',
                shortcut: '⌘+T'
            }
        ]
    },
    {
        label: 'Reports',
        icon: 'pi pi-chart-bar',
        shortcut: '⌘+R',
        items: [
            {
                label: 'Sales',
                icon: 'pi pi-chart-line',
                badge: 3
            },
            {
                label: 'Products',
                icon: 'pi pi-list',
                badge: 6
            }
        ]
    },
    {
        label: 'Profile',
        icon: 'pi pi-user',
        shortcut: '⌘+W',
        items: [
            {
                label: 'Settings',
                icon: 'pi pi-cog',
                shortcut: '⌘+O'
            },
            {
                label: 'Privacy',
                icon: 'pi pi-shield',
                shortcut: '⌘+P'
            }
        ]
    }
]);


// import { ref } from 'vue';
// import { useRouter } from 'vue-router';
import PanelMenu from 'primevue/panelmenu';
import Badge from 'primevue/badge';

// const router = useRouter();

// const employee = ref({
//   name: '홍길동',
//   email: 'hong@gildong.com'
// });

// const items = ref([
//   {
//     label: '근태 관리',
//     icon: 'pi pi-fw pi-clock',
//     items: [
//       { label: '근무 관리', icon: 'pi pi-fw pi-calendar' },
//       { label: '근무 정보', icon: 'pi pi-fw pi-info' },
//       { label: '휴가 정책', icon: 'pi pi-fw pi-briefcase' },
//       { label: '휴가 보유 정보 목록', icon: 'pi pi-fw pi-list' },
//       { label: '휴가 기록 목록', icon: 'pi pi-fw pi-file' }
//     ]
//   },
//   {
//     label: '결재 관리',
//     icon: 'pi pi-fw pi-dollar',
//     items: [
//       { label: '결재 문서 작성', icon: 'pi pi-fw pi-pencil' },
//       { label: '결재 문서 목록', icon: 'pi pi-fw pi-file' }
//     ]
//   },
//   {
//     label: '인사 관리',
//     icon: 'pi pi-fw pi-users',
//     items: [
//       { label: '인사 정보', icon: 'pi pi-fw pi-user', command: () => moveHr() }
//     ]
//   },
//   {
//     label: '부서 관리',
//     icon: 'pi pi-fw pi-sitemap',
//     items: [
//       { label: '부서 정보', icon: 'pi pi-fw pi-info' }
//     ]
//   },
//   {
//     label: '부서 평가',
//     icon: 'pi pi-fw pi-star',
//     items: [
//       { label: '본인 평가 목록', icon: 'pi pi-fw pi-list' },
//       { label: '동료 평가 작성', icon: 'pi pi-fw pi-pencil' }
//     ]
//   }
// ]);

// const moveHr = () => {
//   router.push('/hr/main');
// };

// import axios from 'axios';
// import { useRouter } from 'vue-router';
// import { ref, onMounted, reactive } from 'vue';


// const employee = ref([]);
// const error = ref([]);
// const router = useRouter();

// const fetchEmployee = async () => {
//   try {
//     const response = await axios.get(`http://localhost:8080/users/240201`); // ${employeeNumber}로 수정예정
//     employee.value = response.data.result;
//   } catch (err) {
//     console.error('Error fetching employee:', err);
//     error.value = 'Failed to fetch employee data.';
//   }
// };

// onMounted(() => {
//   fetchEmployee();
// });


// const activeMenus = reactive({
//   workManagement: false,
//   paymentManagement: false,
//   hrManagement: false,
//   departmentManagement: false,
//   departmentEvaluation: false,
// });

// const toggleMenu = (menu) => {
//   activeMenus[menu] = !activeMenus[menu];
// };

// function moveHr() {
//   router.push('/hr/main');
// }

</script>

<style scoped>
@font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.sidebar {
  width: 250px;
  background-color: #f8f9fa;
  padding: 1rem;
}

.profile {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.profile-pic {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 1rem;
}

.profile-info {
  display: flex;
  flex-direction: column;
}

.profile-info h3 {
  margin: 0;
  font-size: 1.2rem;
}

.profile-info p {
  margin: 0;
  font-size: 0.9rem;
  color: #6c757d;
}

.menu {
  margin-top: 1rem;
}

.menu ul {
  list-style-type: none;
  padding: 0;
}

.menu li {
  margin-bottom: 1rem;
}

/* 드롭다운 메뉴 아이템의 패딩을 제거합니다 */
.p-panelmenu-panel .p-panelmenu-header {
  padding-left: 0 !important;
}

.p-menuitem {
  padding-left: 0 !important;
}
</style>
