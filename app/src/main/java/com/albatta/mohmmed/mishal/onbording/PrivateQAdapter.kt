package com.albatta.mohmmed.mishal.onbording

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.albatta.mohmmed.mishal.onbording.Splash.Companion.db
import kotlinx.android.synthetic.main.item.view.btn_delete
import kotlinx.android.synthetic.main.item.view.btn_edit
import kotlinx.android.synthetic.main.item.view.image_view_haveOrNot
import kotlinx.android.synthetic.main.item.view.tv_ansDe
import kotlinx.android.synthetic.main.item.view.tv_opt1De
import kotlinx.android.synthetic.main.item.view.tv_opt2De
import kotlinx.android.synthetic.main.item.view.tv_opt3De
import kotlinx.android.synthetic.main.item.view.tv_qDe

class PrivateQAdapter(val context:Context, private val list: ArrayList<PrivateQuestion>): RecyclerView.Adapter<PrivateQAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val question: TextView =view.tv_qDe
        val answer: TextView =view.tv_ansDe
        val option1: TextView =view.tv_opt1De
        val option2: TextView =view.tv_opt2De
        val option3: TextView =view.tv_opt3De
        val btn_delete:Button=view.btn_delete
        val btn_edit:Button=view.btn_edit
        val image_view_haveOrNot:ImageView=view.image_view_haveOrNot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val l=list[position]
        holder.question.text= l.question
        holder.answer.text= l.answer
        holder.option1.text= l.option1
        holder.option2.text= l.option2
        holder.option3.text= l.option3

        holder.btn_edit.setOnClickListener {
            val intent=Intent(context,UpdatePrivateQuestion::class.java)
            intent.putExtra("id_Private",l.id)
            context.startActivity(intent)
        }

        if (l.image!=null){
            holder.image_view_haveOrNot.setImageResource(R.drawable.baseline_image_24)
        }else{
            holder.image_view_haveOrNot.setImageResource(R.drawable.baseline_image_not_supported_24)

        }
        holder.btn_delete.setOnClickListener {
            val result=db.deletePrivateQuestion(l.id)
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
            if(result) {
                Toast.makeText(context, "تم حذف السؤال", Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(context, "حدث خلل أثناء حذف السؤال", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}