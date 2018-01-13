package com.pyoinsoo.retrofit.kotlin.restful

import com.pyoinsoo.retrofit.kotlin.jsondata.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Created by pyoinsoo on 2018-01-11.
 * insoo.pyo@gmail.com
 * 보낼 요청을 구성한다
 */
interface RetrofitInterface {

    /*
     * 현재 날씨를 가져온다(json data)
     */
    @GET("/weather/current/minutely")
    fun  requestCurrentWeather(
                           @Query("lat")     lat:String, //위도
                           @Query("lon")     lon:String, //경도
                           @Query("version") version:String) : Call<CurrentWeather>
    /*
     * 현재 미세먼지 수치를 가져온다(json data)
     */
    @GET("/weather/dust")
    fun  requestFineDust(
            @Query("lat")     lat:String, //위도
            @Query("lon")     lon:String, //경도
            @Query("version") version:String) : Call<CurrentWeather>
    /*
     * 현재 자외선 지수를 가져온다(json data)
     */
    @GET("/weather/windex/uvindex")
    fun  requestUvRays(
            @Query("lat")     lat:String, //위도
            @Query("lon")     lon:String, //경도
            @Query("version") version:String) : Call<CurrentWeather>
}