package com.albatta.mohmmed.mishal.onbording

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
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.countOfhelps_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.count_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.index_of_listOfPrivateQuestion
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.listOfchoisen_Private
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import kotlinx.android.synthetic.main.activity_admin.btn_add
import kotlinx.android.synthetic.main.activity_admin.image_view_Q
import kotlinx.android.synthetic.main.activity_make_private.btn_Private_add
import kotlinx.android.synthetic.main.activity_make_private.btn_Private_show
import kotlinx.android.synthetic.main.activity_make_private.et_PrivateAnswer
import kotlinx.android.synthetic.main.activity_make_private.et_PrivateOption1
import kotlinx.android.synthetic.main.activity_make_private.et_PrivateOption2
import kotlinx.android.synthetic.main.activity_make_private.et_PrivateOption3
import kotlinx.android.synthetic.main.activity_make_private.et_PrivateqQestion
import kotlinx.android.synthetic.main.activity_make_private.et_et_PrivatTime
import kotlinx.android.synthetic.main.activity_make_private.et_et_PrivateHint
import kotlinx.android.synthetic.main.activity_make_private.image_view_question_Private

class MakePrivate : AppCompatActivity() {
    var flag=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_private)
        try {
            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    btn_Private_add.setTextColor(Color.BLACK)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    btn_Private_add.setTextColor(Color.WHITE)
                }
            }

            var byteArray: ByteArray? = null
            btn_Private_show.setOnClickListener {
                startActivity(Intent(this, ShowPrivateQuestions::class.java))
            }
            image_view_question_Private.setOnClickListener {
                val photoPicker = Intent(Intent.ACTION_PICK)
                photoPicker.type = "image/*"
                startActivityForResult(photoPicker, 6666)
            }
            btn_Private_add.setOnClickListener {
                               if (et_PrivateqQestion.text.toString()
                            .isNotEmpty() && et_PrivateAnswer.text.toString()
                            .isNotEmpty() && et_PrivateOption1.text.toString()
                            .isNotEmpty() && et_PrivateOption2.text.toString()
                            .isNotEmpty() && et_PrivateOption3.text.toString()
                            .isNotEmpty() && et_et_PrivatTime.text.toString()
                            .isNotEmpty() && et_et_PrivateHint.text.toString().isNotEmpty()) {

                        val question = et_PrivateqQestion.text.toString().trim()
                        val answer = et_PrivateAnswer.text.toString().trim()
                        val option1 = et_PrivateOption1.text.toString().trim()
                        val option2 = et_PrivateOption2.text.toString().trim()
                        val option3 = et_PrivateOption3.text.toString().trim()
                        val hint = et_et_PrivateHint.text.toString().trim()
                        val time = et_et_PrivatTime.text.toString().toInt()
                        val converter = time * 1000

                        if (flag == 1) {
                            flag = 0
                            val bitmap = (image_view_question_Private.drawable as BitmapDrawable).toBitmap()
                            byteArray = Utils.getBytes(bitmap)
                        }
                        val result = db.addPrivateQuestion(
                            question,
                            answer,
                            option1,
                            option2,
                            option3,
                            converter.toLong(),
                            hint,
                            db.getUser()[index_of_User].id,
                            byteArray
                        )
                        if (result) {
                            Toast.makeText(this, "تمت الإضافة", Toast.LENGTH_SHORT).show()
                            et_PrivateqQestion.text?.clear()
                            et_PrivateAnswer.text?.clear()
                            et_PrivateOption1.text?.clear()
                            et_PrivateOption2.text?.clear()
                            et_PrivateOption3.text?.clear()
                            et_et_PrivateHint.text?.clear()
                            et_et_PrivatTime.text?.clear()
                            image_view_question_Private.setImageResource(R.drawable.ic_action_name)
                            byteArray = null
                        } else {
                            Toast.makeText(this, "حدث خلل أثناء الإضافة", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, "يجب تعبئة جميع الخيارات", Toast.LENGTH_SHORT).show()
                    }


            }

        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)
        if (resultcode == Activity.RESULT_OK) {
            if (requestcode == 6666&&data !=null) {
                val pickedImage=data.data
                image_view_question_Private.setImageURI(pickedImage)
                flag =1
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
        index_of_listOfPrivateQuestion = 0
        count_Private = 0
        countOfhelps_Private = 3
        listOfchoisen_Private.clear()

    }


}