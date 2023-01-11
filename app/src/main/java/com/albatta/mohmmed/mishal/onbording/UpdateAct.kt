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
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import kotlinx.android.synthetic.main.activity_update.btn_cancel
import kotlinx.android.synthetic.main.activity_update.btn_up
import kotlinx.android.synthetic.main.activity_update.et_up_answer
import kotlinx.android.synthetic.main.activity_update.et_up_hint
import kotlinx.android.synthetic.main.activity_update.et_up_option1
import kotlinx.android.synthetic.main.activity_update.et_up_option2
import kotlinx.android.synthetic.main.activity_update.et_up_option3
import kotlinx.android.synthetic.main.activity_update.et_up_q
import kotlinx.android.synthetic.main.activity_update.et_up_time
import kotlinx.android.synthetic.main.activity_update.image_view_alert
import kotlinx.android.synthetic.main.activity_update.resetImage
import java.lang.Exception
import kotlin.properties.Delegates

class UpdateAct : AppCompatActivity() {
    var flag=0
    var position by Delegates.notNull<Int>()
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                btn_up.setTextColor(Color.BLACK)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                btn_up.setTextColor(Color.WHITE)

            }
        }

        val intent=intent?.extras
        val id=intent?.getInt("id")

        for (i in db.getPublicQuestion().indices){
            if (db.getPublicQuestion()[i].id==id){
                position=i
            }
        }

       et_up_q.setText(db.getPublicQuestion()[position].question)
        et_up_answer.setText(db.getPublicQuestion()[position].answer)
        et_up_option1.setText(db.getPublicQuestion()[position].option1)
       et_up_option2.setText(db.getPublicQuestion()[position].option2)
        et_up_option3.setText(db.getPublicQuestion()[position].option3)
        et_up_hint.setText(db.getPublicQuestion()[position].hintOfAnswer)
        et_up_time.setText((db.getPublicQuestion()[position].time/1000).toString())
        if (db.getPublicQuestion()[position].image!=null){
            image_view_alert.setImageBitmap(Utils.getImage(db.getPublicQuestion()[position].image!!))
            resetImage.visibility=View.VISIBLE
        }
        resetImage.setOnClickListener {
            image_view_alert.setImageResource(R.drawable.ic_action_name)
         val result=db.updatePublicQuestionImage(db.getPublicQuestion()[position].id.toString(),null)
            if (result){
                Toast.makeText(this, "تم إعادة تعيين الصورة", Toast.LENGTH_SHORT).show()
            }
            resetImage.visibility=View.GONE
        }
        image_view_alert.setOnClickListener {
            val photoPicker=Intent(Intent.ACTION_PICK)
            photoPicker.type ="image/*"
            startActivityForResult(photoPicker,3333)

        }



       btn_cancel.setOnClickListener {
           startActivity(Intent(this,Delete::class.java))
       }
        btn_up.setOnClickListener {
            try {



            if (et_up_q.text.toString().isNotEmpty() && et_up_answer.text.toString()
                    .isNotEmpty() && et_up_option1.text.toString()
                    .isNotEmpty() && et_up_option2.text.toString()
                    .isNotEmpty() && et_up_option3.text.toString()
                    .isNotEmpty() && et_up_time.text.toString().isNotEmpty()
                    && et_up_hint.text.toString().isNotEmpty()
            ) {
                val question = et_up_q.text.toString().trim()
                val answer = et_up_answer.text.toString().trim()
                val option1 = et_up_option1.text.toString().trim()
                val option2 = et_up_option2.text.toString().trim()
                val option3 = et_up_option3.text.toString().trim()
                val hint = et_up_hint.text.toString().trim()
                val time = et_up_time.text.toString().toInt()
                val converter = (time * 1000).toLong()
                if (flag == 1) {
                    val bitmap = (image_view_alert.drawable as BitmapDrawable).toBitmap()
                    flag = 0
                    val result=  db.updatePublicQuestion(db.getPublicQuestion()[position].id.toString(),question,answer,option1,option2,option3,converter,hint,Utils.getBytes(bitmap))
                    if (result){
                        Toast.makeText(this, "تم التعديل ", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val result=  db.updatePublicQuestion(db.getPublicQuestion()[position].id.toString(),question,answer,option1,option2,option3,converter,hint,db.getPublicQuestion()[position].image)
                    if (result){
                        Toast.makeText(this, "تم التعديل ", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, "يرجى تعبئة جميع الحقول", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()}
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)
        if (resultcode == Activity.RESULT_OK) {
            if (requestcode == 3333&&data !=null) {
                val pickedImage=data.data
                image_view_alert.setImageURI(pickedImage)
                resetImage.visibility=View.VISIBLE
                flag=1
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            finishAffinity()
            startActivity(Intent(this,Delete::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }

}