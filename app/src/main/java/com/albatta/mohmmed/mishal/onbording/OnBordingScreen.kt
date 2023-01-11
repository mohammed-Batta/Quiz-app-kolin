package com.albatta.mohmmed.mishal.onbording

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.sharedPref
import kotlinx.android.synthetic.main.activity_main.*


class OnBordingScreen : AppCompatActivity() {
    private lateinit var adapter: Adapter

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list= listOf(
            OnboardingItem(R.raw.q,"نوع السؤال", "الأسئلة هي أسئلة عامة ثقافية لتنمية القدرات الذهنية وتنشيط الدماغ والأسئلة جميعها ستكون سهلة")
            ,OnboardingItem(R.raw.multi,"نوع الاختبار","الاختبار مكون من أسئلة اختر من متعدد حيث يكون هناك ثلاث اختيارات خاطئة وواحدة صحيحة"),
            OnboardingItem(R.raw.c,"طريقة الحل","يحق لك في كل محاولة اختيار جواب واحد فقط من الأربعة أجوبة ويمنع اختيار أكثر من ذلك"))
        adapter= Adapter(this,list)
        viewPager2.adapter=adapter

        worm_dots_indicator.attachTo(viewPager2)

        btn_previous.setOnClickListener {
            if (viewPager2.currentItem+1!=1){
                viewPager2.currentItem-=1
            }
        }
           btn_Next.setOnClickListener {
               if (viewPager2.currentItem+1==3) {
               val editor = sharedPref.edit()
               editor.putString("flag", "1")
               editor.apply()
               goTo()
           }else{
                   if (viewPager2.currentItem +1 <adapter.itemCount){
                       viewPager2.currentItem+=1
                   }
               }
       }

        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position+1!=1){
                    btn_previous.visibility=View.VISIBLE
                }else {
                    btn_previous.visibility = View.GONE
                }

                if (position+1==adapter.itemCount){
                    btn_Next.text = "finish"
                }else
                    btn_Next.text = "Next"
            }
        })
        (viewPager2.getChildAt(0) as RecyclerView).overScrollMode= RecyclerView.OVER_SCROLL_NEVER
    }



    private fun goTo(){
        startActivity(Intent(this,Login::class.java))
        finishAffinity()
    }

}