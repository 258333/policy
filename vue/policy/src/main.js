import './assets/main.scss'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'          //npm install element-plus
import 'element-plus/dist/index.css'
import router from '@/router/index.js'          //npm install vue-router
import App from './App.vue'
import { createPinia } from 'pinia'             //npm install pinia
import { createPersistedState } from 'pinia-persistedstate-plugin'  //持久化保存  npm install pinia-persistedstate-plugin
import locale from 'element-plus/dist/locale/zh-cn.js'
// import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
const pinia = createPinia();
const persist = createPersistedState();
pinia.use(persist)
// for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
//     app.component(key, component)
// }

app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale })
app.mount('#app')