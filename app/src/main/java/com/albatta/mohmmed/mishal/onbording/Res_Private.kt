package com.albatta.mohmmed.mishal.onbording


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.countOfhelps_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.count_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.index_of_listOfPrivateQuestion
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.listOfchoisen_Private
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import kotlinx.android.synthetic.main.activity_res.btn_duplicate
import kotlinx.android.synthetic.main.activity_res.tv_Cor_Ans
import kotlinx.android.synthetic.main.activity_res.tv_UnCor_Ans
import kotlinx.android.synthetic.main.activity_res.tv_all
import kotlinx.android.synthetic.main.activity_res.tv_con
import kotlinx.android.synthetic.main.activity_res.tv_lead
import kotlinx.android.synthetic.main.activity_res_private.btn_duplicate_Private
import kotlinx.android.synthetic.main.activity_res_private.tv_Cor_Ans_Private
import kotlinx.android.synthetic.main.activity_res_private.tv_UnCor_Ans_Private
import kotlinx.android.synthetic.main.activity_res_private.tv_all_Private
import kotlinx.android.synthetic.main.activity_res_private.tv_con_Private
import kotlinx.android.synthetic.main.activity_res_private.tv_lead_Private

class Res_Private : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_private)
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                tv_con_Private.setTextColor(Color.WHITE)
                tv_lead_Private.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                tv_con_Private.setTextColor(Color.BLACK)
                tv_lead_Private.setTextColor(Color.BLACK)
            }
        }
        val size=db.getPrivateQuestion(db.getUser()[Splash.index_of_User].id).size
        if (count_Private == size){
            tv_con_Private.text="تهانينا لقد فزت ! 😁🤩🥳"

        }else if (Math.round(size/2.toDouble()) <= count_Private){
            tv_con_Private.text="تهانينا لقد فزت !\uD83D\uDE01\uD83E\uDD29\uD83E\uDD73"
        } else
            tv_con_Private.text="للأسف لقد خسرت ! 😥😕😔"
        tv_all_Private.text=size.toString()

        if (tv_con_Private.text.toString()!="للأسف لقد خسرت ! 😥😕😔"){
            btn_duplicate_Private.text="العودة إلى الشاشة الرئيسية"
        }
        tv_Cor_Ans_Private.text=count_Private.toString()
        tv_UnCor_Ans_Private.text=(size- count_Private).toString()
        btn_duplicate_Private.setOnClickListener {
            try {
                if (btn_duplicate_Private.text.toString() == "إعادة الاختبار") {
                    reset()
                    finishAffinity()
                    startActivity(Intent(this, PrivateQuestionScreen::class.java))
                } else {
                    reset()
                    startActivity(Intent(this@Res_Private,MainActivity::class.java))
                    finishAffinity()
                }
            }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            reset()
            finishAffinity()
            startActivity(Intent(this@Res_Private,MainActivity::class.java))
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