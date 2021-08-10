package com.example.waybillmanagement.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.login.Dao.UserDao
import com.example.login.Dao.WaybillDao
import com.example.login.Database.AppDatabase
import com.example.login.ViewModel.LoginViewHolderFactory
import com.example.login.ViewModel.LoginViewModel
import com.example.waybillmanagement.ActivityCollector
import kotlinx.android.synthetic.main.activity_login.*
import android.os.Handler
import androidx.core.widget.addTextChangedListener
import com.example.waybillmanagement.logic.model.UserEnity
import com.example.waybillmanagement.WaybillApplication
import com.example.waybillmanagement.logic.model.Waybill
import kotlin.concurrent.thread

class LoginActivity: BaseActivity() {

    lateinit var viewModel: LoginViewModel
    lateinit var sp: SharedPreferences
    lateinit var userDao: UserDao
    lateinit var waybillDao: WaybillDao

    val openUserPage = 1
    val mempassSuccess = 2
    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message){
            when(msg.what){
                openUserPage -> {
                    usernameEdit.text.clear()
                    passwordEdit.text.clear()
                    viewModel.curname = viewModel.name.value.toString()
                    viewModel.curpass = viewModel.pass.value.toString()
                    val intent = Intent(WaybillApplication.context, UserActivity::class.java)
                    intent.putExtra("curname",viewModel.curname)
                    intent.putExtra("curpass",viewModel.curpass)
                    intent.putExtra("cursex",viewModel.cursex)
                    intent.putExtra("curdepart",viewModel.curdepart)
                    startActivity(intent)
                }
                mempassSuccess -> {
                    passwordEdit.setText(viewModel.curpass)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        // 取出保存的用户名和密码
        sp = getPreferences(Context.MODE_PRIVATE)
        var nameReserved = sp.getString("nameReserved","")
        var passReserved = sp.getString("passReserved","")
        viewModel = ViewModelProvider(this, LoginViewHolderFactory(nameReserved!!,passReserved!!)).get(LoginViewModel::class.java)  //创建viewModel实例
        userDao = AppDatabase.getDatabase(this).userDao()   //数据库操纵对象
        waybillDao = AppDatabase.getDatabase(this).waybillDao()

        // 初始化，向数据库加入运单
        thread{

            if(waybillDao.loadAllWaybill().isEmpty()){
                waybillDao.insertWaybill(Waybill("X2000021","邓伦","12345678901",
                        "迪丽热巴","12345670987","沈阳","南京",
                        "翻斗大街123号","生日贺卡","5","0","0"))
                waybillDao.insertWaybill(Waybill("X2000022","迪丽热巴","12345670987",
                        "杨敏","12387459822","沈阳","北京",
                        "好漂亮小区","新疆馕饼","30","0","25"))
                waybillDao.insertWaybill(Waybill("X2000023","张国","13343356743",
                        "叶美丽","18763678887","沈阳","武汉",
                        "汉南区下李家台","文件","1","0","23"))
                waybillDao.insertWaybill(Waybill("X2000024","陆修","17672876886",
                        "郝晴","16562762289","沈阳","大连",
                        "甘井子区","衣物","1","18","0"))
                waybillDao.insertWaybill(Waybill("X2000025","乃万","17798786754",
                        "Vava","17652782765","沈阳","安徽",
                        "不知名地方","吉他","1","0","100"))
                waybillDao.insertWaybill(Waybill("X2000026","陈瀚","19827838700",
                        "窦婧函","17789762898","沈阳","潍坊",
                        "市区1县区1什么村","背包","1","50","0"))
                waybillDao.insertWaybill(Waybill("X2000027","王思学","13452345677",
                        "黄哲","18978978934","沈阳","无锡",
                        "","辣椒酱","12","0","80"))
                waybillDao.insertWaybill(Waybill("X2000028","赵韩金","18966546754",
                        "王思学","13452345677","沈阳","金华",
                        "有爱合作社","衣物","1","23","0"))
                waybillDao.insertWaybill(Waybill("X2000029","高美","12345322345",
                        "张爱玲","18736802387","沈阳","厦门",
                        "友谊校区4栋12楼","信件","1","0","8"))
                waybillDao.insertWaybill(Waybill("X2000030","斯如希","16787039734",
                        "金辰澈","15644543453","沈阳","新疆",
                        "团结路185号","家具","3","0","350"))
                waybillDao.insertWaybill(Waybill("X2000031","龚俊","18789787888",
                        "张哲瀚","16726862976","沈阳","商丘",
                        "中念大街","衣物","3","0","20"))
                waybillDao.insertWaybill(Waybill("X2000032","张哲瀚","16726862976",
                        "龚俊", "18789787888","沈阳","资阳",
                        "凌云峰","书籍","3","0","30"))
                waybillDao.insertWaybill(Waybill("X2000033","窦婧函","17789762898",
                        "金辰澈", "15644543453","沈阳","驻马店",
                        "赵家村1号","食品","1","23","0"))
                waybillDao.insertWaybill(Waybill("X2000034","周树人","18782987899",
                        "苏辙", "15678938908","沈阳","双流",
                        "双流国际机场","护肤品","3","0","92"))
                waybillDao.insertWaybill(Waybill("X2000035","金辰澈","15644543453",
                        "成寒", "16289762897","沈阳","丹东",
                        "单家井村134号","鞋子","2","19","0"))

            }

            Log.d("database","database initialization")

        }

        // 针对切换用户所做的页面修改
       viewModel.switch = intent.getBooleanExtra("switchboolean",false)
        if (viewModel.switch){
            viewModel.curname = intent.getStringExtra("curname").toString()
            viewModel.curpass = intent.getStringExtra("curpass").toString()

            usernameEdit.setHint("我是 ${viewModel.curname}")
            passwordEdit.setHint("当前密码是 ${viewModel.curpass}")
            loginButton.setText("确认切换")
            exitButton.setText("返回")
            supportActionBar?.setTitle("切换用户")
        }

        // 用户登录
        loginButton.setOnClickListener{
            viewModel.name.value = usernameEdit.text.toString()
            viewModel.pass.value = passwordEdit.text.toString()

            Log.d("usernameEdit", "username:" + viewModel.name.value )
            Log.d("passwordEdit", "password:" + viewModel.pass.value)

            if (viewModel.name.value.equals("")){
                Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show()
            }
            else if (viewModel.pass.value.equals("")){
                Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show()
            }
            else{
                thread{
                    val result = userDao.queryByNameandPass(viewModel.name.value.toString(),viewModel.pass.value.toString())
                    if(result!=null){
                        if (mempassCheck.isChecked ==  true){
                            var tempUser = UserEnity(result.user_name,result.user_pass,result.user_depart,
                                    result.user_tel,result.user_sex,'y')
                            tempUser.user_ID = result.user_ID
                            userDao.updateUser(tempUser)
                        }
                        else{
                            var tempUser = UserEnity(result.user_name,result.user_pass,result.user_depart,
                                    result.user_tel,result.user_sex,'n')
                            tempUser.user_ID = result.user_ID
                            userDao.updateUser(tempUser)
                        }
                        viewModel.cursex = result.user_sex
                        viewModel.curdepart = result.user_depart
                        val msg = Message()
                        msg.what = openUserPage
                        handler.sendMessage(msg)
                    }
                }

            }
        }

        // 切换密码明文显示
        showpassCheck.setOnCheckedChangeListener {showpassCheck,_->
            if (showpassCheck.isChecked){
                passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                passwordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else{
                passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
                passwordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //记住密码
        usernameEdit.addTextChangedListener {
            if (mempassCheck.isChecked){
                thread {
                    val input = usernameEdit.text.toString()
                    val result = userDao.queryByName(input)
                    if (result!=null && result.user_memps == 'y'){ //该用户先前确实记住密码了
                        viewModel.curpass = result.user_pass
                        viewModel.cursex = result.user_sex
                        viewModel.curdepart = result.user_depart
                        val msg = Message()
                        msg.what = mempassSuccess
                        handler.sendMessage(msg)
                    }
                }
            }
        }

        // 退出程序
        exitButton.setOnClickListener {
            if (viewModel.switch){
                this.finish()
            }
            else {
                ActivityCollector.finishAll()
            }

        }

    }

    //关闭程序时保存输入的账号和密码
    override fun onPause() {
        super.onPause()
        sp.edit {
            putString("name_reserved",viewModel.name.toString())
            putString("pass_reserved",viewModel.pass.toString())
        }
    }

    //加载菜单布局
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    //菜单项的监听事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawyerlayout.openDrawer(GravityCompat.START) //打开滑动菜单
            R.id.userswitch -> {    //切换用户
                if (viewModel.switch == true){
                    Toast.makeText(this, "当前已在切换用户页面", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.userregister -> {
                val intent = Intent(this,LogupActivity::class.java)
                intent.putExtra("curname",viewModel.curname)
                intent.putExtra("curpass",viewModel.curpass)
                startActivity(intent)
            }
            R.id.usersignout -> Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}