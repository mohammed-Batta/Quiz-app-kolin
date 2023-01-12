package com.albatta.mohmmed.mishal.onbording

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.countOfhelps_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.count_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.index_of_listOfPrivateQuestion
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.listOfchoisen_Private
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.count
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.countOfhelps
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_listOfPublicQuestion
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.listOfchoisen
import kotlinx.android.synthetic.main.activity_main2.RadioGroup
import kotlinx.android.synthetic.main.activity_main2.btn_submit
import kotlinx.android.synthetic.main.activity_main2.expanded_image
import kotlinx.android.synthetic.main.activity_main2.image_light_helps
import kotlinx.android.synthetic.main.activity_main2.image_view_show
import kotlinx.android.synthetic.main.activity_main2.r1
import kotlinx.android.synthetic.main.activity_main2.r2
import kotlinx.android.synthetic.main.activity_main2.r3
import kotlinx.android.synthetic.main.activity_main2.r4
import kotlinx.android.synthetic.main.activity_main2.screenRe
import kotlinx.android.synthetic.main.activity_main2.tv_num_helps
import kotlinx.android.synthetic.main.activity_main2.tv_question
import kotlinx.android.synthetic.main.activity_main2.tv_timer
import kotlinx.android.synthetic.main.help.view.rG_help
import kotlinx.android.synthetic.main.help.view.r_help_1
import kotlinx.android.synthetic.main.help.view.r_help_2
import kotlinx.android.synthetic.main.help.view.r_help_3
import java.io.ByteArrayOutputStream

class PublicQuestionScreen : AppCompatActivity(){

    private var currentAnimator: Animator? = null

