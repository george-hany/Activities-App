package com.example.android.activitiesproject.ui.event


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.databinding.FragmentEventsBinding
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import com.example.android.activitiesproject.ui.event.adapter.EventsAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EventsFragment : BaseFragment<FragmentEventsBinding, EventsViewModel>() {

    @Inject
    lateinit var mViewModel: EventsViewModel

    lateinit var pref:SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectEventsFragment(this)
        super.viewModel=mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        pref= SharedPreferenceHelper(context!!)
        mViewModel.token=pref.getToken()!!
        mViewModel.loadEvents()
        activity!!.toolbar_title.text = "Events"
        activity!!.toolbar_icon.setImageResource(R.drawable.event_icon)
        dataBinding.eventProgressBar.visibility=VISIBLE
        mViewModel.events.observe(this, Observer {
            val adapter=
                EventsAdapter(
                    it,
                    { id: String -> partItemClicked(id) })
            dataBinding!!.eventsRecycle.adapter=adapter
            dataBinding.eventProgressBar.visibility=GONE
        })

       dataBinding.addEventBtn.setOnClickListener {

            navigation.navigate(R.id.action_events_to_addEvent)


        }
        dataBinding.eventRefreshIcon.setOnClickListener {
            val ft = getFragmentManager()!!.beginTransaction()
            ft.detach(this).attach(this).commit()
        }
        checkConnectivity()

    }

    override fun layoutId(): Int {
        return R.layout.fragment_events
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }
    fun partItemClicked(id: String) {
        val bundel = bundleOf("id" to id )
        navigation.navigate(R.id.action_events_to_eventDetails, bundel)
    }
    fun checkConnectivity() {
        val cm = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activityNetwork = cm.activeNetworkInfo
        val isConnected = activityNetwork != null && activityNetwork.isConnectedOrConnecting
        if (!isConnected) {
            dataBinding.eventProgressBar.visibility = GONE
            dataBinding.eventRefreshIcon.visibility = VISIBLE
            dataBinding.eventConnectionMessage.visibility = VISIBLE
        }
    }
}
