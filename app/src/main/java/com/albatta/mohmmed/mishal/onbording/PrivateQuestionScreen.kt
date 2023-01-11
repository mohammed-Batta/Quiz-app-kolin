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
import androidx.appcompat.app.AppCompatActivity
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
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.countOfhelps_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.count_Private
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.index_of_listOfPrivateQuestion
import com.albatta.mohmmed.mishal.onbording.MainActivity.Companion.listOfchoisen_Private
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_private_question_screen.RadioGroup_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.btn_submit_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.expanded_image2
import kotlinx.android.synthetic.main.activity_private_question_screen.image_light_helps_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.image_view_show_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.r1_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.r2_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.r3_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.r4_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.screenRe_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.tv_num_helps_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.tv_question_Private
import kotlinx.android.synthetic.main.activity_private_question_screen.tv_timer_Private
import kotlinx.android.synthetic.main.help.view.rG_help
import kotlinx.android.synthetic.main.help.view.r_help_1
import kotlinx.android.synthetic.main.help.view.r_help_2
import kotlinx.android.synthetic.main.help.view.r_help_3
import java.io.ByteArrayOutputStream
import java.util.Calendar

class PrivateQuestionScreen : AppCompatActivity() {
    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("InflateParams", "UnlocalizedSms")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_question_screen)
        try {


            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    tv_timer_Private.setTextColor(Color.WHITE)
                    tv_question_Private.setTextColor(Color.WHITE)
                    r1_Private.setTextColor(Color.WHITE)
                    r2_Private.setTextColor(Color.WHITE)
                    r3_Private.setTextColor(Color.WHITE)
                    r4_Private.setTextColor(Color.WHITE)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    tv_timer_Private.setTextColor(Color.BLACK)
                    tv_question_Private.setTextColor(Color.BLACK)
                    r1_Private.setTextColor(Color.BLACK)
                    r2_Private.setTextColor(Color.BLACK)
                    r3_Private.setTextColor(Color.BLACK)
                    r4_Private.setTextColor(Color.BLACK)

                }
            }

            image_view_show_Private.setOnClickListener {
                try {

                    if (db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].image != null) {
                        val bitmap = Utils.getImage(db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].image!!)
                        zoomImageFromThumb(image_view_show_Private, bitmap)
                    }
                }catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
            }
            shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
            tv_num_helps_Private.text=countOfhelps_Private.toString()
            image_light_helps_Private.setOnClickListener {
                if (countOfhelps_Private !=0) {
                    val inf = LayoutInflater.from(this).inflate(R.layout.help, null)
                    val alert = AlertDialog.Builder(this)
                    val listOfRad= arrayOf(inf.r_help_1,inf.r_help_2,inf.r_help_3)

                    alert.setTitle("المساعدات")
                    alert.setMessage("إذا تم استخدام مساعدة فلن تستطيع إعادة إستخدامها!")
                    alert.setView(inf)
                    alert.setIcon(R.drawable.idea)
                    for (i in listOfRad.indices){
                        for (f in listOfchoisen_Private.indices) {
                            if (listOfRad[i].text.toString() == listOfchoisen_Private[f]){
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
                                            screenRe_Private.setBackgroundColor(Color.WHITE)
                                        }
                                    }
                                    listOfchoisen_Private.add(checked.text.toString())
                                    val bitmap = getScreenShotFromView(screenRe_Private)
                                    if (db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].image!=null) {
                                            shareImageAndText(bitmap!!, Utils.getImage(db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].image!!))

                                    }else{
                                        shareImageAndText(bitmap!!, null)
                                    }
                                    countOfhelps_Private -= 1
                                    tv_num_helps_Private.text = countOfhelps_Private.toString()
                                } catch (e: Exception) {
                                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()                                }
                            } else if (checked.text.toString() == "لمحة عن الإجابة الصحيحة") {
                                listOfchoisen_Private.add(checked.text.toString())
                                Toast.makeText(
                                    this,
                                    db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].hintOfAnswer,
                                    Toast.LENGTH_LONG
                                ).show()
                                countOfhelps_Private -= 1
                                tv_num_helps_Private.text = countOfhelps_Private.toString()
                            } else {
                                listOfchoisen_Private.add(checked.text.toString())
                                if (index_of_listOfPrivateQuestion != db.getPrivateQuestion(db.getUser()[index_of_User].id).size - 1) {
                                    count_Private += 1
                                    index_of_listOfPrivateQuestion += 1
                                    countOfhelps_Private -= 1
                                    tv_num_helps_Private.text = countOfhelps_Private.toString()
                                    timer.cancel()
                                    image_view_show_Private.isEnabled=false
                                    RadioGroup_Private.clearCheck()
                                    this.recreate()
                                } else {
                                    count_Private += 1
                                    timer.cancel()
                                    val intent = Intent(this, Res_Private::class.java)
                                    finishAffinity()
                                    startActivity(intent)

                                }
                                Toast.makeText(this, "تم احتساب السؤال", Toast.LENGTH_SHORT).show()

                            }

                        } else { Toast.makeText(this, "لم يتم إختيار أي مساعدة", Toast.LENGTH_SHORT).show() }
                    }

                    alert.setNegativeButton("إلغاء",null)
                    alert.show()
                } else{
                    Toast.makeText(this, "لم تتبقى لديك أي مساعدات", Toast.LENGTH_SHORT).show()
                }

            }





            tv_question_Private.text = db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].question
            if(db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].image!=null){
                image_view_show_Private.setImageBitmap(Utils.getImage(db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].image!!))
            }
            val listOfRadio = listOf(r1_Private, r2_Private, r3_Private, r4_Private)
            val listOfOptionsAndAnswer = listOf(db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer,db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].option1, db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].option2, db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].option3)
            val listOfOptionsRandom=  listOfOptionsAndAnswer.asSequence().shuffled().take(listOfOptionsAndAnswer.size).toList()
            for (i in listOfRadio.indices) {
                    listOfRadio[i].text = listOfOptionsRandom[i]
            }
            btn_submit_Private.setOnClickListener {
                try {
                    val listOfRadioButton= arrayOf(r1_Private,r2_Private,r3_Private,r4_Private)
                    val intent = Intent(this, Res_Private::class.java)
                    btn_submit_Private.isEnabled=false
                    if (index_of_listOfPrivateQuestion!= db.getPrivateQuestion(db.getUser()[index_of_User].id).size - 1) {
                        isTrue(timer)

                    } else {
                        val id = RadioGroup_Private.checkedRadioButtonId
                        val checked = findViewById<RadioButton>(id)
                        if (checked != null) {
                            if (checked.text.toString() == db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer) {
                                Toast.makeText(this, "أحسنت!", Toast.LENGTH_SHORT).show()
                                count_Private += 1
                                checked.setBackgroundColor(Color.parseColor("#89FF00"))
                                checked.setTextColor(Color.BLACK)
                                for (i in listOfRadioButton.indices){listOfRadioButton[i].isEnabled=false}
                                timer.cancel()
                                image_view_show_Private.isEnabled=false
                                image_light_helps_Private.isEnabled=false
                                Handler(Looper.myLooper()!!).postDelayed({
                                    RadioGroup_Private.clearCheck()
                                    finishAffinity()
                                    startActivity(intent)
                                },1500)

                            } else {
                                Toast.makeText(this, "الإجابة خاطئة", Toast.LENGTH_SHORT).show()

                                for (i in listOfRadioButton.indices){
                                    listOfRadioButton[i].isEnabled=false
                                    if (listOfRadioButton[i].text.toString() == db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer){
                                        listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                                        listOfRadioButton[i].setTextColor(Color.BLACK)


                                    }
                                }
                                checked.setBackgroundColor(Color.parseColor("#FF0000"))
                                checked.setTextColor(Color.BLACK)

                                timer.cancel()
                                image_view_show_Private.isEnabled=false
                                image_light_helps_Private.isEnabled=false

                                Handler(Looper.myLooper()!!).postDelayed({
                                    RadioGroup_Private.clearCheck()
                                    finishAffinity()
                                    startActivity(intent)
                                },1500)
                            }
                        } else {
                            timer.cancel()
                            image_view_show_Private.isEnabled=false
                            image_light_helps_Private.isEnabled=false

                            Toast.makeText(this, "لم يتم احتساب الإجابة", Toast.LENGTH_SHORT).show()
                            for (i in listOfRadioButton.indices){
                                listOfRadioButton[i].isEnabled=false
                                if (listOfRadioButton[i].text.toString() == db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer){
                                    listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                                    listOfRadioButton[i].setTextColor(Color.BLACK)
                                }
                            }
                            Handler(Looper.myLooper()!!).postDelayed({
                                finishAffinity()
                                startActivity(intent)
                            },1500)


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
    private val timer=object :
        CountDownTimer(db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].time,1000){
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_timer_Private.text="الوقت المتبقي لديك : ${millisUntilFinished/1000}"
        }
        override fun onFinish() {
            if (index_of_listOfPrivateQuestion != db.getPrivateQuestion(db.getUser()[index_of_User].id).size-1) {
                isTrue(this)
            }else{
                this.cancel()
                image_view_show_Private.isEnabled=false
                val intent= Intent(this@PrivateQuestionScreen,Res_Private::class.java)
                finishAffinity()
                startActivity(intent)
            }
            Toast.makeText(this@PrivateQuestionScreen, "انتهى الوقت", Toast.LENGTH_SHORT).show()

        }

    }

    fun isTrue(timer: CountDownTimer){
        val id = RadioGroup_Private.checkedRadioButtonId
        val checked = findViewById<RadioButton>(id)
        val listOfRadioButton= arrayOf(r1_Private,r2_Private,r3_Private,r4_Private)
        if (checked != null) {
            if (checked.text.toString() == db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer) {
                btn_submit_Private.isEnabled=false
                Toast.makeText(this, "أحسنت!", Toast.LENGTH_SHORT).show()
                count_Private += 1
                index_of_listOfPrivateQuestion +=1

                checked.setBackgroundColor(Color.parseColor("#89FF00"))
                checked.setTextColor(Color.BLACK)

                for (i in listOfRadioButton.indices){listOfRadioButton[i].isEnabled=false}
                timer.cancel()
                image_view_show_Private.isEnabled=false
                image_light_helps_Private.isEnabled=false

                Handler(Looper.myLooper()!!).postDelayed({
                    RadioGroup_Private.clearCheck()
                    this.recreate() },1500)
            } else {
                btn_submit_Private.isEnabled=false
                Toast.makeText(this, "الإجابة خاطئة", Toast.LENGTH_SHORT).show()
                for (i in listOfRadioButton.indices){
                    listOfRadioButton[i].isEnabled=false
                    if (listOfRadioButton[i].text.toString() == db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer){
                        listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                        listOfRadioButton[i].setTextColor(Color.BLACK)


                    }
                }


                checked.setBackgroundColor(Color.parseColor("#FF0000"))
                checked.setTextColor(Color.BLACK)

                timer.cancel()
                image_view_show_Private.isEnabled=false
                image_light_helps_Private.isEnabled=false

                Handler(Looper.myLooper()!!).postDelayed({
                    index_of_listOfPrivateQuestion +=1
                    RadioGroup_Private.clearCheck()
                    this.recreate()
                },1500)

            }
        } else {

            for (i in listOfRadioButton.indices){
                listOfRadioButton[i].isEnabled=false
                if (listOfRadioButton[i].text.toString() == db.getPrivateQuestion(db.getUser()[index_of_User].id)[index_of_listOfPrivateQuestion].answer){
                    listOfRadioButton[i].setBackgroundColor(Color.parseColor("#89FF00"))
                    listOfRadioButton[i].setTextColor(Color.BLACK) }}
            timer.cancel()
            image_view_show_Private.isEnabled=false
            image_light_helps_Private.isEnabled=false

            index_of_listOfPrivateQuestion +=1
            Handler(Looper.myLooper()!!).postDelayed({
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
                try {
                    val uri = getImageUri(this, bitmap)
                    val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
                    val array = ArrayList<Uri?>()
                    val uri2 = getImageUri(this, bitmap2)
                    array.add(uri2)
                    array.add(uri)
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, array)
                    intent.putExtra(Intent.EXTRA_TEXT, "أرجو منك مساعدتي في حل هذا السؤال !")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "أرجو منك مساعدتي في حل هذا السؤال !")
                    intent.type = "image/png"
                    startActivity(Intent.createChooser(intent, "مشاركة السؤال"))
                }catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

            }else{
                val intent =  Intent(Intent.ACTION_SEND)
                val uri = getImageUri(this, bitmap)
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
            listOfchoisen_Private.clear()
            countOfhelps_Private = 3
            index_of_listOfPrivateQuestion =0
            finishAffinity()
            startActivity(Intent(this@PrivateQuestionScreen,MainActivity::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }
    private fun zoomImageFromThumb(thumbView: View, imageResId: Bitmap) {
        currentAnimator?.cancel()

        expanded_image2.setImageBitmap(imageResId)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        thumbView.getGlobalVisibleRect(startBoundsInt)
        findViewById<View>(R.id.screenRe_Private).getGlobalVisibleRect(finalBoundsInt, globalOffset)
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
        expanded_image2.visibility = View.VISIBLE
        btn_submit_Private.visibility=View.INVISIBLE


        expanded_image2.pivotX = 0f
        expanded_image2.pivotY = 0f

        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expanded_image2,
                    View.X,
                    startBounds.left,
                    finalBounds.left)
            ).apply {
                with(ObjectAnimator.ofFloat(expanded_image2, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(expanded_image2, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expanded_image2, View.SCALE_Y, startScale, 1f))
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

        expanded_image2.setOnClickListener {
            currentAnimator?.cancel()

            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expanded_image2, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expanded_image2, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expanded_image2, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expanded_image2, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image2.visibility = View.GONE
                        btn_submit_Private.visibility=View.VISIBLE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image2.visibility = View.GONE
                        btn_submit_Private.visibility=View.VISIBLE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        var uri:Uri?=null
        try {


            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
          val path=  MediaStore.Images.Media.insertImage(
                inContext.contentResolver, inImage, "IMG_" + System.currentTimeMillis(), null)
            uri = Uri.parse(path)
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }

}

