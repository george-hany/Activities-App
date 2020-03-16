package com.example.android.activitiesproject.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.*
import android.view.View.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.network.model.likes.Like
import com.example.android.activitiesproject.databinding.ActivityItemBinding
import com.example.android.activitiesproject.ui.postdetails.adapter.CommentAdapter
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import java.util.*
import android.text.format.DateUtils
import com.example.android.activitiesproject.ui.home.*
import com.example.android.activitiesproject.ui.postdetails.model.Post
import java.util.concurrent.TimeUnit


class PostsAdapter(
    var posts: ArrayList<Post>,
    var userId: String,
    var commentsAdapter: CommentAdapter
) :
    RecyclerView.Adapter<PostsAdapter.PostsHolder>() {
    lateinit var con: Context
    lateinit var postHolder: PostsHolder
    lateinit var onSendComment: OnSendComment
    lateinit var onSendLike: OnSendLike
    lateinit var getAllComments: GetAllComments
    lateinit var showUserProfile: ShowUserProfile
    var likes = HashMap<String, ArrayList<Like>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder {
        con = parent.context
        val v = LayoutInflater.from(parent.context)
        val binding: ActivityItemBinding =
            DataBindingUtil.inflate(v, R.layout.activity_item, parent, false)
        val h =
            PostsHolder(
                binding
            )
        return h
    }

    override fun getItemCount(): Int = posts.size

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PostsHolder, index: Int) {
        postHolder = holder
        val activityImageOption =
            RequestOptions().placeholder(R.drawable.grey_image).error(R.drawable.grey_image)
        val userImageOption = RequestOptions().placeholder(R.drawable.user_default_grey)
            .error(R.drawable.user_default_grey)
        if (posts[index].image == null) {
            holder.activityImage.visibility = GONE
        } else {
            holder.activityImage.visibility = VISIBLE
            Glide.with(con).load(posts[index].image).apply(activityImageOption)
                .into(holder.activityImage)

        }
        Glide.with(con).load(posts[index].user!!.image).apply(userImageOption)
            .into(holder.userImage)

        if (userId == posts[index].user!!.id)
            holder.activityLikesContainer.isEnabled = false
        else
            holder.activityLikesContainer.isEnabled = true

        var list = likes.get(posts[index].id)
        if (list != null && list.size != 0)
            for (i in list) {
                if (i.userid == userId) {
                    holder.activityLikeText.setTextColor(R.color.colorPrimaryDark)
                    holder.activityLikeIcon.setImageResource(R.drawable.heart_icon)
                    break
                } else {
                    holder.activityLikeText.setTextColor(R.color.colorPrimaryDark)
                    holder.activityLikeIcon.setImageResource(R.drawable.unlike_icon)
                }
            } else {
            holder.activityLikeText.setTextColor(R.color.colorPrimaryDark)
            holder.activityLikeIcon.setImageResource(R.drawable.unlike_icon)
        }
        holder.itemView.setOnClickListener {
            Log.e("item", userId)
            Log.e("item", posts[index].user!!.id)

        }
        holder.userImage.setOnClickListener {
            var image: String? = posts[index].user!!.image
            if (image == null) {
                image = ""
            }
            showUserProfile.showProfile(posts[index].user!!.id, image)
        }
        holder.userName.setOnClickListener {
            showUserProfile.showProfile(
                posts[index].user!!.id,
                posts[index].user!!.image!!
            )
        }
        holder.bind(posts.get(index))
        holder.activityShowAllComments.setOnClickListener {
            getAllComments.navtoPostDetailsFragment(posts[index].id!!)
            holder.onClick(
                posts.get(index).id!!,
                posts[index].user!!.id,
                commentsAdapter
            )

        }
        if (userId != posts[index].user!!.id) {
            holder.activityMenu.visibility = INVISIBLE
            holder.activityMenu.isEnabled = false
        } else {
            holder.activityMenu.visibility = VISIBLE
            holder.activityMenu.isEnabled = true
            holder.activityLikesContainer.isEnabled = false
        }

        holder.activityLikesContainer.setOnClickListener {
            if (list != null) {
                for (i in list) {
                    if (i.userid != userId) {
                        holder.activityLikeText.setTextColor(R.color.colorPrimaryDark)
                        holder.activityLikeIcon.setImageResource(R.drawable.heart_icon)
                        onSendLike.onSendLike(userId, posts[index].id!!)
                        var count =
                            (holder.activityLikesCount.text.toString().toInt() + 1).toString()
                        holder.activityLikesCount.setText(count)
                    }
                }
                if (list.size == 0) {
                    if (posts[index].user!!.id != userId) {
                        holder.activityLikeText.setTextColor(R.color.colorPrimaryDark)
                        holder.activityLikeIcon.setImageResource(R.drawable.heart_icon)
                        onSendLike.onSendLike(userId, posts[index].id!!)
                        var count =
                            (holder.activityLikesCount.text.toString().toInt() + 1).toString()
                        holder.activityLikesCount.setText(count)
                    }
                } else {
                    if (posts[index].user!!.id != userId) {
                        holder.activityLikeText.setTextColor(R.color.colorPrimaryDark)
                        holder.activityLikeIcon.setImageResource(R.drawable.heart_icon)
                        onSendLike.onSendLike(userId, posts[index].id!!)
                        var count =
                            (holder.activityLikesCount.text.toString().toInt() + 1).toString()
                        holder.activityLikesCount.setText(count)
                    }
                }
            }
        }
        holder.activitySendComment.setOnClickListener {
            onSendComment.onSend(
                posts[index].id!!,
                userId,
                holder.activityComment.text.toString()
            )
            holder.activityComment.setText("")
        }
        holder.itemView.setOnClickListener {
            getAllComments.navtoPostDetailsFragment(posts[index].id!!)
            holder.onClick(
                posts.get(index).id!!,
                posts[index].user!!.id,
                commentsAdapter
            )
        }
        holder.activityLikesCount.text = posts[index].totallikes
        val date = posts[index].insertiondate

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val time = sdf.parse(date)!!.time
        val now = Date().time
        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now - time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now - time)
        val hours = TimeUnit.MILLISECONDS.toHours(now - time)
        val days = TimeUnit.MILLISECONDS.toDays(now - time)

        if (days > 30)
            holder.activityDate.setText(ago)
        else if (days < 30 && days > 0)
            holder.activityDate.setText("$days d ago")
        else if (hours < 24 && hours > 0)
            holder.activityDate.setText("$hours h ago")
        else if (minutes < 60 && minutes > 0)
            holder.activityDate.setText("$minutes m ago")
        else if (seconds < 60 && seconds > 0)
            holder.activityDate.setText("$seconds s ago")
        else
            holder.activityDate.setText("Just now")

