package com.example.android.activitiesproject.ui.rank.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.network.model.Rank
import com.example.android.activitiesproject.databinding.RankItemBinding

class RanksAdapter(var ranks: ArrayList<Rank>, val clickListener: (String,String) -> Unit) : RecyclerView.Adapter<RanksAdapter.RanksHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RanksHolder {
        val v = LayoutInflater.from(parent.context)
        val binding: RankItemBinding = DataBindingUtil.inflate(v, R.layout.rank_item, parent, false)
        val h =
            RanksHolder(
                binding
            )
        return h
    }

    override fun getItemCount(): Int = ranks.size

    override fun onBindViewHolder(holder: RanksHolder, index: Int) {
//        holder.rank_profile_image.setOnClickListener {
//            val bundle= bundleOf("userId" to ranks.get(index).userid)
//
//        }
        val rank=index+1
        if (rank>3){
            holder.rank_order_image.visibility=GONE
            holder.rank_order_text.visibility=VISIBLE
            holder.rank_order_text.setText("#$rank")
        }
        else{
            holder.rank_order_image.visibility=VISIBLE
            holder.rank_order_text.visibility=GONE
        }
        when(rank){
            1->{
                holder.rank_order_image.setImageResource(R.drawable.first)
            }
            2->{
                holder.rank_order_image.setImageResource(R.drawable.second)
            }
            3->
            {
                holder.rank_order_image.setImageResource(R.drawable.third)
            }

        }
        holder.bind(ranks.get(index))
        var image=""
        if(ranks.get(index).imagepath!=null)
        {
            image=ranks.get(index).imagepath
        }

        holder.onClick(ranks.get(index).userid,image,clickListener)
    }

    class RanksHolder(var v: RankItemBinding) : RecyclerView.ViewHolder(v.root) {
        val rank_order_image=v.rankOrderImage
        val rank_order_text=v.rankOrderText
        val rank_profile_image=v.rankProfileImage
        val rank_user_name=v.rankUserName
        fun bind(rank: Rank) {
            v.rank = rank
        }
        fun onClick(id:String,image:String?,clickListener: (String,String) -> Unit){
            rank_profile_image.setOnClickListener { clickListener(id,image!!) }
            rank_user_name.setOnClickListener { clickListener(id,image!!) }
        }

    }
}