package com.albatta.mohmmed.mishal.onbording

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.inside
import kotlinx.android.synthetic.main.login.btn_login
import kotlinx.android.synthetic.main.login.et_Email
import kotlinx.android.synthetic.main.login.et_password
import kotlinx.android.synthetic.main.login.textField
import kotlinx.android.synthetic.main.login.tv_signUp
import kotlin.properties.Delegates
import kotlin.system.exitProcess


class Login : AppCompatActivity() {

    var num by Delegates.notNull<Int>()
    @SuppressLint("SuspiciousIndentation", "UnlocalizedSms")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val textWatcherEmail = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isEmailValid(p0) == false) {
                    textField.helperText = "يجب أن يحتوي على gmail.com@"
                    btn_login.isEnabled=false
                }else{
                    textField.helperText=""
                    btn_login.isEnabled=true
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        }
        et_Email.addTextChangedListener(textWatcherEmail)
        btn_login.setOnClickListener {
            var flag=false
        val email=et_Email.text.toString().trim()
        val passWord=et_password.text.toString().trim()
            if (email.isNotEmpty()&&passWord.isNotEmpty()) {
                if (email!="Admin123@gmail.com"||passWord!="123") {
                    if (isEmailValid(email) == true) {
                        textField.helperText = ""
                        if (db.getUser().isNotEmpty()) {
                            for (i in db.getUser().indices) {
                                if (db.getUser()[i].Email == email && db.getUser()[i].password == passWord) {
                                    flag=true
                                    num=i
                                }
                            }
                               if (flag){
                                    db.updateUserinout(db.getUser()[num].id.toString(), inside)
                                    index_of_User=num
                                    Toast.makeText(this, "تم تسجيل الدخول", Toast.LENGTH_SHORT).show()

                                    finishAffinity()
                                    startActivity(Intent(this, MainActivity::class.java))
                                } else {Toast.makeText(this, "لم يتم العثور على الحساب ", Toast.LENGTH_SHORT).show()}
                        }else{Toast.makeText(this, "لم يتم العثور على الحساب ", Toast.LENGTH_SHORT).show()}
                    } else{ textField.helperText = "يجب أن يحتوي على gmail.com@"}
                }else{
                    startActivity(Intent(this,Admin::class.java))
                }
            }else Toast.makeText(this, "يرجى التأكد من ملئ الحقول", Toast.LENGTH_SHORT).show()
        }
        tv_signUp.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }
    }
    fun isEmailValid(email: CharSequence?): Boolean? {
        return email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            finishAffinity()
            exitProcess(0)
        }else{
            super.onKeyDown(keyCode, event) }

    }

}