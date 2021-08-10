package com.example.login.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


//向MainViewModel的构造函数传递参数
class LoginViewHolderFactory(private val nameReserved: String, private val passReserved: String): ViewModelProvider.Factory {

    //必须实现这个方法
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(nameReserved,passReserved) as T
    }
}