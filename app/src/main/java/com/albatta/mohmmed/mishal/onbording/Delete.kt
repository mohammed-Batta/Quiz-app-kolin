package com.albatta.mohmmed.mishal.onbording

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import kotlinx.android.synthetic.main.activity_delete.rv
import kotlinx.android.synthetic.main.activity_delete.search
import java.util.Locale

class Delete : AppCompatActivity() {
    lateinit var adapter: AdapterShow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)
        adapter= AdapterShow(this,db.getPublicQuestion())
        rv.adapter=adapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = linearLayoutManager
        rv.setHasFixedSize(true)

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = ArrayList<PublicQuestion>()
                if (search.text!!.isNotEmpty()) {
                    for (i in 0 until db.getPublicQuestion().size) {
                        if (db.getPublicQuestion()[i].question.lowercase(Locale.getDefault()).contains(s.toString().lowercase(Locale.getDefault()))
                            || db.getPublicQuestion()[i].answer.lowercase(Locale.getDefault()).contains(s.toString().lowercase(Locale.getDefault()))) {
                            filtered.add(db.getPublicQuestion()[i])
                        }

                    }
                    adapter = AdapterShow( this@Delete, filtered)
                    rv.adapter = adapter

                } else {
                    adapter = AdapterShow( this@Delete, db.getPublicQuestion())
                    rv.adapter = adapter
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode== KeyEvent.KEYCODE_BACK){

            finishAffinity()
            startActivity(Intent(this,Admin::class.java))
            true
        }else{
            super.onKeyDown(keyCode, event)
        }

    }



}