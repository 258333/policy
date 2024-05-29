package com.example.policy.logic.creator

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

////请求拦截器
//class AuthInterceptor(private val context: Context) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//        val token = runBlocking {
//            DataStoreUtil.getToken()
//        }
//        val request = chain.request()
//        val newRequest = request.newBuilder().apply {
//            if (token.isNotEmpty()) {
//                addHeader("Authorization", token)
//            }
//        }.build()
//        return chain.proceed(newRequest)
//    }
//}

object ServiceCreator {
    private const val BASE_URL = "http://10.0.2.2:8080/"

//    private val okHttpClient = okhttp3.OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(LearnRecordApplication.context))
//        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
//        .client(okHttpClient)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}