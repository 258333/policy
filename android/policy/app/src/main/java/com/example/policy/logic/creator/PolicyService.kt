package com.example.policy.logic.creator

import com.example.policy.logic.model.Results
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PolicyService {
    @POST("policy/android")
    @FormUrlEncoded
    fun policyListService(
        @Field("checkList") checkList: List<String>,
        @Field("input") input: String
    ): Call<Results<List<Map<String, Any>>>>

    @GET("policy")
    fun typeListService(): Call<Results<List<Map<String, Any>>>>
}