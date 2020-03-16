package com.example.android.activitiesproject.ui.postdetails


import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.databinding.FragmentPostDetailsBinding
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import com.example.android.activitiesproject.ui.postdetails.adapter.CommentAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding, PostDetailsViewModel>(),
    PostDetailsInterface {


    var id = ""
    var userId = ""
    var isLiked = false
    lateinit var commentAdapter: CommentAdapter
    @Inject
    lateinit var mViewModel: PostDetailsViewModel

    lateinit var pref: SharedPreferenceHelper
    override fun layoutId(): Int {
        return R.layout.fragment_post_details
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectPostDetailsFragment(this)
        super.viewModel = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref=SharedPreferenceHelper(context!!)
        userId=pref.getId()!!
        mViewModel.token=pref.getToken()!!
        val args = arguments
        id = args!!.getString("id")!!
        mViewModel.id = id
        mViewModel.postDetailsInterface = this

        val activityImageOption =
            RequestOptions().placeholder(R.drawable.grey_image).error(R.drawable.grey_image)
        val userImageOption = RequestOptions().placeholder(R.drawable.user_default_grey)
        mViewModel.getPost(id)
        mViewModel.getAllComments(id)
        activity!!.toolbar_title.setText("Details")
        setHasOptionsMenu(true)

        mViewModel.post.observe(this, Observer {

            dataBinding.activityUserName.text = it!!.user!!.name
            dataBinding.activityDate.text = it.insertiondate
            dataBinding.activityBodyText.text = it.text
            dataBinding.activityLikesCount2.text = it.totallikes
            if (it.image == null)
                dataBinding.activityBodyImage.visibility = GONE
            else
                Glide.with(this).load(it.image).apply(activityImageOption).into(dataBinding.activityBodyImage)

            Glide.with(this).load(it.user!!.image).apply(userImageOption)
                .into(dataBinding.activityProfileImage)
            if (userId != it.user.id) {
                setHasOptionsMenu(false)
                dataBinding.activityLikeContainer.isEnabled = true
            } else
                dataBinding.activityLikeContainer.isEnabled = false
            val date = it.insertiondate
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
//        sdf.timeZone = TimeZone.getTimeZone("GMT")
            val time = sdf.parse(date)!!.time
//        val now = System.currentTimeMillis()
            val now = Date().time
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now - time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now - time)
            val hours = TimeUnit.MILLISECONDS.toHours(now - time)
            val days = TimeUnit.MILLISECONDS.toDays(now - time)

            if (days > 30)
                dataBinding.activityDate.setText(ago)
            else if (days < 30 && days > 0)
                dataBinding.activityDate.setText("$days d ago")
            else if (hours < 24 && hours > 0)
                dataBinding.activityDate.setText("$hours h ago")
            else if (minutes < 60 && minutes > 0)
                dataBinding.activityDate.setText("$minutes m ago")
            else if (seconds < 60)
                dataBinding.activityDate.setText("$seconds s ago")


        })
        mViewModel.totalLikes.observe(this, Observer {
            dataBinding.activityLikesCount2.text = it
        })

        mViewModel.comments.observe(this, Observer {
            commentAdapter =
                CommentAdapter(
                    it,
                    userId
                )
            dataBinding.commentsRecycle.adapter = commentAdapter
        })

        dataBinding.activitySendComment.setOnClickListener {
            if (dataBinding.activityComment.text.isNotEmpty())
                mViewModel.sendComment(id, userId, dataBinding.activityComment.text.toString())

        }

        dataBinding.activityLikeContainer.setOnClickListener {
            if (!isLiked) {
                mViewModel.sendLike(userId, id)
                dataBinding.activityLikeIcon.setImageResource(R.drawable.heart_icon)
            }
        }

        mViewModel.getAllLikes()
        mViewModel.likes.observe(this, Observer {
            for (like in it) {
                if (userId == like.userid) {
                    dataBinding.activityLikeIcon.setImageResource(R.drawable.heart_icon)
                    isLiked = true
                    break
                }
            }
        })

        // Inflate the layout for this fragment
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.post_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_delete -> {
                mViewModel.deletePost()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDeletePostSuccess() {
        Toasty.success(context!!, "post deleted", Toasty.LENGTH_SHORT).show()
        navigation.navigate(R.id.action_postDetails_to_home)
    }

    override fun onAddCommentSuccess() {
        dataBinding.activityComment.setText("")
        val vv = activity?.currentFocus
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(vv!!.windowToken, 0)
        commentAdapter.notifyDataSetChanged()
        mViewModel.getAllComments(id)

    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

            1 -> {
                mViewModel.deleteComment(
                    commentAdapter.comments[item.groupId].id,
                    commentAdapter.comments[item.groupId].postid
                )


            }
        }
        return super.onContextItemSelected(item)
    }


}
