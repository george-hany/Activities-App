package com.example.android.activitiesproject.ui.eventdetails


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer

import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.databinding.FragmentEventDetailsBinding
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import es.dmoral.toasty.Toasty
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
class EventDetailsFragment : BaseFragment<FragmentEventDetailsBinding, EventDetailsViewModel>(),
    EventDetailsInterface {


    @Inject
    lateinit var mViewModel: EventDetailsViewModel
    var userId: String = ""
    var eventUserId=""
    lateinit var eventId: String
    lateinit var date: String
    lateinit var pref:SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectEventDetailsFragment(this)
        super.viewModel = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
//        setHasOptionsMenu(true)
        pref= SharedPreferenceHelper(context!!)
        userId=pref.getId()!!
        eventId = arguments!!.getString("id")!!
        activity!!.toolbar_title.text = "Details"
        activity!!.toolbar_icon.setImageResource(R.drawable.event_icon)
        mViewModel.initEventDetailsInterface(this)
        mViewModel.event.observe(this, Observer {
            dataBinding.eventTitle.text = it.title
            dataBinding.eventDescription.text = it.description
            date = it.date!!.substring(0, 10)
            dataBinding.eventDate.text = date
            dataBinding.eventStartTime.text = it.start
            dataBinding.eventEndTime.text = it.end
            eventUserId = it.userid
            if (userId == eventUserId) {
                setHasOptionsMenu(true)
            }

        })
        mViewModel.getEventDetails(eventId)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.event_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_delete -> {
                mViewModel.deleteEvent(eventId)
            }
            R.id.action_edit -> {
                val action = EventDetailsFragmentDirections.actionEventDetailsToAddEvent(
                    "update",
                    mViewModel.event.value!!.title,
                    mViewModel.event.value!!.description,
                    date,
                    mViewModel.event.value!!.start,
                    mViewModel.event.value!!.end,
                    mViewModel.event.value!!.eventid
                )
                navigation.navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun layoutId(): Int {
        return R.layout.fragment_event_details
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }

    override fun onDeleteSuccess() {
        navigation.navigate(R.id.action_eventDetails_to_events)
        Toasty.success(context!!, "Event Deleted", Toasty.LENGTH_SHORT).show()
    }

    override fun onDeleteFailed() {
        Toasty.error(context!!, "error", Toasty.LENGTH_SHORT).show()
    }

}
