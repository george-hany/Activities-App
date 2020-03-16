package com.example.android.activitiesproject.app

import android.app.Activity
import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.example.android.activitiesproject.di.AppComponent
import com.example.android.activitiesproject.di.DaggerAppComponent
import com.example.android.activitiesproject.ui.event.EventsFragment
import com.example.android.activitiesproject.ui.event.addevent.AddEventFragment
import com.example.android.activitiesproject.ui.eventdetails.EventDetailsFragment
import com.example.android.activitiesproject.ui.home.HomeFragment
import com.example.android.activitiesproject.ui.login.LoginActivity
import com.example.android.activitiesproject.ui.postdetails.PostDetailsFragment
import com.example.android.activitiesproject.ui.profile.ProfileFragment
import com.example.android.activitiesproject.ui.rank.RankFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MyApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

    companion object {
        lateinit var appComponent: AppComponent

        fun injectLoginActivity(loginActivity: LoginActivity) {
            appComponent.inject(loginActivity)
        }

        fun injectRankFragment(rankFragment: RankFragment) {
            appComponent.injectRankFragment(rankFragment)
        }
        fun injectProfileFragment(profileFragment: ProfileFragment){
            appComponent.injectProfileFragment(profileFragment)
        }

        fun injectEventsFragment(eventsFragment: EventsFragment){
            appComponent.injectEventsFragment(eventsFragment)
        }
        fun injectHomeFragment(homeFragment: HomeFragment){
            appComponent.injectHomeFragment(homeFragment)
        }
        fun injectAddEventFragment(addEventFragment: AddEventFragment){
            appComponent.injectAddEventFragment(addEventFragment)
        }
        fun injectEventDetailsFragment(eventDetailsFragment: EventDetailsFragment){
            appComponent.injectEventDetailsFragment(eventDetailsFragment)
        }
        fun injectPostDetailsFragment(postDetailsFragment: PostDetailsFragment){
            appComponent.injectPostDetailsFragment(postDetailsFragment)
        }
//        fun injectBaseFragment(baseFragment: BaseFragment<ViewDataBinding,BaseViewModel>){
//            appComponent.injectBaseFragment(baseFragment)
//        }
    }


    override fun onCreate() {
        super.onCreate()
        initDataComponent()
        AndroidNetworking.initialize(this)
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC)
    }

    private fun initDataComponent() {
        appComponent = DaggerAppComponent.builder()
            .build()
    }
}