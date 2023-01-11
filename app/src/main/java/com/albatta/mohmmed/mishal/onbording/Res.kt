package com.albatta.mohmmed.mishal.onbording

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.count
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.countOfhelps
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_listOfPublicQuestion
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.listOfchoisen
import kotlinx.android.synthetic.main.activity_res.btn_duplicate
import kotlinx.android.synthetic.main.activity_res.tv_Cor_Ans
import kotlinx.android.synthetic.main.activity_res.tv_UnCor_Ans
import kotlinx.android.synthetic.main.activity_res.tv_all
import kotlinx.android.synthetic.main.activity_res.tv_con
import kotlinx.android.synthetic.main.activity_res.tv_lead
import kotlin.Exception

class Res : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res)

        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                tv_con.setTextColor(Color.WHITE)
                tv_lead.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                tv_con.setTextColor(Color.BLACK)
                tv_lead.setTextColor(Color.BLACK)
            }
        }
        val size= db.getPublicQuestion().size
        if (count == size){
            tv_con.text="تهانينا لقد فزت ! 😁🤩🥳"

        }else if (Math.round(size/2.toDouble()) <=count){
            tv_con.text="تهانينا لقد فزت !\uD83D\uDE01\uD83E\uDD29\uD83E\uDD73"
        } else
            tv_con.text="للأسف لقد خسرت ! 😥😕😔"
        tv_all.text=size.toString()

        if (tv_con.text.toString()!="للأسف لقد خسرت ! 😥😕😔"){
            btn_duplicate.text="العودة إلى الشاشة الرئيسية"
        }
        tv_Cor_Ans.text= count.toString()
        tv_UnCor_Ans.text=(size-count).toString()
        btn_duplicate.setOnClickListener {
            try {
                if (btn_duplicate.text.toString() == "إعادة الاختبار") {
                    reset()
                    startActivity(Intent(this, PublicQuestionScreen::class.java))
                    finishAffinity()
                } else {
                    reset()
                    finishAffinity()
                    startActivity(Intent(this@Res,MainActivity::class.java))
                }
            }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode==KeyEvent.KEYCODE_BACK){
           reset()
            finishAffinity()
            startActivity(Intent(this@Res,MainActivity::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }
    private fun reset(){
        index_of_listOfPublicQuestion = 0
        count = 0
        countOfhelps = 3
        listOfchoisen.clear()
            MainActivity.index_of_listOfPrivateQuestion = 0
            MainActivity.count_Private = 0
            MainActivity.countOfhelps_Private = 3
            MainActivity.listOfchoisen_Private.clear()
    }
}