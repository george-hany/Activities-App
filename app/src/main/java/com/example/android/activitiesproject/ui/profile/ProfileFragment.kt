package com.example.android.activitiesproject.ui.profile


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.databinding.EditPasswordDialogBinding
import com.example.android.activitiesproject.databinding.EditProfileDialogBinding
import com.example.android.activitiesproject.databinding.FragmentProfileBinding
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.edit_password_dialog.*
import kotlinx.android.synthetic.main.edit_profile_dialog.*
import kotlinx.android.synthetic.main.edit_profile_dialog.dialog_cancel_btn
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.activitiesproject.data.network.model.User
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import com.example.android.activitiesproject.ui.home.*
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.home.adapter.PostsAdapter
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.adapter.CommentAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(), ProfileInterface {


    @Inject
    lateinit var mViewModel: ProfileViewModel
    lateinit var pref: SharedPreferenceHelper
    lateinit var option: RequestOptions
    var currentImagePath: String? = ""
    val STORAGE_PERMISSION_CODE: Int = 2342
    var imagePath: Uri? = null
    lateinit var v: View
    var userGustId = ""
    var userId = ""
    lateinit var dialogBinding: EditPasswordDialogBinding
    lateinit var dialogProfileBinding: EditProfileDialogBinding
    lateinit var dialogPassword: Dialog
    lateinit var dialogInfo: Dialog
    var posts = ArrayList<Post>()
    lateinit var adapter: PostsAdapter
    lateinit var commentsadapter: CommentAdapter
    var comments = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectProfileFragment(this)
        super.viewModel = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferenceHelper(context!!)
        mViewModel.token = pref.getToken()!!
        userId = pref.getId()!!
        mViewModel.userId=userId
//        userId="b5b622e5-10ce-42d1-bc8c-0b3362720ca0"
        activity!!.toolbar_title.text = "My profile"
        activity!!.toolbar_icon.setImageResource(R.drawable.profile)
//        mViewModel.user=null

        mViewModel.initProfileInterface(this)
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
        setHasOptionsMenu(true)

        dialogPassword = Dialog(context!!)
        dialogPassword.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialogInfo = Dialog(context!!)
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE)
        option = RequestOptions()
            .placeholder(R.drawable.user_default_grey)
            .error(R.drawable.user_default_grey)
        mViewModel.user.observe(this, Observer {
            currentImagePath = it.image
            val rank = it.rank
            if (rank > 3) {
                dataBinding.userLikesOrderImage.visibility = View.GONE
                dataBinding.userLikesOrderText.visibility = View.VISIBLE
                dataBinding.userLikesOrderText.setText("#$rank")
            } else {
                dataBinding.userLikesOrderImage.visibility = View.VISIBLE
                dataBinding.userLikesOrderText.visibility = View.GONE
            }
            when (rank) {
                1 -> {
                    dataBinding.userLikesOrderImage.setImageResource(R.drawable.first)
                }
                2 -> {
                    dataBinding.userLikesOrderImage.setImageResource(R.drawable.second)
                }
                3 -> {
                    dataBinding.userLikesOrderImage.setImageResource(R.drawable.third)
                }
            }
            option = RequestOptions()
                .placeholder(R.drawable.user_default_grey)
                .error(R.drawable.user_default_grey)
            Glide.with(activity!!).load(it.image).apply(option).into(dataBinding.profileImage)
            dataBinding.userName.text = it.name
            dataBinding.userTeam.text = it.team
            dataBinding.userLikes.text = it.totallikes.toString() + ""
            if (arguments != null && userId != userGustId) {
                activity!!.toolbar_title.text = it.name
                dataBinding.txtActivities.text = "Activities"

            }

        })
        mViewModel.userName.observe(this, Observer {
            dataBinding.userName.text = it
            if (arguments != null && userId != userGustId) {
                activity!!.toolbar_title.text = it
                dataBinding.txtActivities.text = "Activities"

            }
        })
        mViewModel.userTeam.observe(this, Observer { dataBinding.userTeam.text = it })
        mViewModel.totalLikes.observe(this, Observer { dataBinding.userLikes.text = it.toString() })
        mViewModel.likesRank.observe(this, Observer {
            val rank = it
            if (rank > 3) {
                dataBinding.userLikesOrderImage.visibility = View.GONE
                dataBinding.userLikesOrderText.visibility = View.VISIBLE
                dataBinding.userLikesOrderText.setText("#$rank")
            } else {
                dataBinding.userLikesOrderImage.visibility = View.VISIBLE
                dataBinding.userLikesOrderText.visibility = View.GONE
            }
            when (rank) {
                1 -> {
                    dataBinding.userLikesOrderImage.setImageResource(R.drawable.first)
                }
                2 -> {
                    dataBinding.userLikesOrderImage.setImageResource(R.drawable.second)
                }
                3 -> {
                    dataBinding.userLikesOrderImage.setImageResource(R.drawable.third)
                }
            }
        })
