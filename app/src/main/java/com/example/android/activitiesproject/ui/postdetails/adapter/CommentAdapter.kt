package com.example.android.activitiesproject.ui.postdetails.adapter

import android.text.format.DateUtils
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.databinding.CommentItemBinding
import com.example.android.activitiesproject.ui.home.OnItemClicked
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CommentAdapter(var comments: ArrayList<Comment>, var userId: String) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    lateinit var onItemClicked: OnItemClicked
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val v = LayoutInflater.from(parent.context)
        val binding: CommentItemBinding =
            DataBindingUtil.inflate(v, R.layout.comment_item, parent, false)
        val h =
            CommentHolder(
                binding
            )
        return h
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentHolder, index: Int) {
        if (userId != comments[index].user.id) {
            holder.commentMenu.visibility = INVISIBLE
            holder.commentMenu.isEnabled = false
        } else {
            holder.commentMenu.visibility = VISIBLE
            holder.commentMenu.isEnabled = true
        }
        holder.bind(comments.get(index))
        holder.commentMenu.setOnClickListener {
            onItemClicked.onClick(comments[index].postid, comments[index].id)
        }
        val date = comments[index].insertiondate
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time = sdf.parse(date)!!.time
        val now = Date().time
        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now - time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now - time)
        val hours = TimeUnit.MILLISECONDS.toHours(now - time)
        val days = TimeUnit.MILLISECONDS.toDays(now - time)

        if (days > 30)
            holder.commentDate.setText(ago)
        else if (days < 30 && days > 0)
            holder.commentDate.setText("$days d ago")
        else if (hours < 24 && hours > 0)
            holder.commentDate.setText("$hours h ago")
        else if (minutes < 60 && minutes > 0)
            holder.commentDate.setText("$minutes m ago")
        else if (seconds < 60 && seconds > 0)
            holder.commentDate.setText("$seconds s ago")
        else
            holder.commentDate.setText("Just now")
    }


    class CommentHolder(var v: CommentItemBinding) : RecyclerView.ViewHolder(v.root),
        View.OnCreateContextMenuListener {
        val commentMenu = v.commentMenu
        val commentDate = v.commentDate
        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(this.adapterPosition, 1, 0, "Delete")
        }

        fun bind(comment: Comment) {
            v.comment = comment
            commentMenu.setOnCreateContextMenuListener(this)
        }


    }
}