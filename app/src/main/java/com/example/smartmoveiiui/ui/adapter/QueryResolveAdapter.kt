package com.example.smartmoveiiui.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmoveiiui.R
import com.example.smartmoveiiui.data.model.Query
import com.example.smartmoveiiui.databinding.ItemQueryResolveBinding

class QueryResolveAdapter(
    private val queries: List<Query>,
    private val onResolveClick: (Query) -> Unit
) : RecyclerView.Adapter<QueryResolveAdapter.QueryViewHolder>() {

    class QueryViewHolder(val binding: ItemQueryResolveBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val binding = ItemQueryResolveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QueryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        val query = queries[position]
        holder.binding.tvQueryCategory.text = query.category
        holder.binding.tvQueryDesc.text = query.description
        holder.binding.tvQueryStatus.text = query.status
        val context = holder.itemView.context
        holder.binding.tvQueryUser.text = context.getString(R.string.query_from, query.commuterId)
        
        holder.binding.btnReply.setOnClickListener {
            onResolveClick(query)
        }
    }

    override fun getItemCount() = queries.size
}
