import { createRouter, createWebHistory } from 'vue-router'

import PolicyVue from '@/components/Policy.vue'
import Policy1Vue from '@/components/Policy1.vue'
import Policy2Vue from '@/components/Policy2.vue'
import SetVue from '@/components/Set.vue'
const routes = [
    {
        path: '/',
        component: PolicyVue,
        redirect: '/policy1',
        children: [
            { path: '/policy1', component: Policy1Vue },
            // { path: '/policy2', component: Policy2Vue },
            { path: '/set', component: SetVue },
        ]
    },
    { path: '/policy2', component: Policy2Vue }
]
//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由
export default router 