package com.example.login.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//MainActivity
class LoginViewModel(nameReserved: String, passReserved: String): ViewModel() {

    var name = MutableLiveData<String>()    //账号，可变的LiveData
    var pass = MutableLiveData<String>()    //密码
    var switch: Boolean = false
    var curname: String = ""    //当前已登录用户的账户名
    var curpass: String = ""    //当前已登录用户的密码
    var cursex: String = ""    //当前已登录用户的性别，用于显示头像
    var curdepart: String = ""  // 当前已登录用户的发货地

    init {
        name.value = nameReserved   //  这里有语法糖
        pass.value = passReserved
    }

}