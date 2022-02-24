package com.ashishvz.atlys.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashishvz.atlys.R
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.database.entities.Item
import com.ashishvz.atlys.databinding.ItemDetailsBinding

class ItemListAdapter :
    ListAdapter<Item, ItemListAdapter.ItemsViewHolder>(ItemDiffCallBack()) {

    class ItemsViewHolder(
        private val binding: ItemDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Item) {
            with(binding) {
                item = items
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_details,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ItemDiffCallBack : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

}
