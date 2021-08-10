package com.example.waybillmanagement.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waybillmanagement.ActivityCollector

//Activity栈的基类
open class BaseActivity: AppCompatActivity() {

    //创建了一个Activity
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    //关闭了一个Activity
    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}