//        else
//            holder.activityDate.setText(ago)
        val niceDateStr = DateUtils.getRelativeTimeSpanString(
            time,
            Calendar.getInstance().timeInMillis,
            DateUtils.SECOND_IN_MILLIS
        )
        Log.e("time", (now - time).toString())
    }

    class PostsHolder(var v: ActivityItemBinding) : RecyclerView.ViewHolder(v.root),
        View.OnCreateContextMenuListener {
        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuinfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(this.adapterPosition, 0, 0, "Delete")
        }

        var userImage = v.activityProfileImage
        var activityImage = v.activityBodyImage
        var activityComment = v.activityComment
        var activitySendComment = v.activitySendComment
        var activityLikesContainer = v.activityLikeContainer
        var activityShowAllComments = v.activityAllComments
        var activityDate = v.activityDate
        var activityMenu = v.activityMenu
        var activityLikeText = v.activityLike
        var activityLikeIcon = v.activityLikeIcon
        var activityLikesCount = v.activityLikesCount2
        var userName = v.activityUserName
        lateinit var userid: String

        fun bind(post: Post) {
            v.post = post
            activityMenu.setOnCreateContextMenuListener(this)
        }

        fun onClick(
            id: String,
            userId: String,
            commentsAdapter: CommentAdapter
        ) {
            userid = userId
        }


    }
}