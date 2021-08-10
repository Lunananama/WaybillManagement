package com.example.waybillmanagement.logic.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//用户类
@Entity(tableName = "t_user")
@Parcelize
class UserEnity(
        @ColumnInfo(name="user_name")
        var user_name: String ="",

        @ColumnInfo(name = "user_pass")
        var user_pass: String ="",

        @ColumnInfo(name = "user_depart")
        var user_depart: String ="",

        @ColumnInfo(name = "user_tel")
        var user_tel: String ="",

        @ColumnInfo(name = "user_sex")
        var user_sex: String ="",

        @ColumnInfo(name = "user_memps")
        var user_memps: Char = 'n'): Parcelable{

    @PrimaryKey(autoGenerate = true)
    var user_ID: Long = 0
}
