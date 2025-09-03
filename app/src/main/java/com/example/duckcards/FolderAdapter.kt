package com.example.duckcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FolderAdapter(
    private val onClick: (FolderItem) -> Unit,
    private val onLongClick: (FolderItem) -> Unit
) : ListAdapter<FolderItem, FolderAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FolderItem>() {
            override fun areItemsTheSame(oldItem: FolderItem, newItem: FolderItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: FolderItem, newItem: FolderItem) = oldItem == newItem
        }
    }

    fun submit(items: List<FolderItem>) = submitList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_folder_card, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, onLongClick)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tvFolderName)
        private val count: TextView = itemView.findViewById(R.id.tvFolderCount)

        fun bind(item: FolderItem, onClick: (FolderItem) -> Unit, onLongClick: (FolderItem) -> Unit) {
            name.text = item.name
            count.text = when (item.count) {
                1 -> "1 Karte"
                else -> "${item.count} Karten"
            }

            itemView.setOnClickListener { onClick(item) }
            itemView.setOnLongClickListener {
                onLongClick(item)
                true
            }
        }
    }
}