//        mViewModel.userImagePath.observe(this, Observer {
//            currentImagePath = it!!
//            Glide.with(activity!!).load(it).apply(option).into(dataBinding.profileImage)
//
//        })
        if (arguments != null) {
            userGustId = arguments!!.getString("id")!!
            val image = arguments!!.getString("image")!!
            if (userId!=userGustId)
                setHasOptionsMenu(false)
            if (image == "")
                Glide.with(activity!!).load(R.drawable.user_default_grey).apply(option).into(
                    dataBinding.profileImage
                )
            else
                Glide.with(activity!!).load(image).apply(option).into(dataBinding.profileImage)
            mViewModel.LoadProfileData(userGustId)
            mViewModel.loadUserPosts(userGustId)

//            Toast.makeText(context, id, Toast.LENGTH_LONG).show()
        } else {
            mViewModel.LoadProfileData(userId)
            mViewModel.loadUserPosts(userId)


        }
        dataBinding.profileProgressBar.visibility = VISIBLE


        mViewModel.userPosts.observe(this, Observer {
            //            dataBinding.activitiesRecycle.adapter=PostsAdapter(it,id,{id:String,type:String->partItemClicked(id,type)})
            dataBinding.profileProgressBar.visibility = GONE
            adapter.posts = it
            activities_recycle.adapter = adapter
            adapter.getAllComments = object : GetAllComments {
                override fun navtoPostDetailsFragment(postId: String) {
                    partItemClicked(postId, "")
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
            adapter.showUserProfile = object : ShowUserProfile {
                override fun showProfile(userId: String, image: String) {

                }

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_profile_info -> {
                showProfileInfoDialog()
                return true
            }
            R.id.action_password -> {
                showPasswordDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showProfileInfoDialog() {
        Toast.makeText(context, mViewModel.userName.value, Toast.LENGTH_LONG).show()
        mViewModel.imagePath = ""
        mViewModel.dialogUserName = mViewModel.userName.value!!
        dialogInfo.setCancelable(true)
        dialogProfileBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.edit_profile_dialog,
                null,
                false
            )
        dialogProfileBinding.vm = mViewModel

        dialogInfo.setContentView(dialogProfileBinding.root)
        val dialog_profile_image = dialogInfo.dialog_profile_image
        dialog_profile_image.setImageResource(R.drawable.user_profile)
        val dialog_cancel_btn = dialogInfo.dialog_cancel_btn
        Glide.with(activity!!).load(mViewModel.userImagePath.value).apply(option)
            .into(dialog_profile_image)
        dialogProfileBinding.dialogUserName.setText(mViewModel.user.value!!.name)
        dialog_cancel_btn.setOnClickListener { dialogInfo.dismiss() }
        dialog_profile_image.setOnClickListener {
            requestStoragePermission()
        }
        dialogInfo.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogInfo.show()

    }

    fun showPasswordDialog() {
        dialogPassword.setCancelable(true)
        dialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.edit_password_dialog,
                null,
                false
            )
        dialogBinding.vm = mViewModel
        dialogPassword.setContentView(dialogBinding.root)
        val cancel_btn = dialogPassword.dialog_cancel_password_btn
        cancel_btn.setOnClickListener {
            dialogPassword.dismiss()
            dialogPassword.dialog_current_password.setText("")
            dialogPassword.dialog_new_password.setText("")
            dialogPassword.dialog_confirm_password.setText("")

        }
        dialogPassword.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogPassword.show()
    }

    override fun userNameError() {
        dialogInfo.dialog_user_name.error = "user name is empty"
        dialogInfo.dialog_user_name.requestFocus()
        showKeyBoard()
    }

    override fun currentPasswordError(msg: String) {
        dialogPassword.current_password_container.error = msg
        dialogPassword.dialog_current_password.requestFocus()
        showKeyBoard()

    }

    override fun newPasswordError(msg: String) {
        dialogPassword.current_password_container.error = null
        dialogPassword.new_password_container.error = msg
        dialogPassword.dialog_new_password.requestFocus()
        showKeyBoard()

    }

    override fun confirmPasswordError() {
        dialogPassword.new_password_container.error = null
        dialogPassword.confirm_password_container.error = "confirm password is empty"
        dialogPassword.dialog_confirm_password.requestFocus()
        showKeyBoard()

    }

    override fun matchPasswordsError() {
        dialogPassword.new_password_container.error = "passwords does not match"
        dialogPassword.confirm_password_container.error = "passwords does not match"
        showKeyBoard()

    }

    override fun changePasswordSuccess() {
        dialogPassword.new_password_container.error = null
        dialogPassword.confirm_password_container.error = null
        Toasty.success(context!!, "Saved", Toasty.LENGTH_LONG).show()
        dialogPassword.dismiss()
    }

    override fun changeInfoSuccess() {
        Toasty.success(context!!, "Saved", Toasty.LENGTH_LONG).show()
        dialogInfo.dismiss()
    }

    fun showKeyBoard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
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

    override fun updateProfileInfo(user: User) {
        val rank = user.rank
        if (rank > 3) {
            dataBinding.userLikesOrderImage.visibility = View.GONE
            dataBinding.userLikesOrderText.visibility = View.VISIBLE
            dataBinding.userLikesOrderText.setText("#$rank")
        } else {
            dataBinding.userLikesOrderImage.visibility = View.VISIBLE
            dataBinding.userLikesOrderText.visibility = View.GONE
        }
        when (rank) {
            1 -> {
                dataBinding.userLikesOrderImage.setImageResource(R.drawable.first)
            }
            2 -> {
                dataBinding.userLikesOrderImage.setImageResource(R.drawable.second)
            }
            3 -> {
                dataBinding.userLikesOrderImage.setImageResource(R.drawable.third)
            }
        }
        option = RequestOptions()
            .placeholder(R.drawable.user_default_grey)
            .error(R.drawable.user_default_grey)
        Glide.with(activity!!).load(user.image).apply(option).into(dataBinding.profileImage)
        dataBinding.userLikes.text = user.totallikes.toString() + ""
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

                dialogInfo.dialog_profile_image.setImageBitmap(bitmap)
//                var file = File(imagePath.toString())
//                Log.e("file", file.name)
//                var x = getRealPathFromURI(context!!, imagePath!!)!!
                Log.e("erroopath", imagePath.toString() + "")
                var z = getRealPathFromURI_API19(context!!, imagePath!!)
                mViewModel.imagePath = z
                Log.e("erroopath", z + "")

//                var x = getPath(imagePath)
//                Log.e("erroopath", x + "")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null;
        try {
            var proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (e: Exception) {
            Log.e("Error file", "getRealPathFromURI Exception : " + e.toString());
            return ""
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            context!!.getContentResolver().query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    public fun getRealPathFromURI2(context: Context, uri: Uri): String? {
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null;

        val cursorLoader = CursorLoader(
            context,
            uri, proj, null, null, null
        );
        val cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            val column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri): String {
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        var column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    fun getRealPathFromURI3(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null;
        try {
            var proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            var column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor!!.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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

    fun partItemClicked(id: String, type: String) {
        val bundle = bundleOf("id" to id)
        navigation.navigate(R.id.action_profile_to_postDetails, bundle)
    }


}
