package com.albatta.mohmmed.mishal.onbording

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlin.properties.Delegates

class Splash : AppCompatActivity() {
    companion object{
        lateinit var sharedPref:SharedPreferences
        @SuppressLint("StaticFieldLeak")
        lateinit var db:DB
        var index_of_listOfPublicQuestion=0
       var index_of_User by Delegates.notNull<Int>()
        const val inside="inside"
        const val outside="outside"
        var flag=0
        var count=0
        var countOfhelps=3
        val listOfchoisen=ArrayList<String>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        db= DB(this)
        sharedPref = this.getSharedPreferences("home", Context.MODE_PRIVATE)
        val flag = sharedPref.getString("flag", "")
        var bool=false
        Handler(Looper.myLooper()!!).postDelayed({
            if (flag=="1"){
                if (db.getUser().isNotEmpty()) {
                    for (i in db.getUser().indices) {
                        if (db.getUser()[i].inout == inside) {
                            bool=true
                            index_of_User = i
                            finishAffinity()
                            startActivity(Intent(this, MainActivity::class.java))
                        }

                    }
                    if (!bool){
                        finishAffinity()
                        startActivity(Intent(this, Login::class.java))
                    }

                }else{
                    finishAffinity()
                    startActivity(Intent(this, Login::class.java))
                }
            }else {
                finishAffinity()
                startActivity(Intent(this, OnBordingScreen::class.java))
            } },1500)
    }
}