package com.pyoinsoo.retrofit.kotlin.restful

import com.pyoinsoo.retrofit.kotlin.R
import com.pyoinsoo.retrofit.kotlin.common.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/*
 * Created by pyoinsoo on 2018-01-11.
 * insoo.pyo@gmail.com
 *
 * Okhttp3와 rerofit2를 설정하는 코드
 */
object OkHttp3RetrofitManager {

    private val ALL_TIMEOUT = 10L
    private val SK_API_KEY = MyApplication.myApplication.
             resources.getString(R.string.sk_weather_api_key)
    private val SK_WEATHER_HOST = "http://apis.skplanetx.com/"

    private var okHttpClient: OkHttpClient
    private var retrofit: Retrofit

    init{
        /*
         * 로깅 인터셉터 연결
         */
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.level = HttpLoggingInterceptor.Level.BASIC


        okHttpClient = OkHttpClient().newBuilder().apply {

            addInterceptor(httpLogging)
            addInterceptor(HeaderSettingInterceptor())
            connectTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)

        }.build()
        /*
         * Retrofit2 + OKHttp3를 연결한다
         */
        retrofit = Retrofit.Builder().apply{
            baseUrl(SK_WEATHER_HOST)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())//gson을 이용해 json파싱
        }.build()
    }
    /*
     *  Request Header를 세팅하는 Interceptor
     */
    private  class HeaderSettingInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val chainRequest = chain.request()

            val request = chainRequest.newBuilder().apply{
                addHeader("Accept", "application/json")
                addHeader("appKey", SK_API_KEY)
            }.build()

            return chain.proceed(request)
        }
    }
    /*
     * 현재 선언된 요청 인터페이스 객체(RetrofitInterface)를 리턴한다
     */
    internal fun <T> getRetrofitService(restClass: Class<T>): T {
        return retrofit.create(restClass)
    }
}