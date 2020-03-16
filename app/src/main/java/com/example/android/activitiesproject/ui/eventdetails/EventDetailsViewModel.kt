package com.example.android.activitiesproject.ui.eventdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.Event
import com.example.android.activitiesproject.data.network.model.EventResponse
import com.example.android.activitiesproject.base.BaseViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventDetailsViewModel constructor(var apihelper: ApiHelper) : BaseViewModel(apihelper) {
    var event = MutableLiveData<Event>()
    var token=""
    lateinit var eventDetailsInterface: EventDetailsInterface

    fun initEventDetailsInterface(i: EventDetailsInterface) {
        eventDetailsInterface = i
    }

    fun getEventDetails(id: String) {
        val observable = apihelper.getEventsDetails(id)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<EventResponse> {
                override fun onSubscribe(d: Disposable) {


                }

                override fun onError(e: Throwable) {
                    Log.e("erroo", e.toString())

                }

                override fun onNext(t: EventResponse) {
                    event.value = t!!.event
                    Log.e("erroo", event.value!!.title)

                }

                override fun onComplete() {
                }

            })
    }

    fun deleteEvent(eventId: String) {
        val observable = apihelper.deleteEvent(eventId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Void) {

                }

                override fun onError(e: Throwable) {
                    Log.e("erroo", e.toString())
                    eventDetailsInterface.onDeleteSuccess()
                }


                override fun onComplete() {
                    eventDetailsInterface.onDeleteSuccess()
                }

            })
    }

}