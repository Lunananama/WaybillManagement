package com.example.waybillmanagement.ui

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.waybillmanagement.WaybillApplication
import com.example.waybillmanagement.logic.model.Waybill
import com.example.waybillmanagement.ui.ViewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.nav_header.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

class UserActivity : BaseActivity() {

    val viewModel = UserViewModel()

    val timeClick = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(usertoolbar)

        //获取上一个界面传来的用户名和密码
        viewModel.curname = intent.getStringExtra("curname").toString() //用户名
        viewModel.curpass = intent.getStringExtra("curpass").toString() //密码
        viewModel.cursex = intent.getStringExtra("cursex").toString()   //性别
        viewModel.curdepart = intent.getStringExtra("curdepart").toString() //发货地

        Log.d("user",viewModel.curname)
        Log.d("user",viewModel.curpass)
        Log.d("user",viewModel.cursex)
        Log.d("user",viewModel.curdepart)

        thread {
            try {
                while (true){
                    Thread.sleep(1000)
                    runOnUiThread{
                        getCurrentTime()
                    }
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }

        if (viewModel.cursex.equals("男")){
            userImage.setImageResource(R.drawable.ic_maleuser)
        }
        else{
            userImage.setImageResource(R.drawable.ic_femaleuser)
        }
        userdepartText.setText("您的发货地位于: ${viewModel.curdepart}")

        // 设置滑动菜单栏导航按钮
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)          //让导航按钮显示出来
            it.setHomeAsUpIndicator(R.drawable.ic_menu) //设置导航栏图标
            it.setTitle("用户中心")
        }

        // 滑动菜单项监听器
        usernavView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.navAddOrder -> {   //添加运单
                    Toast.makeText(this,"addorder",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddOrderActivity::class.java)
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    intent.putExtra("modify",false)
                    startActivity(intent)
                    Log.d("LoginActivity","addorder is clicked")
                }

                R.id.navJSONOrder -> {  //查询JSON运单
                    Toast.makeText(this,"jsonorder",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","json")
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("LoginActivity","JSONorder is clicked")
                }

                R.id.navXMLOrder -> {   //查询XML运单
                    Toast.makeText(this,"xmlorder",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","xml")
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("LoginActivity","XMLorder is clicked")
                }

                R.id.navLOCOrder -> {   //查询本地运单
                    Toast.makeText(this,"locorder",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ShowOrderActivity::class.java)
                    intent.putExtra("type","loc")
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                    Log.d("LoginActivity","LOCOrder is clicked")
                }
                R.id.navSetting -> {    //设置键

                }
            }

            userdrawyerlayout.closeDrawers()    //关闭侧边菜单栏
            true
        }

    }

    //加载菜单布局
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (viewModel.cursex.equals("男")){
            headeruserImage.setImageResource(R.drawable.ic_maleuser)
        }
        else{
            headeruserImage.setImageResource(R.drawable.ic_femaleuser)
        }
        headerusernameText.setText("欢迎使用，${viewModel.curname}")
        headeruserdepartText.setText(viewModel.curdepart)

        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    //菜单项的监听事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> userdrawyerlayout.openDrawer(GravityCompat.START) //打开滑动菜单
            R.id.userswitch -> {    //切换用户
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("switchboolean",true)
                intent.putExtra("curname",viewModel.curname)
                intent.putExtra("curpass",viewModel.curpass)
                startActivity(intent)
            }
            R.id.userregister -> {  //用户注册
                val intent = Intent(this, LogupActivity::class.java)
                intent.putExtra("curname",viewModel.curname)
                intent.putExtra("curpass",viewModel.curpass)
                startActivity(intent)
            }
            R.id.usersignout -> {   //退出登录
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("curname","")
                intent.putExtra("curpass","")
                intent.putExtra("switchboolean",false)
                startActivity(intent)
            }
        }
        return true
    }

    //获得当前时间
    fun getCurrentTime():String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS")
        val formatted = current.format(formatter)

        if (current.hour < 6){
            userwelcomeText.setText("努力工作的 ${viewModel.curname} 最棒啦！")
        }
        else if (current.hour < 9){
            userwelcomeText.setText("早上好， ${viewModel.curname}")
        }
        else if (current.hour < 12){
            userwelcomeText.setText("上午好， ${viewModel.curname}")
        }
        else if (current.hour < 19){
            userwelcomeText.setText("下午好， ${viewModel.curname}")
        }
        else if (current.hour < 23){
            userwelcomeText.setText("晚上好， ${viewModel.curname}")
        }
        else{
            userwelcomeText.setText("夜深了 ${viewModel.curname}，早点休息~")
        }
        usertimeText.setText(formatted.toString())

        return formatted.toString()
    }

}