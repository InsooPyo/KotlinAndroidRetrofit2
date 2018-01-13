package com.pyoinsoo.retrofit.kotlin

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.pyoinsoo.retrofit.kotlin.common.MyApplication

class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private var networkInfo: NetworkInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkInfo = manager.activeNetworkInfo
    }

    override fun onStart() {
        super.onStart()

        handler.postDelayed({

            val networkStatus: Boolean = networkInfo?.isConnectedOrConnecting ?: false
            if (networkStatus) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
              Toast.makeText(MyApplication.myApplication,
                             "네트웍이 연결되지않아 종료합니다",Toast.LENGTH_SHORT).show()
              finish()
            }
        }, 1000)
    }
}