    private var shortAnimationDuration: Int = 0
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("InflateParams", "UnlocalizedSms", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        try {


            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    tv_timer.setTextColor(Color.WHITE)
                    tv_question.setTextColor(Color.WHITE)
                    r1.setTextColor(Color.WHITE)
                    r2.setTextColor(Color.WHITE)
                    r3.setTextColor(Color.WHITE)
                    r4.setTextColor(Color.WHITE)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    tv_timer.setTextColor(Color.BLACK)
                    tv_question.setTextColor(Color.BLACK)
                    r1.setTextColor(Color.BLACK)
                    r2.setTextColor(Color.BLACK)
                    r3.setTextColor(Color.BLACK)
                    r4.setTextColor(Color.BLACK)

                }
            }
            image_view_show.setOnClickListener {
                if (db.getPublicQuestion()[index_of_listOfPublicQuestion].image!=null) {
                    val bitmap = Utils.getImage(db.getPublicQuestion()[index_of_listOfPublicQuestion].image!!)
                    zoomImageFromThumb(image_view_show, bitmap)
                }
            }
            shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
            tv_num_helps.text=countOfhelps.toString()
            image_light_helps.setOnClickListener {
                if (countOfhelps!=0) {
                    val inf = LayoutInflater.from(this).inflate(R.layout.help, null)
                    val alert = AlertDialog.Builder(this)
                    val listOfRad= arrayOf(inf.r_help_1,inf.r_help_2,inf.r_help_3)

                    alert.setTitle("المساعدات")
                    alert.setMessage("إذا تم استخدام مساعدة فلن تستطيع إعادة إستخدامها!")
                    alert.setView(inf)
                    alert.setIcon(R.drawable.idea)
                    for (i in listOfRad.indices){
                        for (f in listOfchoisen.indices) {
                            if (listOfRad[i].text.toString() ==listOfchoisen[f]){
                                listOfRad[i].isEnabled=false
                            }
                        }
                    }
                        alert.setPositiveButton("إستخدام المساعدة") { _ , _ ->
                            val id = inf.rG_help.checkedRadioButtonId
                            val checked = inf.findViewById<RadioButton>(id)
                            if (checked != null) {

                                if (checked.text.toString() == "مشاركة السؤال مع صديق") {
                                    try {
                                        timer.cancel()
                                        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                                            Configuration.UI_MODE_NIGHT_NO -> {
                                                screenRe.setBackgroundColor(Color.WHITE)
                                            }
                                        }

                                        listOfchoisen.add(checked.text.toString())
                                        val bitmap = getScreenShotFromView(screenRe)
                                        if (db.getPublicQuestion()[index_of_listOfPublicQuestion].image!=null) {
                                            shareImageAndText(bitmap!!, Utils.getImage(db.getPublicQuestion()[index_of_listOfPublicQuestion].image!!))
                                        }else{
                                            shareImageAndText(bitmap!!, null)
                                        }


                                        countOfhelps -= 1
                                        tv_num_helps.text = countOfhelps.toString()
                                    } catch (e: Exception) {
                                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                                    }
                                } else if (checked.text.toString() == "لمحة عن الإجابة الصحيحة") {
                                    listOfchoisen.add(checked.text.toString())
                                    Toast.makeText(
                                        this,
                                        db.getPublicQuestion()[index_of_listOfPublicQuestion].hintOfAnswer,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    countOfhelps -= 1
                                    tv_num_helps.text = countOfhelps.toString()
                                } else {
                                    listOfchoisen.add(checked.text.toString())
                                    if (index_of_listOfPublicQuestion != db.getPublicQuestion().size - 1) {
                                        count += 1
                                        index_of_listOfPublicQuestion += 1
                                        countOfhelps -= 1
                                        tv_num_helps.text = countOfhelps.toString()
                                        timer.cancel()
                                        RadioGroup.clearCheck()
                                        this.recreate()
                                    } else {
                                        count += 1
                                        timer.cancel()
                                        val intent = Intent(this@PublicQuestionScreen, Res::class.java)
                                        startActivity(intent)
                                        finishAffinity()
                                    }
                                    Toast.makeText(
                                        this@PublicQuestionScreen,
                                        "تم احتساب السؤال",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            } else {
                                Toast.makeText(this, "لم يتم إختيار أي مساعدة", Toast.LENGTH_SHORT)
                                    .show()

                            }
                        }

                    alert.setNegativeButton("إلغاء",null)
                    alert.show()
                } else{
                    Toast.makeText(this, "لم تتبقى لديك أي مساعدات", Toast.LENGTH_SHORT).show()
                }

            }




            tv_question.text = db.getPublicQuestion()[index_of_listOfPublicQuestion].question
            if(db.getPublicQuestion()[index_of_listOfPublicQuestion].image!=null){
                image_view_show.setImageBitmap(Utils.getImage(db.getPublicQuestion()[index_of_listOfPublicQuestion].image!!))
            }
            val listOfRadio = listOf(r1, r2, r3, r4)
            val listOfOptionsAndAnswer = listOf(db.getPublicQuestion()[index_of_listOfPublicQuestion].answer,db.getPublicQuestion()[index_of_listOfPublicQuestion].option1, db.getPublicQuestion()[index_of_listOfPublicQuestion].option2, db.getPublicQuestion()[index_of_listOfPublicQuestion].option3)
          val listOfOptionsRandom=  listOfOptionsAndAnswer.asSequence().shuffled().take(listOfOptionsAndAnswer.size).toList()

            for (i in listOfRadio.indices) {
                listOfRadio[i].text = listOfOptionsRandom[i]
            }
            btn_submit.setOnClickListener {
                try {
                    val listOfRadioButton= arrayOf(r1,r2,r3,r4)
                    val intent = Intent(this, Res::class.java)
                    btn_submit.isEnabled=false
                    if (index_of_listOfPublicQuestion != db.getPublicQuestion().size - 1) {
                        isTrue(timer)

                    } else {
                        val id = RadioGroup.checkedRadioButtonId
                        val checked = findViewById<RadioButton>(id)
                        if (checked != null) {
                            if (checked.text.toString() == db.getPublicQuestion()[index_of_listOfPublicQuestion].answer) {
                                Toast.makeText(this, "أحسنت!", Toast.LENGTH_SHORT).show()
                                count += 1
                                 checked.setBackgroundColor(Color.parseColor("#89FF00"))
                                 checked.setTextColor(Color.BLACK)
                                for (i in listOfRadioButton.indices){listOfRadioButton[i].isEnabled=false}
                                timer.cancel()
                                image_view_show.isEnabled=false
                                image_light_helps.isEnabled=false
                                Handler(Looper.myLooper()!!).postDelayed({
                                    RadioGroup.clearCheck()
                                    finishAffinity()
                                    startActivity(intent)
                                     },1000)

                            } else {
                                Toast.makeText(this, "الإجابة خاطئة", Toast.LENGTH_SHORT).show()

                                for (i in listOfRadioButton.indices){
                                    listOfRadioButton[i].isEnabled=false
                                    if (listOfRadioButton[i].text.toString() == db.getPublicQuestion()[index_of_listOfPublicQuestion].answer){
                                        listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                                        listOfRadioButton[i].setTextColor(Color.BLACK)


                                    }
                                }
                                checked.setBackgroundColor(Color.parseColor("#FF0000"))
                                checked.setTextColor(Color.BLACK)

                                timer.cancel()
                                image_view_show.isEnabled=false
                                image_light_helps.isEnabled=false

                                Handler(Looper.myLooper()!!).postDelayed({
                                    RadioGroup.clearCheck()
                                    finishAffinity()
                                    startActivity(intent)
                                },1000)
                            }
                        } else {
                            timer.cancel()
                            Toast.makeText(this, "لم يتم احتساب الإجابة", Toast.LENGTH_SHORT).show()
                            for (i in listOfRadioButton.indices){
                                listOfRadioButton[i].isEnabled=false
                                if (listOfRadioButton[i].text.toString() == db.getPublicQuestion()[index_of_listOfPublicQuestion].answer){
                                    listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                                    listOfRadioButton[i].setTextColor(Color.BLACK)



                                }
                            }
                            image_view_show.isEnabled=false
                            image_light_helps.isEnabled=false

                            Handler(Looper.myLooper()!!).postDelayed({
                                finishAffinity()
                                startActivity(intent)
                            },1000)


                        }


                    }

                } catch (e: Exception) {
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            timer.start()
        }catch (E:Exception){
            Toast.makeText(this, E.message, Toast.LENGTH_LONG).show()}
    }
    private val timer=object :CountDownTimer(db.getPublicQuestion()[index_of_listOfPublicQuestion].time,1000){
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_timer.text="الوقت المتبقي لديك : ${millisUntilFinished/1000}"
        }
        override fun onFinish() {
            if (index_of_listOfPublicQuestion!=db.getPublicQuestion().size-1) {
                isTrue(this)
            }else{
                this.cancel()
                val intent=Intent(this@PublicQuestionScreen,Res::class.java)
                finishAffinity()
                startActivity(intent)
            }
            Toast.makeText(this@PublicQuestionScreen, "انتهى الوقت", Toast.LENGTH_SHORT).show()

        }

    }

    fun isTrue(timer: CountDownTimer){
        val id = RadioGroup.checkedRadioButtonId
        val checked = findViewById<RadioButton>(id)
        val listOfRadioButton= arrayOf(r1,r2,r3,r4)
        if (checked != null) {
            if (checked.text.toString() == db.getPublicQuestion()[index_of_listOfPublicQuestion].answer) {
                btn_submit.isEnabled=false

                Toast.makeText(this, "أحسنت!", Toast.LENGTH_SHORT).show()
                count += 1
                index_of_listOfPublicQuestion+=1

                checked.setBackgroundColor(Color.parseColor("#89FF00"))
                checked.setTextColor(Color.BLACK)

                for (i in listOfRadioButton.indices){listOfRadioButton[i].isEnabled=false}
                timer.cancel()
                image_view_show.isEnabled=false
                image_light_helps.isEnabled=false

                Handler(Looper.myLooper()!!).postDelayed({
                    RadioGroup.clearCheck()
                    this.recreate() },1000)
            } else {
                btn_submit.isEnabled=false

                Toast.makeText(this, "الإجابة خاطئة", Toast.LENGTH_SHORT).show()
                for (i in listOfRadioButton.indices){
                    listOfRadioButton[i].isEnabled=false
                    if (listOfRadioButton[i].text.toString() == db.getPublicQuestion()[index_of_listOfPublicQuestion].answer){
                        listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                        listOfRadioButton[i].setTextColor(Color.BLACK)


                    }
                }


                    checked.setBackgroundColor(Color.parseColor("#FF0000"))
                checked.setTextColor(Color.BLACK)

                timer.cancel()
                image_view_show.isEnabled=false
                image_light_helps.isEnabled=false
                Handler(Looper.myLooper()!!).postDelayed({
                    index_of_listOfPublicQuestion+=1
                    RadioGroup.clearCheck()
                    this.recreate()
                },1000)

            }
        } else {

            for (i in listOfRadioButton.indices){
                listOfRadioButton[i].isEnabled=false
                if (listOfRadioButton[i].text.toString() == db.getPublicQuestion()[index_of_listOfPublicQuestion].answer){
                    listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                    listOfRadioButton[i].setTextColor(Color.BLACK)


                }
            }
            timer.cancel()
            image_view_show.isEnabled=false
            image_light_helps.isEnabled=false
            Handler(Looper.myLooper()!!).postDelayed({
                index_of_listOfPublicQuestion+=1
                this.recreate()
            },1500)

            Toast.makeText(this, "لم يتم احتساب السؤال", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getScreenShotFromView(v: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {

            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        return screenshot
    }


    @SuppressLint("SuspiciousIndentation")
    private fun shareImageAndText(bitmap:Bitmap,bitmap2:Bitmap?) {
        Thread{
        if (bitmap2!=null) {

                val uri = getImageUri(this, bitmap)
                val uri2 = getImageUri(this, bitmap2)
                val intent =  Intent(Intent.ACTION_SEND_MULTIPLE)
                val array=ArrayList<Uri?>()
                array.add(uri2)
                array.add(uri)
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, array)
                intent.putExtra(Intent.EXTRA_TEXT, "أرجو منك مساعدتي في حل هذا السؤال !")
                intent.putExtra(Intent.EXTRA_SUBJECT, "أرجو منك مساعدتي في حل هذا السؤال !")
                intent.type = "image/png"
                startActivity(Intent.createChooser(intent, "مشاركة السؤال"))
        }else{
                val intent =  Intent(Intent.ACTION_SEND)
                val uri = getImageUri(this,bitmap)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.putExtra(Intent.EXTRA_TEXT, "أرجو منك مساعدتي في حل هذا السؤال !")
                intent.putExtra(Intent.EXTRA_SUBJECT, "أرجو منك مساعدتي في حل هذا السؤال !")
                intent.type = "image/png"
                startActivity(Intent.createChooser(intent, "مشاركة السؤال"))
        }
        }.start()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            timer.cancel()
            index_of_listOfPublicQuestion=0
            finishAffinity()
            count=0
            index_of_listOfPrivateQuestion = 0
            count_Private = 0
            countOfhelps_Private = 3
            countOfhelps = 3
            listOfchoisen_Private.clear()
            listOfchoisen.clear()
            startActivity(Intent(this@PublicQuestionScreen,MainActivity::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }




    private fun zoomImageFromThumb(thumbView: View, imageResId: Bitmap) {
        currentAnimator?.cancel()
        expanded_image.setImageBitmap(imageResId)
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()
        thumbView.getGlobalVisibleRect(startBoundsInt)
        findViewById<View>(R.id.screenRe).getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right+=deltaWidth.toInt()
        } else {

            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }


         thumbView.alpha = 0f
        expanded_image.visibility = View.VISIBLE
        btn_submit.visibility = View.INVISIBLE
        expanded_image.pivotX = 0f
        expanded_image.pivotY = 0f
        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(expanded_image, View.X, startBounds.left, finalBounds.left)).apply {
                with(ObjectAnimator.ofFloat(expanded_image, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }

        expanded_image.setOnClickListener {
            currentAnimator?.cancel()

            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expanded_image, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expanded_image, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image.visibility = View.GONE
                        btn_submit.visibility=View.VISIBLE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image.visibility = View.GONE
                        btn_submit.visibility=View.VISIBLE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage, "IMG_" + System.currentTimeMillis(), null)
        return Uri.parse(path)
    }

}