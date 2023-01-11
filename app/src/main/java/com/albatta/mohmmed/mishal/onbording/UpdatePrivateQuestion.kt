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
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import kotlinx.android.synthetic.main.activity_update.btn_cancel
import kotlinx.android.synthetic.main.activity_update.btn_up
import kotlinx.android.synthetic.main.activity_update.et_up_answer
import kotlinx.android.synthetic.main.activity_update.et_up_hint
import kotlinx.android.synthetic.main.activity_update.et_up_option1
import kotlinx.android.synthetic.main.activity_update.et_up_option2
import kotlinx.android.synthetic.main.activity_update.et_up_option3
import kotlinx.android.synthetic.main.activity_update.et_up_q
import kotlinx.android.synthetic.main.activity_update.et_up_time
import kotlinx.android.synthetic.main.activity_update_private_question.btn_cancel_Private
import kotlinx.android.synthetic.main.activity_update_private_question.btn_up_Private
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_PrivateAnswer
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_PrivateOption1
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_PrivateOption2
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_PrivateOption3
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_PrivateQ
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_Private_hint
import kotlinx.android.synthetic.main.activity_update_private_question.et_up_Private_time
import kotlinx.android.synthetic.main.activity_update_private_question.image_up_Private
import kotlinx.android.synthetic.main.activity_update_private_question.resetImage_up_Private
import kotlin.Exception
import kotlin.properties.Delegates

class UpdatePrivateQuestion : AppCompatActivity() {
    var flag=0
    var position by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_private_question)
        try {
            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    btn_up_Private.setTextColor(Color.BLACK)
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    btn_up_Private.setTextColor(Color.WHITE)

                }
            }

            val intent = intent?.extras
            val id = intent?.getInt("id_Private")
            for (i in db.getPrivateQuestion(db.getUser()[index_of_User].id).indices) {
                if (db.getPrivateQuestion(db.getUser()[index_of_User].id)[i].id == id) {
                    position = i
                }
            }

            et_up_PrivateQ.setText(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].question)
            et_up_PrivateAnswer.setText(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].answer)
            et_up_PrivateOption1.setText(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].option1)
            et_up_PrivateOption2.setText(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].option2)
            et_up_PrivateOption3.setText(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].option3)
            et_up_Private_hint.setText(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].hintOfAnswer)
            et_up_Private_time.setText((db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].time / 1000).toString())
            if (db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].image != null) {
                image_up_Private.setImageBitmap(Utils.getImage(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].image!!))
                resetImage_up_Private.visibility = View.VISIBLE
            }
            resetImage_up_Private.setOnClickListener {
                image_up_Private.setImageResource(R.drawable.ic_action_name)
                val result = db.updatePrivateQuestionImage(db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].id.toString(), null)
                if (result) {
                    Toast.makeText(this, "تم إعادة تعيين الصورة", Toast.LENGTH_SHORT).show()
                }
                resetImage_up_Private.visibility = View.GONE
            }
            image_up_Private.setOnClickListener {
                val photoPicker = Intent(Intent.ACTION_PICK)
                photoPicker.type = "image/*"
                startActivityForResult(photoPicker, 7777)

            }



            btn_cancel_Private.setOnClickListener {
                finishAffinity()
                startActivity(Intent(this, ShowPrivateQuestions::class.java))
            }
            btn_up_Private.setOnClickListener {
                try {


                    if (et_up_PrivateQ.text.toString()
                            .isNotEmpty() && et_up_PrivateAnswer.text.toString()
                            .isNotEmpty() && et_up_PrivateOption1.text.toString()
                            .isNotEmpty() && et_up_PrivateOption2.text.toString()
                            .isNotEmpty() && et_up_PrivateOption3.text.toString()
                            .isNotEmpty() && et_up_Private_time.text.toString().isNotEmpty()
                        && et_up_Private_hint.text.toString().isNotEmpty()
                    ) {
                        val question = et_up_PrivateQ.text.toString().trim()
                        val answer = et_up_PrivateAnswer.text.toString().trim()
                        val option1 = et_up_PrivateOption1.text.toString().trim()
                        val option2 = et_up_PrivateOption2.text.toString().trim()
                        val option3 = et_up_PrivateOption3.text.toString().trim()
                        val hint = et_up_Private_hint.text.toString().trim()
                        val time = et_up_Private_time.text.toString().toInt()
                        val converter = (time * 1000).toLong()
                        if (flag == 1) {
                            val bitmap = (image_up_Private.drawable as BitmapDrawable).toBitmap()
                            flag = 0
                            val result = db.updatePrivateQuestion(
                                db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].id.toString(),
                                question,
                                answer,
                                option1,
                                option2,
                                option3,
                                converter,
                                hint,
                                Utils.getBytes(bitmap)
                            )
                            if (result) {
                                Toast.makeText(this, "تم التعديل ", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val result = db.updatePrivateQuestion(
                                db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].id.toString(),
                                question,
                                answer,
                                option1,
                                option2,
                                option3,
                                converter,
                                hint,
                                db.getPrivateQuestion(db.getUser()[index_of_User].id)[position].image
                            )
                            if (result) {
                                Toast.makeText(this, "تم التعديل ", Toast.LENGTH_SHORT).show()
                            }
                        }

                    } else {
                        Toast.makeText(this, "يرجى تعبئة جميع الحقول", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)
        if (resultcode == Activity.RESULT_OK) {
            if (requestcode == 7777&&data !=null) {
                val pickedImage=data.data
                image_up_Private.setImageURI(pickedImage)
                resetImage_up_Private.visibility= View.VISIBLE
                flag=1
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            finishAffinity()
            startActivity(Intent(this,ShowPrivateQuestions::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }

}