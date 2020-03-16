package com.example.android.activitiesproject.base

import androidx.lifecycle.ViewModel
import com.example.android.activitiesproject.data.network.ApiHelper

open class BaseViewModel constructor(var apiHelper: ApiHelper):ViewModel() {
}