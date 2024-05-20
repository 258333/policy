//定义store
import { defineStore } from 'pinia'
import { ref } from 'vue'

/**
 * 第一个参数：名字，唯一性 
 * 第二个参数：函数，函数的内部可以定义状态的所有内容
 * 
 * 返回值：函数
 */
export const usePolicyStore = defineStore('policy', () => {

    //1.响应式变量
    const policy = ref({})
    //2.定义一个函数，修改good的值
    const setPolicy = (newPolicy) => {
        policy.value = newPolicy
    }
    //3.函数，移除good的值
    const removePolicy = () => {
        policy.value = ''
    }
    return { policy, setPolicy, removePolicy}
}, {
    persist: true       //持久化存储
});