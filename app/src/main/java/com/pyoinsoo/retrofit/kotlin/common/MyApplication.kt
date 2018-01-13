package com.pyoinsoo.retrofit.kotlin.common

import android.app.Application

/*
 * Created by pyoinsoo on 2017-01-10.
 * insoo.pyo@gmail.com
 */
class MyApplication : Application() {
    companion object {
        lateinit var myApplication : MyApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }
}