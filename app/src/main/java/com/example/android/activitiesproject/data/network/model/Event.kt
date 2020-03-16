package com.example.android.activitiesproject.data.network.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Event(
    var eventid:String,
    var userid:String,
    var teamname:String,
    var title:String="t",
    var description:String="d",
    var date:String="da",
    var start:String="s",
    var end:String="e"
)
class EventResponse{
    @SerializedName("_event")
    lateinit var event:Event
}