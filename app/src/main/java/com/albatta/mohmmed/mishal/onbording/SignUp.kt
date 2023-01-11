package com.albatta.mohmmed.mishal.onbording

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.outside
import kotlinx.android.synthetic.main.activity_sign_up.UserNameContainer
import kotlinx.android.synthetic.main.activity_sign_up.btn_SignUp
import kotlinx.android.synthetic.main.activity_sign_up.container1
import kotlinx.android.synthetic.main.activity_sign_up.container2
import kotlinx.android.synthetic.main.activity_sign_up.et_UserNameSignUp
import kotlinx.android.synthetic.main.activity_sign_up.et_confirm_Password
import kotlinx.android.synthetic.main.activity_sign_up.et_passwordSignUp
import kotlin.properties.Delegates


class SignUp : AppCompatActivity(){


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
      var flag=false
        textWatcher()
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                container1.counterTextColor=ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.BLACK, Color.WHITE))
                container2.counterTextColor=ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.BLACK, Color.WHITE))

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                container1.counterTextColor=ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled),   intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.WHITE, Color.BLACK))
                container2.counterTextColor=ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.WHITE, Color.BLACK))
            }
        }


        btn_SignUp.setOnClickListener {

                val userNameSignUp = et_UserNameSignUp.text.toString().trim()
                val passwordSignUp = et_passwordSignUp.text.toString().trim()
                val confirmPassword = et_confirm_Password.text.toString().trim()
                if (userNameSignUp.isNotEmpty() && passwordSignUp.isNotEmpty() && confirmPassword.isNotEmpty()) {
                            if (isEmailValid(userNameSignUp) == true) {
                                UserNameContainer.helperText = ""
                                if (validPassword()) {
                                    container1.helperText = ""
                                    if (confirmPassword == passwordSignUp) {
                                        for (i in db.getUser().indices){
                                            if (db.getUser()[i].Email==userNameSignUp){
                                                flag=true
                                            }
                                        }
                                        if (!flag) {
                                            val result = db.addUser(userNameSignUp, passwordSignUp, outside, null)
                                            if (result) {
                                                Toast.makeText(this, "تم نجاح إشتراكك", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this, Login::class.java))
                                                finishAffinity()
                                            } else Toast.makeText(this, "حدث خلل أثناء الإشتراك", Toast.LENGTH_SHORT).show()
                                        }else{ Toast.makeText(this, "هذا الحساب موجود مسبقا", Toast.LENGTH_SHORT).show() }
                                    } else { Toast.makeText(this, "تأكد من مطابقة كلمة السر", Toast.LENGTH_SHORT).show() }
                                } else {validPassword()}
                            } else {UserNameContainer.helperText = "يجب أن يحتوي على gmail.com@"}
                } else{ Toast.makeText(this, "يرجى التأكد من ملئ الحقول", Toast.LENGTH_SHORT).show()}
            }
    }
    private fun textWatcher() {
        val textWatcherpassword = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validPassword()

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        et_passwordSignUp.addTextChangedListener(textWatcherpassword)
        val textWatcherEmail = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isEmailValid(p0) == false) {
                    UserNameContainer.helperText = "يجب أن يحتوي على gmail.com@"
                }else   UserNameContainer.helperText=""
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        et_UserNameSignUp.addTextChangedListener(textWatcherEmail)
    }



    fun isEmailValid(email: CharSequence?): Boolean? {
        return email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }


    private fun validPassword(): Boolean {
       val passwordText=et_passwordSignUp.text.toString()
        if(passwordText.length != 10) {
            container1.helperText="يجب أن يحتوي على عشرة أحرف"
            return false
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            container1.helperText="يجب أن يحتوي على حرف واحد على الأقل من الأحرف الكبيرة [A-Z]"
            return false
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            container1.helperText="يجب أن يحتوي على حرف واحد على الأقل من الأحرف الصغيرة [a-z]"
            return false
        }
        if(!passwordText.matches(".*[@/#$%^&+=!].*".toRegex())) {
            container1.helperText="يجب أن يحتوي على علامة واحدة على الأقل من [@/#\$%^&+=]"
            return false
        }
        if(!passwordText.matches(".*[0-9].*".toRegex())) {
            container1.helperText="يجب أن يحتوي على رقم واحد على الأقل [0-9]"
            return false
        }
        container1.helperText=""
        return true
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){

            finishAffinity()
            startActivity(Intent(this,Login::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }


}