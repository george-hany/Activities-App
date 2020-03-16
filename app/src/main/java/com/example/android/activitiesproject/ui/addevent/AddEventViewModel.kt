package com.example.android.activitiesproject.ui.event.addevent

import android.util.Log
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.Event
import com.example.android.activitiesproject.base.BaseViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

class AddEventViewModel @Inject constructor(var apihelper: ApiHelper) : BaseViewModel(apihelper) {
    var token =""
    lateinit var addEventInterface: AddEventInterface
    var title = ""
    var description = ""
    var day = ""
    var month = ""
    var year = ""

    var hour_start = ""
    var minute_start = ""
    var AMPM_start = "AM"
    var start_time = ""

    var hour_end = ""
    var minute_end = ""
    var AMPM_end = "AM"
    var end_time = ""
    var eventId = ""

    var isAddOperation = true

    var userId=""

    fun initAddEventInterface(addEventInterface: AddEventInterface) {
        this.addEventInterface = addEventInterface
    }

    fun saveEvent() {

        if (AMPM_start.equals("AM"))
            start_time = "$hour_start:$minute_start:00"
        else {
            val h = hour_start.toInt() + 12
            start_time = "$h:$minute_start:00"

        }
        if (AMPM_end.equals("AM"))
            end_time = "$hour_end:$minute_end:00"
        else {
            val h = hour_end.toInt() + 12
            end_time = "$h:$minute_end:00"

        }
        var date = "$year-$month-$day"
        val eventJSON = JSONObject()
        eventJSON.put("title", title)
        eventJSON.put("description", description)
        eventJSON.put("date", date)
        eventJSON.put("start", start_time)
        eventJSON.put("finish", end_time)
        eventJSON.put("userid", userId)
        if (isAddOperation) {
            val observable = apihelper.addEvent(eventJSON)
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Event> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        addEventInterface.onAddFailed()
                        Log.e("erroo", e.toString())
                    }

                    override fun onNext(t: Event) {

                    }

                    override fun onComplete() {
                        addEventInterface.onAddSuccess()
                    }

                })
        } else {
            eventJSON.put("id",eventId)
            val observable = apihelper.updateEvent( eventJSON)
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Event> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        addEventInterface.onAddFailed()
                        Log.e("erroo", e.toString())
                    }

                    override fun onNext(t: Event) {

                    }

                    override fun onComplete() {
                        addEventInterface.onAddSuccess()
                    }

                })
        }

    }

}