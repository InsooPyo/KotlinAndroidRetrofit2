package com.pyoinsoo.retrofit.kotlin.jsondata

/*
 * Created by pyoinsoo on 2017-01-11.
 * insoo.pyo@gmail.com
 *
 * SK Weather 서버에서 가져온 JSON을 객체로 변환 할
 * 클래스 선언
 */

/*
 * 현재날씨(최저/최고 포함), 미세먼지, 자외선 지수를 나타내는 클래스
 */
data class CurrentWeather(val weather:Weather)

class Weather{
    //현재 날씨(오늘 최저/최고)
    var minutely:ArrayList<Minutely>? = null
    //미세먼지
    var dust : ArrayList<Dust>? = null
    //자외선
    var wIndex : WIndex? = null
}
//현재 날씨에 해당하는 JSON값을 객체화
//실제 날씨정보가 들어 있는 JSON값을 객체화
data class Minutely(
        val sky:Sky,
        val temperature:Temperature
)
data class Sky(val code:String, val name:String)
data class Temperature(val tc:String, val tmax:String, val tmin:String)

//미세먼지 데이터 값
data class Dust(val pm10:Pm10)
data class Pm10(val value:String)

//자외선 JSON값을 객체화
data class WIndex(val uvindex:ArrayList<UvIndex>)
data class UvIndex(val day00:Day00)
data class Day00(val index:String)