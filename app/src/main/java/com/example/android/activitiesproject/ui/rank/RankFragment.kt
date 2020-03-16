package com.example.android.activitiesproject.ui.rank


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.databinding.FragmentRankBinding
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import com.example.android.activitiesproject.ui.rank.adapter.RanksAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_rank.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class RankFragment : BaseFragment<FragmentRankBinding, RankViewModel>() {
    @Inject
    lateinit var mViewModel: RankViewModel
lateinit var pref:SharedPreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectRankFragment(this)
        super.viewModel = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref= SharedPreferenceHelper(context!!)
        mViewModel.token=pref.getToken()!!
        dataBinding.rankProgressBar.visibility = VISIBLE
        mViewModel.loadRanks()
        mViewModel.ranks.observe(this, Observer {
            ranks_recycle.adapter =
                RanksAdapter(
                    it!!,
                    { id: String, image: String? -> partItemClicked(id, image) })
            dataBinding.rankProgressBar.visibility = GONE
        })
        activity!!.toolbar_title.text = "Rank"
        activity!!.toolbar_icon.setImageResource(R.drawable.rank)

        Log.e("asd", activity!!.supportFragmentManager.backStackEntryCount.toString())
        dataBinding.rankRefreshIcon.setOnClickListener {
            val ft = getFragmentManager()!!.beginTransaction()
            ft.detach(this).attach(this).commit()
        }
        checkConnectivity()
    }

    fun partItemClicked(id: String, image: String?) {
        var bundel=Bundle()
        if (image == null) {
             bundel = bundleOf("id" to id, "image" to "")
        }
        else {
             bundel = bundleOf("id" to id, "image" to image)
        }
        navigation.navigate(R.id.action_rank_to_profile, bundel)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_rank
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }

    fun checkConnectivity() {
        val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activityNetwork = cm.activeNetworkInfo
        val isConnected = activityNetwork != null && activityNetwork.isConnectedOrConnecting
        if (!isConnected) {
            dataBinding.rankProgressBar.visibility = GONE
            dataBinding.rankRefreshIcon.visibility = VISIBLE
            dataBinding.rankConnectionMessage.visibility = VISIBLE
        }
    }

}

