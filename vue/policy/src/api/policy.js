//导入request.js请求工具
import request from '@/utils/request.js'

//查询所有政策
export const policyListService = (params) => {
    return request.post('/policy', params);
}

//查询所有政策类型
export const typeListService = () => {
    return request.get('/policy');
}

//二次检索
export const policyListSecondService = (second) => {
    return request.get('/policy/' + second)
}