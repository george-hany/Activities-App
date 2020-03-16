package com.example.android.activitiesproject.ui.event.addevent


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.fragment.navArgs

import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.databinding.FragmentAddEventBinding
import com.example.android.activitiesproject.app.MyApplication
import com.example.android.activitiesproject.base.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEventFragment : BaseFragment<FragmentAddEventBinding, AddEventViewModel>(),
    AddEventInterface {

    var isAM_end = true
    var isAM_start = true
    val args: AddEventFragmentArgs by navArgs()
    var hour: Int = 0
    var minute = 0
    lateinit var pref:SharedPreferenceHelper
    var userId=""

    @Inject
    lateinit var mViewModel: AddEventViewModel

    var isAddOperation = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectAddEventFragment(this)
        super.viewModel = mViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        pref= SharedPreferenceHelper(context!!)
        userId=pref.getId()!!
        mViewModel.userId=userId
        mViewModel.token=pref.getToken()!!
        activity!!.toolbar_icon.setImageResource(R.drawable.event_icon)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        mViewModel.initAddEventInterface(this)
        if (args.type.equals("add")) {
            activity!!.toolbar_title.text = "Add Event"

        }
        else {
            activity!!.toolbar_title.text = "Edit Event"
            mViewModel.isAddOperation = false
            isAddOperation = false
            mViewModel.eventId=args.id
//            dataBinding.eventTitle.setText(args.title)
            mViewModel.title=args.title
//            dataBinding.eventDescription.setText(args.description)
            mViewModel.description=args.description
            Log.e("args",args.title+"0")
            Log.e("args",args.description+"0")
            val dateData = args.date.split("-")
            Log.e("date",args.date)
            Log.e("date",dateData.toString())
//            dataBinding.eventDateYear.setText(dateData.get(0))
//            dataBinding.eventDateMonth.setText(dateData.get(1))
//            dataBinding.eventDateDay.setText(dateData.get(2))
            mViewModel.year=dateData.get(0)
            mViewModel.month=dateData.get(1)
            mViewModel.day=dateData.get(2)
            var timeData = args.startTime.split(":")
            if (timeData.get(0).toInt() > 12) {
                isAM_start = false
                hour=timeData[0].toInt()-12
                dataBinding.eventStartDateAMPM.setText("PM")
                mViewModel.AMPM_start="PM"
            }
            else{
                hour=timeData[0].toInt()
            }
//            dataBinding.eventStartDateHours.setText(hour.toString())
//            dataBinding.eventStartDateMinutes.setText(timeData[1])
            mViewModel.hour_start=hour.toString()
            mViewModel.minute_start=timeData[1]
            timeData=args.endTime.split(":")
            if (timeData.get(0).toInt() > 12) {
                isAM_end = false
                hour=timeData[0].toInt()-12
                dataBinding.eventEndDateAMPM.setText("PM")
                mViewModel.AMPM_end="PM"
            }
            else{
                hour=timeData[0].toInt()
            }
//            dataBinding.eventEndDateHours.setText(hour.toString())
//            dataBinding.eventEndDateMinutes.setText(timeData[1])
            mViewModel.hour_end=hour.toString()
            mViewModel.minute_end=timeData[1]

        }
        dataBinding.eventDateIcon.setOnClickListener {
            val dateDialog = DatePickerDialog(
                context!!,
                R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    dataBinding.eventDateDay.setText(if (mDay.toString().length == 2) mDay.toString() else "0$mDay")
                    dataBinding.eventDateMonth.setText(if ((mMonth + 1).toString().length == 2) (mMonth + 1).toString() else "0${(mMonth + 1)}")
                    dataBinding.eventDateYear.setText(mYear.toString())
                },
                year,
                month,
                day
            )

            dateDialog.show()
        }
        dataBinding.eventEndDateAMPM.setOnClickListener {
            if (isAM_end) {
                dataBinding.eventEndDateAMPM.setText("PM")
                mViewModel.AMPM_end = "PM"
                isAM_end = false
            } else {
                dataBinding.eventEndDateAMPM.setText("AM")
                mViewModel.AMPM_end = "AM"
                isAM_end = true
            }


        }
        dataBinding.eventStartDateAMPM.setOnClickListener {
            if (isAM_start) {
                dataBinding.eventStartDateAMPM.setText("PM")
                mViewModel.AMPM_start = "PM"
                isAM_start = false
            } else {
                dataBinding.eventStartDateAMPM.setText("AM")
                mViewModel.AMPM_start = "AM"
                isAM_start = true
            }
        }

    }

    override fun layoutId(): Int {
        return R.layout.fragment_add_event
    }

    override fun bindingVariable(): Int {
        return BR.vm
    }

    override fun onAddSuccess() {
        if (isAddOperation) {
            Toasty.success(context!!, "added", Toasty.LENGTH_LONG).show()
        }
        else {
            Toasty.success(context!!, "updated", Toasty.LENGTH_LONG).show()
            navigation.navigate(R.id.action_addEvent_to_events)
        }
        dataBinding.eventTitle.setText("")
        dataBinding.eventDescription.setText("")
        dataBinding.eventDateMonth.setText("")
        dataBinding.eventDateDay.setText("")
        dataBinding.eventDateYear.setText("")
        dataBinding.eventEndDateHours.setText("")
        dataBinding.eventEndDateMinutes.setText("")
        dataBinding.eventStartDateHours.setText("")
        dataBinding.eventStartDateMinutes.setText("")
        dataBinding.eventStartDateAMPM.setText("AM")
        dataBinding.eventEndDateAMPM.setText("AM")
        mViewModel.AMPM_end = "AM"
        mViewModel.AMPM_start = "AM"
    }

    override fun onAddFailed() {
        Toasty.error(context!!, "error", Toasty.LENGTH_LONG).show()
    }

}
