package com.example.smartmoveiiui.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmoveiiui.data.model.Schedule
import com.example.smartmoveiiui.databinding.ItemScheduleBinding

class ScheduleAdapter(private val schedules: List<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.binding.tvSchRouteName.text = "Route ID: ${schedule.routeId}"
        holder.binding.tvSchBusNumber.text = "Bus ID: ${schedule.busId}"
        
        val timings = StringBuilder("Stops & Timings:\n")
        schedule.stopTimings.forEach { (stopId, time) ->
            timings.append("$stopId: $time\n")
        }
        holder.binding.tvSchTimings.text = timings.toString()
    }

    override fun getItemCount() = schedules.size
}
