package com.albatta.mohmmed.mishal.onbording

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.flag
import kotlinx.android.synthetic.main.activity_admin.btn_add
import kotlinx.android.synthetic.main.activity_admin.btn_deletePage
import kotlinx.android.synthetic.main.activity_admin.et_Q
import kotlinx.android.synthetic.main.activity_admin.et_answer
import kotlinx.android.synthetic.main.activity_admin.et_hint
import kotlinx.android.synthetic.main.activity_admin.et_option1
import kotlinx.android.synthetic.main.activity_admin.et_option2
import kotlinx.android.synthetic.main.activity_admin.et_option3
import kotlinx.android.synthetic.main.activity_admin.et_time
import kotlinx.android.synthetic.main.activity_admin.image_view_Q

class Admin : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                btn_add.setTextColor(Color.BLACK)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                btn_add.setTextColor(Color.WHITE)

            }
        }

        var byteArray:ByteArray?=null
        btn_deletePage.setOnClickListener {
            startActivity(Intent(this,Delete::class.java))
        }
        image_view_Q.setOnClickListener {
            val photoPicker=Intent(Intent.ACTION_PICK)
            photoPicker.type ="image/*"
            startActivityForResult(photoPicker,2222)
        }
        btn_add.setOnClickListener {
                if ( et_Q.text.toString().isNotEmpty() && et_answer.text.toString().isNotEmpty() && et_option1.text.toString().isNotEmpty() && et_option2.text.toString().isNotEmpty() && et_option3.text.toString().isNotEmpty() && et_time.text.toString().isNotEmpty()&&et_hint.text.toString().isNotEmpty()) {


                    val question = et_Q.text.toString().trim()
                    val answer = et_answer.text.toString().trim()
                    val option1 = et_option1.text.toString().trim()
                    val option2 = et_option2.text.toString().trim()
                    val option3 = et_option3.text.toString().trim()
                    val hint = et_hint.text.toString().trim()
                    val time = et_time.text.toString().toInt()
                    val converter = time * 1000
                    if (flag==1) {
                        flag=0
                         val bitmap=(image_view_Q.drawable as BitmapDrawable).toBitmap()
                        byteArray=Utils.getBytes(bitmap)
                    }
                  val result=  db.addPublicQuestion(
                        question,
                        answer,

                        option1,
                        option2,
                        option3,
                        converter.toLong(),
                        hint,
                        byteArray)
                    if (result) {
                        Toast.makeText(this, "تمت الإضافة", Toast.LENGTH_SHORT).show()

                        et_Q.text?.clear()
                        et_answer.text?.clear()
                        et_option1.text?.clear()
                        et_option2.text?.clear()
                        et_option3.text?.clear()
                        et_hint.text?.clear()
                        et_time.text?.clear()
                        image_view_Q.setImageResource(R.drawable.ic_action_name)
                        byteArray=null
                    }else{
                        Toast.makeText(this, "حدث خلل أثناء الإضافة", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "يجب تعبئة جميع الخيارات", Toast.LENGTH_SHORT).show()
                }

        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)
        if (resultcode == Activity.RESULT_OK) {
            if (requestcode == 2222&&data !=null) {
                val pickedImage=data.data
                image_view_Q.setImageURI(pickedImage)
                flag=1
            }
        }
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