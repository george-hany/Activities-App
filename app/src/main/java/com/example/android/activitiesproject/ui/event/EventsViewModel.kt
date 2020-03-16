package com.example.android.activitiesproject.ui.event

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.Event
import com.example.android.activitiesproject.data.network.model.EventResult
import com.example.android.activitiesproject.base.BaseViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EventsViewModel @Inject constructor(var apihelper: ApiHelper) : BaseViewModel(apihelper) {

    var events = MutableLiveData<ArrayList<Event>>()
    var token=""

    fun loadEvents() {
        var observable = apihelper.getAllEvents()

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<EventResult> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Log.e("erroo", e.toString())
                }

                override fun onNext(t: EventResult) {
                    events.value = t!!.events!!
                }

                override fun onComplete() {
                }

            })

    }
}