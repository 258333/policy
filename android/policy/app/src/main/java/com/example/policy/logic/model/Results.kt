package com.example.policy.logic.model

data class Results<T>(
    val code: Int,
    //业务状态码 0-成功 1-失败
    val message: String,
    // 提示信息
    val data: T
    //响应数据
)