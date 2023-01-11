package com.albatta.mohmmed.mishal.onbording

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import kotlinx.android.synthetic.main.activity_main4.account
import kotlinx.android.synthetic.main.activity_main4.cardView3
import kotlinx.android.synthetic.main.activity_main4.image_ac
import kotlinx.android.synthetic.main.activity_main4.makePrivate
import kotlinx.android.synthetic.main.activity_main4.startPrivate
import kotlinx.android.synthetic.main.activity_main4.startPublic
import kotlinx.android.synthetic.main.activity_main4.tv_account
import kotlinx.android.synthetic.main.activity_main4.tv_desForQuiz
import kotlinx.android.synthetic.main.activity_main4.tv_makePrivateQuestions
import kotlinx.android.synthetic.main.activity_main4.tv_managerPrivate
import kotlinx.android.synthetic.main.activity_main4.tv_quiz
import kotlinx.android.synthetic.main.activity_main4.tv_startPublicQuestions
import kotlinx.android.synthetic.main.activity_res.tv_con
import kotlinx.android.synthetic.main.activity_res.tv_lead
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    companion object{
        var index_of_listOfPrivateQuestion=0
        var count_Private=0
        var countOfhelps_Private=3
        val listOfchoisen_Private=ArrayList<String>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        try {
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                tv_quiz.setTextColor(Color.WHITE)
                tv_desForQuiz.setTextColor(Color.WHITE)
                tv_startPublicQuestions.setTextColor(Color.WHITE)
                tv_makePrivateQuestions.setTextColor(Color.WHITE)
                tv_managerPrivate.setTextColor(Color.WHITE)
                tv_account.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                tv_quiz.setTextColor(Color.BLACK)
                tv_desForQuiz.setTextColor(Color.BLACK)
                tv_startPublicQuestions.setTextColor(Color.BLACK)
                tv_makePrivateQuestions.setTextColor(Color.BLACK)
                tv_managerPrivate.setTextColor(Color.BLACK)
                tv_account.setTextColor(Color.BLACK)
            }
        }
        if (db.getPrivateQuestion(db.getUser()[index_of_User].id).isNotEmpty()){
            cardView3.visibility=View.VISIBLE
        }
        startPrivate.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this,PrivateQuestionScreen::class.java))
        }
        if (db.getUser()[index_of_User].image!=null){
            image_ac.setImageBitmap(Utils.getImage(db.getUser()[index_of_User].image!!))
        }
        startPublic.setOnClickListener {
            if (db.getPublicQuestion().isNotEmpty()) {
                finishAffinity()
                startActivity(Intent(this, PublicQuestionScreen::class.java))
            }else{
                Toast.makeText(this, "لا يوجد إختبار عام حاليا", Toast.LENGTH_SHORT).show()
            }
        }
        account.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this,Account::class.java))
        }

        makePrivate.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this,MakePrivate::class.java))
        }
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}