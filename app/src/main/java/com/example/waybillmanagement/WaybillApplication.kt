package com.example.waybillmanagement

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


//是Applcation里的Context，全局只有一份，在整个应用程序的生命周期内都不会回收，不会内存泄漏
//全局获取Context
class WaybillApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }



}