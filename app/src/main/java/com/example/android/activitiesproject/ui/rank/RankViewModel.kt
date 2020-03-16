package com.example.android.activitiesproject.ui.rank

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.Rank
import com.example.android.activitiesproject.data.network.model.RankResult
import com.example.android.activitiesproject.base.BaseViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RankViewModel @Inject constructor(var apihelper: ApiHelper) : BaseViewModel(apihelper) {

    var ranks = MutableLiveData<ArrayList<Rank>>()
    var token=""

    fun loadRanks() {
        var observable = apihelper.getAllRanks()
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<RankResult> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: RankResult) {
                    ranks.value = t!!.ranks
                }


                override fun onComplete() {

                }


                override fun onError(e: Throwable) {
                    Log.e("errorr", e.message)
                }
            })
    }
}