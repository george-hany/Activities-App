package com.example.android.activitiesproject.ui.home


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.databinding.FragmentHomeBinding
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.home.adapter.PostsAdapter
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.adapter.CommentAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
const val PICK_IMAGE_REQUEST = 71
const val PICK_File_REQUEST = 72

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeInterface {

    @Inject
    lateinit var mViewModel: HomeViewModel
    lateinit var pref: SharedPreferenceHelper

    var comments = ArrayList<Comment>()
    var posts = ArrayList<Post>()
    lateinit var adapter: PostsAdapter
    lateinit var commentsadapter: CommentAdapter
    var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectHomeFragment(this)
        super.viewModel = mViewModel
    }


    val STORAGE_PERMISSION_CODE: Int = 2342
    var imagePath: Uri? = null
    lateinit var v: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceHelper(context!!)
        userId = pref.getId()!!
        mViewModel.token = pref.getToken()!!
        mViewModel.userId = userId
        mViewModel.initHomeInterface(this)
        mViewModel.loadPosts()
        dataBinding.homeProgressBar.visibility = VISIBLE
        commentsadapter =
            CommentAdapter(
                comments,
                userId
            )
        adapter =
            PostsAdapter(
                posts,
                userId,
                commentsadapter
            )
//        mViewModel.comments.observe(this, Observer {
//            commentsadapter.comments = it
//            adapter.commentsAdapter = commentsadapter
//            commentsadapter.notifyDataSetChanged()
//        })
        val userImageOption = RequestOptions().placeholder(R.drawable.user_default_grey)
            .error(R.drawable.user_default_grey)
        Glide.with(this).load(pref.getImage()).apply(userImageOption)
            .into(dataBinding.activityProfileImage)
        mViewModel.posts.observe(this, Observer {
            adapter.posts = it
            activities_recycle.adapter = adapter
            for (i in it) {
                mViewModel.getLikes(i.id!!)
            }
            adapter.showUserProfile = object : ShowUserProfile {
                override fun showProfile(userId: String, image: String) {
                    val bundle = bundleOf("id" to userId, "image" to image)
                    navigation.navigate(R.id.action_home_to_profile, bundle)
                }

            }
            adapter.getAllComments = object : GetAllComments {
                override fun navtoPostDetailsFragment(postId: String) {
                    partItemClicked(postId)
                }

                override fun getAllComments(postId: String) {
                    mViewModel.getComments(postId)
                }

            }
            adapter.onSendComment = object : OnSendComment {
                override fun onSend(postId: String, userId: String, comment: String) {
                    mViewModel.sendComment(userId, postId, comment)
                }

            }
            adapter.onSendLike = object : OnSendLike {
                override fun onSendLike(userId: String, postId: String) {
                    mViewModel.sendLike(userId, postId)
                }

            }

            adapter.commentsAdapter.onItemClicked = object : OnItemClicked {
                override fun onClick(postId: String, commentId: String) {
                    mViewModel.deleteComment(commentId, postId)

                }

            }
            dataBinding.homeProgressBar.visibility = GONE
        })
        mViewModel.likes.observe(this, Observer {
            adapter.likes = it
        })
        activity!!.toolbar_title.text = "Home"
        activity!!.toolbar_icon.setImageResource(R.drawable.home)
        dataBinding.addImage.setOnClickListener {
            requestStoragePermission()
        }
        dataBinding.addFile.setOnClickListener {
            launchFiles()
        }
        dataBinding.deleteSelectedImage.setOnClickListener {
            dataBinding.selectedImage.visibility = GONE
            dataBinding.deleteSelectedImage.visibility = GONE
            imagePath = null
            mViewModel.imagePath = ""
        }
        dataBinding.homeContainer.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val vv = activity?.currentFocus
                val imm =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(p0!!.windowToken, 0)
            }
        })

        dataBinding.homeRefreshIcon.setOnClickListener {
            val ft = getFragmentManager()!!.beginTransaction()
            ft.detach(this).attach(this).commit()
        }
        checkConnectivity()


    }

    private fun launchFiles() {
        val intent = Intent()
        intent.type = "application/*"
        val mimeTypes =
            arrayOf("application/pdf", "application/ms-word", "application/vnd.ms-powerpoint")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select File"),
            PICK_File_REQUEST
        )
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST
        )


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null)
                return
            imagePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imagePath)
                mViewModel.imagePath = getRealPathFromURI_API19(context!!, imagePath!!)
                dataBinding.selectedImage.setImageBitmap(bitmap)
                dataBinding.selectedImage.visibility = VISIBLE
                dataBinding.deleteSelectedImage.visibility = VISIBLE


            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == PICK_File_REQUEST && resultCode == Activity.RESULT_OK) {

            if (data == null || data.data == null)
                return
            mViewModel.imagePath=data.data!!.toString()
            dataBinding.deleteSelectedImage.visibility = VISIBLE
            dataBinding.selectedImage.visibility = VISIBLE
            dataBinding.selectedImage.setImageResource(R.drawable.icons_file)

        }
    }

    fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            launchGallery()
            return
        }
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                launchGallery()
            }
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }

    override fun onPublishSuccess(a: String, path: String?) {
        Toasty.success(
            context!!,
            "Published",
            Toasty.LENGTH_LONG
        ).show()
        clearText()
        mViewModel.loadPosts()
    }

    private fun clearText() {
        dataBinding.activityText.setText("")
        val vv = activity?.currentFocus
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(vv!!.windowToken, 0)
        dataBinding.selectedImage.visibility = GONE
        dataBinding.deleteSelectedImage.visibility = GONE
        imagePath = null
        mViewModel.imagePath = ""
    }


    override fun onPublishFailed() {

    }

    fun getRealPathFromURI_API19(context: Context, uri: Uri): String {
        var filePath = "";
        val wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        var id = wholeID.split(":")[1];

        var column = arrayOf(MediaStore.Images.Media.DATA)

        // where id is equal to
        var sel = MediaStore.Images.Media._ID + "=?";

        var cursor = context.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null
        );

        val columnIndex = cursor!!.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    fun partItemClicked(id: String) {
        val bundle = bundleOf("id" to id)
        navigation.navigate(R.id.action_home_to_postDetails, bundle)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            0 -> {
                mViewModel.deletePost(adapter.posts[item.groupId].id!!)
//                adapter.deletePost(item.groupId)
            }
//            1 -> {
//                adapter.postHolder.commentAdapter.onItemClicked = object : OnItemClicked {
//                    override fun onClick(postId: String, commentId: String) {
//                        mViewModel.deleteComment(commentId, postId)
//                    }
//
//                }
//            }
        }
        return super.onContextItemSelected(item)
    }

    fun checkConnectivity() {
        val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activityNetwork = cm.activeNetworkInfo
        val isConnected = activityNetwork != null && activityNetwork.isConnectedOrConnecting
        if (!isConnected) {
            dataBinding.homeProgressBar.visibility = GONE
            dataBinding.homeRefreshIcon.visibility = VISIBLE
            dataBinding.homeConnectionMessage.visibility = VISIBLE
        }
    }

    override fun onDeletePostSuccess() {
        Toasty.success(context!!, "Post Deleted", Toasty.LENGTH_SHORT).show()
        mViewModel.loadPosts()
    }

}
