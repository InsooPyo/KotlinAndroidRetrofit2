package com.pyoinsoo.retrofit.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v4.util.Pair
import android.widget.Toast
import com.pyoinsoo.retrofit.kotlin.common.WeatherUtil
import com.pyoinsoo.retrofit.kotlin.jsondata.CurrentWeather
import com.pyoinsoo.retrofit.kotlin.jsondata.Minutely
import com.pyoinsoo.retrofit.kotlin.restful.OkHttp3RetrofitManager
import com.pyoinsoo.retrofit.kotlin.restful.RetrofitInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

/*
 * MainActivity 화면
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         * 현재 날짜정보를 TextView에 출력한다
         */
        tvTodayDate.text = WeatherUtil.currentDate()
        tvTodayAmPm.text = WeatherUtil.amPm()
        tvTodayHhMm.text = WeatherUtil.minuteSecond()

    }
    /*
     * 요청을 보낼 질의문자열의 값을 정의
     */
    private val latitude = "37.572978"
    private val logitude = "126.989061"
    private val apiVersion = "1"

    override fun onResume() {
        super.onResume()

        /*
         * 요청을 보낼 객체를 리턴 받는다
         */
        val restClient: RetrofitInterface =
                OkHttp3RetrofitManager.getRetrofitService(RetrofitInterface::class.java)

        /*
         * 현재 날씨 정보를 비동기 방식으로 요청
         * Retrofit2을 사용하면 별도의 Thread(AsyncTask)를 만들필요없이
         * 비동기 방식으로 동작하도록 구성할 수 있다
         */
        val currentWeather = restClient.requestCurrentWeather(latitude, logitude, apiVersion)
        currentWeather.enqueue(object : Callback<CurrentWeather> {
            override fun onResponse(call: Call<CurrentWeather>?,
                                    response: Response<CurrentWeather>?) {
                if(response != null && response.isSuccessful)
                    setUICurrentWeather(response.body())
            }
            override fun onFailure(call: Call<CurrentWeather>?, t: Throwable?) {
                errorMessage(message = t.toString())
            }
        })
        /*
         * 현재 미세먼지 정보를 비동기 방식으로 요청
         */
        val finDustCall = restClient.requestFineDust(latitude, logitude, apiVersion)
        finDustCall.enqueue(object : Callback<CurrentWeather> {
            override fun onResponse(call: Call<CurrentWeather>?,
                                    response: Response<CurrentWeather>?) {
                if(response != null && response.isSuccessful)
                    setUIDust(response.body())
            }
            override fun onFailure(call: Call<CurrentWeather>?, t: Throwable?) {
                errorMessage(message = t.toString())
            }
        })
        /*
         * 현재 자외선 정보를 비동기방식으로 요청
         */
        val uvlayCall = restClient.requestUvRays(latitude, logitude, apiVersion)
        uvlayCall.enqueue(object : Callback<CurrentWeather> {
            override fun onResponse(call: Call<CurrentWeather>?,
                                    response: Response<CurrentWeather>?) {
                if(response != null && response.isSuccessful)
                    setUIUvindex(response.body())
            }
            override fun onFailure(call: Call<CurrentWeather>?, t: Throwable?) {
                errorMessage(message = t.toString())
            }
        })
    }

    /*
     * JSON을 파싱한 데이터를 넘겨 받는다
     */
    private fun setUICurrentWeather(data: CurrentWeather?) {

        val minutely: Minutely? = data?.weather?.minutely?.get(0)

        /*
         * 현재날씨및 오늘의 최저/최고 온도를 가져온다
         */
        minutely?.let {
            tvTodayCurrentTemperature.text = it.temperature.tc.toDouble().roundToInt().toString()
            maxTv.text = it.temperature.tmax.toDouble().roundToInt().toString()
            minTv.text = it.temperature.tmin.toDouble().roundToInt().toString()

            /*
             * 현재 하늘상태(sky code)맞는 Icon으로
             * Background Image및 Weather Icon 을 세팅한다
             */
            val pair: Pair<Int,Int> = WeatherUtil.currentABGIconCondition(it.sky.code)
            mainRootview.setBackgroundResource(pair.first!!)
            ivCurrentWeatherIcon.setImageResource(pair.second!!)
        }
    }
    /*
     * 미세먼지 원형 바에 표현
     */
    private fun setUIDust(data: CurrentWeather?) {

        val dustValue = data?.weather?.dust?.get(0)!!.pm10.value
        val pair = WeatherUtil.getDustMessage(dustValue.trim())

        dustCircular.progressValue = pair.first!!.toFloat()

        tvDustGrade.text = pair.second
    }
   /*
    * 자외선 지수를 원형 바에 표현
    */
    private fun setUIUvindex(data: CurrentWeather?) {
        var uvValue = data?.weather?.wIndex?.uvindex!!.get(0).day00.index
       /*
        * 18시 이후엔 json값이 empty가 된다
        */
        if(uvValue.isNullOrEmpty()){
            uvValue = "0"
        }
        val pair = WeatherUtil.getUvrayMessage(uvValue)

        uvCircular.progressValue = pair.first!!.toFloat()
        tvUvStateMessage.text = pair.second
    }
    private fun errorMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}
