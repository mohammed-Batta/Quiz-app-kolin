package com.albatta.mohmmed.mishal.onbording

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
class Adapter(private val context: Context,private val list: List<OnboardingItem>): RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val image=view.findViewById<LottieAnimationView>(R.id.animationView)
        val title=view.findViewById<TextView>(R.id.textTitle)
        val des=view.findViewById<TextView>(R.id.textDes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.onboarding_item_container,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list=list[position]
        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                holder.title.setTextColor(Color.WHITE)
                holder.des.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                holder.title.setTextColor(Color.BLACK)
                holder.des.setTextColor(Color.BLACK)
            }
        }
       holder.image.setAnimation(list.lottie)
        holder.title.text = list.title
        holder.des.text = list.description
    }

    override fun getItemCount(): Int {
        return list.size
    }

}