package com.sportsbaazi.bootstrap

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Manish Kumar on 18/05/24.
 */
@Suppress("DEPRECATION")
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instances = this
    }

    companion object {
        lateinit var instances: BaseApplication

        fun isNetworkConnected(): Boolean {
            val connectivityManager =
                instances.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}