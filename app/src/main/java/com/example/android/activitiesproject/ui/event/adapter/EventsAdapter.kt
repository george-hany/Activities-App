package com.example.android.activitiesproject.ui.event.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.activitiesproject.R
import com.example.android.activitiesproject.data.network.model.Event
import com.example.android.activitiesproject.databinding.EventItemBinding

class EventsAdapter(var events: ArrayList<Event>,val clickListener: (String) -> Unit) : RecyclerView.Adapter<EventsAdapter.EventsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsHolder {
        val v = LayoutInflater.from(parent.context)
        val binding: EventItemBinding = DataBindingUtil.inflate(v, R.layout.event_item, parent, false)
        val h =
            EventsHolder(
                binding
            )
        return h
    }

    override fun getItemCount(): Int = events.size


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventsHolder, index: Int) {
//        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        var date = LocalDate.parse(events.get(index).date, formatter)
        holder.event_date.setText(events.get(index).date!!.substring(0,10))
        holder.bind(events.get(index))
        holder.onClick(events.get(index).eventid!!,clickListener)

    }

    class EventsHolder(var v: EventItemBinding) : RecyclerView.ViewHolder(v.root) {
        val event_date=v.eventDate
        fun bind(event: Event) {
            v.event = event
        }
        fun onClick(id:String,clickListener: (String) -> Unit){
            itemView.setOnClickListener { clickListener(id) }
        }

    }
}