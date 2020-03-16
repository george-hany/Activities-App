package com.example.android.activitiesproject.di

import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.AppApiHelper
import com.example.android.activitiesproject.ui.event.EventsViewModel
import com.example.android.activitiesproject.ui.event.addevent.AddEventViewModel
import com.example.android.activitiesproject.ui.eventdetails.EventDetailsViewModel
import com.example.android.activitiesproject.ui.home.HomeViewModel
import com.example.android.activitiesproject.ui.postdetails.PostDetailsViewModel
import com.example.android.activitiesproject.ui.profile.ProfileViewModel
import com.example.android.activitiesproject.ui.rank.RankViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaggerHelper {

    @Provides
    @Singleton
    fun getApiHelper(): ApiHelper {
        return AppApiHelper()
    }

    @Singleton
    @Provides
    fun getRankViewModel(apiHelper: ApiHelper) = RankViewModel(apiHelper)

    @Singleton
    @Provides
    fun getProfileViewModel(apiHelper: ApiHelper) = ProfileViewModel(apiHelper)

    @Singleton
    @Provides
    fun getEventsViewModel(apiHelper: ApiHelper) = EventsViewModel(apiHelper)

    @Singleton
    @Provides
    fun getHomeViewModel(apiHelper: ApiHelper) = HomeViewModel(apiHelper)

    @Singleton
    @Provides
    fun getAddEventViewModel(apiHelper: ApiHelper) = AddEventViewModel(apiHelper)

    @Singleton
    @Provides
    fun getEventDetailsViewModel(apiHelper: ApiHelper) = EventDetailsViewModel(apiHelper)

    @Singleton
    @Provides
    fun getPostDetailsViewModel(apiHelper: ApiHelper) = PostDetailsViewModel(apiHelper)

}