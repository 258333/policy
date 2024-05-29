package com.example.policy.logic.creator

import com.example.policy.logic.model.Results
import com.example.policy.logic.model.Type
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface PolicyService {
    @POST("policy")
    fun policyListService(): Call<Results<Int>>

    @GET("policy")
    fun typeListService(): Call<Results<List<Type>>>
}