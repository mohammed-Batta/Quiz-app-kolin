package com.albatta.mohmmed.mishal.onbording

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.inside
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.outside
import kotlinx.android.synthetic.main.activity_account.UserNameContainer33
import kotlinx.android.synthetic.main.activity_account.btn_Delete_Account
import kotlinx.android.synthetic.main.activity_account.btn_logOut
import kotlinx.android.synthetic.main.activity_account.btn_up_Account
import kotlinx.android.synthetic.main.activity_account.container5
import kotlinx.android.synthetic.main.activity_account.et_AccEmail
import kotlinx.android.synthetic.main.activity_account.et_AccPassword
import kotlinx.android.synthetic.main.activity_account.image_account
import kotlinx.android.synthetic.main.activity_account.resetImage_Account
import kotlinx.android.synthetic.main.activity_sign_up.UserNameContainer
import kotlinx.android.synthetic.main.activity_sign_up.container1
import kotlinx.android.synthetic.main.activity_sign_up.et_UserNameSignUp
import kotlinx.android.synthetic.main.activity_sign_up.et_confirm_Password
import kotlinx.android.synthetic.main.activity_sign_up.et_passwordSignUp
import kotlinx.android.synthetic.main.activity_update.btn_up
import kotlinx.android.synthetic.main.activity_update.image_view_alert
import kotlinx.android.synthetic.main.activity_update.resetImage
import kotlin.properties.Delegates

class Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        try {

            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    container5.counterTextColor = ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_enabled),
                            intArrayOf(android.R.attr.state_enabled)
                        ), intArrayOf(Color.BLACK, Color.WHITE)
                    )
                    btn_up_Account.setTextColor(Color.BLACK)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    btn_up_Account.setTextColor(Color.WHITE)
                    container5.counterTextColor = ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_enabled),
                            intArrayOf(android.R.attr.state_enabled)
                        ), intArrayOf(Color.WHITE, Color.BLACK)
                    )

                }
            }
            textWatcher()
            et_AccEmail.setText(db.getUser()[index_of_User].Email)
            et_AccPassword.setText(db.getUser()[index_of_User].password)
            if (db.getUser()[index_of_User].image != null) {
                image_account.setImageBitmap(Utils.getImage(db.getUser()[index_of_User].image!!))
                resetImage_Account.visibility = View.VISIBLE
            }
            resetImage_Account.setOnClickListener {
                image_account.setImageResource(R.drawable.sod)
                val result = db.updateUserImage(db.getUser()[index_of_User].id.toString(), null)
                if (result) {
                    Toast.makeText(this, "تم إعادة تعيين الصورة", Toast.LENGTH_SHORT).show()
                }
                resetImage_Account.visibility = View.GONE
            }
            image_account.setOnClickListener {
                val photoPicker = Intent(Intent.ACTION_PICK)
                photoPicker.type = "image/*"
                startActivityForResult(photoPicker, 5555)
            }

            btn_up_Account.setOnClickListener {
                try {
                    if (et_AccEmail.text.toString().isNotEmpty() && et_AccPassword.text.toString().isNotEmpty()) {
                        val email = et_AccEmail.text.toString().trim()
                        val password = et_AccPassword.text.toString().trim()
                        if (isEmailValid(email) == true) {
                            UserNameContainer33.helperText = ""
                            if (validPassword()) {
                                container5.helperText = ""
                                    val result = db.updateUser(db.getUser()[index_of_User].id.toString(), email, password, inside)
                                    if (result) {
                                        Toast.makeText(this, "تم الحفظ", Toast.LENGTH_SHORT).show()
                                    } else Toast.makeText(this, "حدث خلل أثناء الحفظ", Toast.LENGTH_SHORT).show()

                            } else {
                                validPassword()
                            }
                        } else {
                            UserNameContainer33.helperText = "يجب أن يحتوي على gmail.com@"
                        }
                    } else {
                        Toast.makeText(this, "يرجى التأكد من ملئ الحقول", Toast.LENGTH_SHORT).show()
                    }

                }catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
            }
            btn_logOut.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("تسجيل الخروج")
                alertDialog.setMessage("هل أنت متأكد من تسجيل الخروج من الحساب")
                alertDialog.setPositiveButton("نعم") { _, _ ->
                    val result =
                        db.updateUserinout(db.getUser()[index_of_User].id.toString(), outside)
                    if (result) {
                        Toast.makeText(this, "تم تسجيل خروجك من الحساب", Toast.LENGTH_SHORT).show()
                        finishAffinity()
                        startActivity(Intent(this, Login::class.java))
                    }
                }
                alertDialog.setNegativeButton("إلغاء", null)
                alertDialog.show()
            }
            btn_Delete_Account.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("حذف الحساب")
                alertDialog.setMessage("هل أنت متأكد من حذف الحساب بشكل نهائي؟")
                alertDialog.setPositiveButton("نعم") { _, _ ->
                    val result = db.deleteUser(db.getUser()[index_of_User].id)
                    if (result) {
                        Toast.makeText(this, "تم حذف حسابك بشكل نهائي", Toast.LENGTH_SHORT).show()
                        finishAffinity()
                        startActivity(Intent(this, Login::class.java))
                    }

                }
                alertDialog.setNegativeButton("إلغاء", null)
                alertDialog.show()

            }
        }catch (e:Exception){ Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()} }

    fun isEmailValid(email: CharSequence?): Boolean? {
        return email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }


    private fun validPassword(): Boolean {
        val passwordText=et_AccPassword.text.toString()
        if(passwordText.length != 10) {
            container5.helperText="يجب أن يحتوي على عشرة أحرف"
            return false
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            container5.helperText="يجب أن يحتوي على حرف واحد على الأقل من الأحرف الكبيرة [A-Z]"
            return false
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            container5.helperText="يجب أن يحتوي على حرف واحد على الأقل من الأحرف الصغيرة [a-z]"
            return false
        }
        if(!passwordText.matches(".*[@/#$%^&+=!].*".toRegex())) {
            container5.helperText="يجب أن يحتوي على علامة واحدة على الأقل من [@/#\$%^&+=]"
            return false
        }
        if(!passwordText.matches(".*[0-9].*".toRegex())) {
            container5.helperText="يجب أن يحتوي على رقم واحد على الأقل [0-9]"
            return false
        }
        container5.helperText=""
        return true
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
        et_AccPassword.addTextChangedListener(textWatcherpassword)
        val textWatcherEmail = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isEmailValid(p0) == false) {
                    UserNameContainer33.helperText = "يجب أن يحتوي على gmail.com@"
                }else   UserNameContainer33.helperText=""
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        et_AccEmail.addTextChangedListener(textWatcherEmail)
    }



    @RequiresApi(Build.VERSION_CODES.N)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)
        if (resultcode == Activity.RESULT_OK) {
            if (requestcode == 5555&&data !=null) {
                val pickedImage=data.data
                image_account.setImageURI(pickedImage)
                val bitmap=(image_account.drawable as BitmapDrawable).toBitmap()
                db.updateUserImage(db.getUser()[index_of_User].id.toString(),Utils.getBytes(bitmap))
                resetImage_Account.visibility=View.VISIBLE
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            reset()
            finishAffinity()
            startActivity(Intent(this,MainActivity::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }
    private fun reset(){
        MainActivity.index_of_listOfPrivateQuestion = 0
        MainActivity.count_Private = 0
        MainActivity.countOfhelps_Private = 3
        MainActivity.listOfchoisen_Private.clear()
    }



}