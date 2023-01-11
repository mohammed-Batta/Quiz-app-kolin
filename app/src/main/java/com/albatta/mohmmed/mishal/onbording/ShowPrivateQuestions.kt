package com.albatta.mohmmed.mishal.onbording

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.index_of_User
import kotlinx.android.synthetic.main.activity_delete.rv
import kotlinx.android.synthetic.main.activity_delete.search
import kotlinx.android.synthetic.main.activity_show_private_questions.rv_Private
import java.util.Locale

class ShowPrivateQuestions : AppCompatActivity() {
    lateinit var adapter: PrivateQAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_private_questions)

        adapter= PrivateQAdapter(this, db.getPrivateQuestion(db.getUser()[index_of_User].id))
        rv_Private.adapter=adapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_Private.layoutManager = linearLayoutManager
        rv_Private.setHasFixedSize(true)

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = ArrayList<PrivateQuestion>()
                if (search.text!!.isNotEmpty()) {
                    for (i in 0 until db.getPrivateQuestion(db.getUser()[index_of_User].id).size) {
                        if (db.getPrivateQuestion(db.getUser()[index_of_User].id)[i].question.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                Locale.getDefault()))
                            || db.getPrivateQuestion(db.getUser()[index_of_User].id)[i].answer.lowercase(Locale.getDefault()).contains(s.toString().lowercase(
                                Locale.getDefault()))) {
                            filtered.add(db.getPrivateQuestion(db.getUser()[index_of_User].id)[i])
                        }

                    }
                    adapter = PrivateQAdapter( this@ShowPrivateQuestions, filtered)
                    rv_Private.adapter = adapter

                } else {
                    adapter = PrivateQAdapter( this@ShowPrivateQuestions,db.getPrivateQuestion(db.getUser()[index_of_User].id))
                    rv_Private.adapter = adapter
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){
            finishAffinity()
            startActivity(Intent(this,MakePrivate::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }


}