package com.example.smartmoveiiui.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmoveiiui.R
import com.example.smartmoveiiui.data.model.Bus
import com.example.smartmoveiiui.databinding.ItemBusBinding

class BusAdapter(
    private val buses: List<Bus>,
    private val onEditClick: (Bus) -> Unit
) : RecyclerView.Adapter<BusAdapter.BusViewHolder>() {

    class BusViewHolder(val binding: ItemBusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        val binding = ItemBusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        val bus = buses[position]
        val context = holder.itemView.context
        holder.binding.tvBusNumber.text = context.getString(R.string.bus_item_number, bus.busNumber)
        holder.binding.tvBusStatus.text = context.getString(R.string.bus_item_status, bus.status, bus.capacity)
        
        holder.binding.btnEditBus.setOnClickListener {
            onEditClick(bus)
        }
    }

    override fun getItemCount() = buses.size
}
