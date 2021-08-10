package com.example.waybillmanagement

import android.app.Activity

object ActivityCollector {
    private val activities = ArrayList<Activity>()

    //添加新activity
    fun addActivity(activity: Activity){
        activities.add(activity)
    }

    //清除activity
    fun removeActivity(activity: Activity){
        activities.remove(activity)
    }

    //结束所有的activity
    fun finishAll(){
        for (activity in activities){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}