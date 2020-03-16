package com.example.android.activitiesproject.ui.notification


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.databinding.FragmentNotificationBinding
import kotlinx.android.synthetic.main.app_bar_main.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NotificationFragment : Fragment() {
    lateinit var notificationBinding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        notificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        val v = notificationBinding.root
        val notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        notificationViewModel.loadNoifications()
        notificationBinding.vm = notificationViewModel
        activity!!.toolbar_title.text = "Notification"

        Log.e("asd",activity!!.supportFragmentManager.backStackEntryCount.toString())
        activity!!.toolbar_icon.setImageResource(R.drawable.notification)
//        if(activity!!.supportFragmentManager.backStackEntryCount>0)
//            activity!!.supportFragmentManager.popBackStack()
        return v
    }


}
