package com.example.smartmoveiiui.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmoveiiui.data.model.Announcement
import com.example.smartmoveiiui.databinding.ItemAnnouncementBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AnnouncementAdapter(private val announcements: List<Announcement>) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    class AnnouncementViewHolder(val binding: ItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = announcements[position]
        holder.binding.tvAnnTitle.text = announcement.title
        holder.binding.tvAnnDescription.text = announcement.description

        val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
        val dateStr = announcement.publishDate?.let { sdf.format(it.toDate()) } ?: "N/A"
        holder.binding.tvAnnDate.text = dateStr
    }

    override fun getItemCount() = announcements.size
}
