package com.example.android.activitiesproject.di

import com.example.android.activitiesproject.ui.event.EventsFragment
import com.example.android.activitiesproject.ui.event.addevent.AddEventFragment
import com.example.android.activitiesproject.ui.eventdetails.EventDetailsFragment
import com.example.android.activitiesproject.ui.home.HomeFragment
import com.example.android.activitiesproject.ui.login.LoginActivity
import com.example.android.activitiesproject.ui.postdetails.PostDetailsFragment
import com.example.android.activitiesproject.ui.profile.ProfileFragment
import com.example.android.activitiesproject.ui.rank.RankFragment
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DaggerHelper::class, AndroidInjectionModule::class))
interface AppComponent {
    fun inject(loginActivity: LoginActivity)
    fun injectRankFragment(rankFragment: RankFragment)
    fun injectProfileFragment(profileFragment: ProfileFragment)
    fun injectEventsFragment(eventsFragment: EventsFragment)
    fun injectHomeFragment(homeFragment: HomeFragment)
    fun injectAddEventFragment(addEventFragment: AddEventFragment)
    fun injectEventDetailsFragment(eventDetailsFragment: EventDetailsFragment)
    fun injectPostDetailsFragment(postDetailsFragment: PostDetailsFragment)

//    fun injectBaseFragment(baseFragment: BaseFragment<ViewDataBinding,BaseViewModel>)
}