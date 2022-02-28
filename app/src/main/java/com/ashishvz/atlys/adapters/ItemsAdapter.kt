package com.ashishvz.atlys.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ashishvz.atlys.R
import com.ashishvz.atlys.database.entities.Item
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class ItemsAdapter(private var itemList: MutableList<Item>) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: Item, index: Int) {
            val name = view.findViewById<TextInputEditText>(R.id.textInputItemNameEditText)
            val qty = view.findViewById<TextInputEditText>(R.id.textInputQtyEditText)
            val price = view.findViewById<TextInputEditText>(R.id.textInputPriceEditText)
            val total = view.findViewById<MaterialTextView>(R.id.totalTextView)
            val deleteBtn = view.findViewById<ImageView>(R.id.deleteInvoice)
            name.setText(item.name)
            qty.setText(if (item.quantity != null) item.quantity?.toDouble().toString() else "")
            price.setText(if (item.price != null) item.price.toString() else "")
            total.text = if (item.total != null) item.total.toString() else ""
            deleteBtn.setOnClickListener {
                itemList.removeAt(index)
                notifyItemRemoved(index)
            }
            name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null)
                        itemList[index].name = p0.toString()
                }

            })
            qty.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().isNotBlank())
                        itemList[index].quantity = p0.toString().toInt()
                }

            })
            price.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (qty.text!!.isNotBlank() && price.text!!.isNotBlank()) {
                        total.text = (price.text.toString().toDouble() * qty.text.toString()
                            .toDouble()).toString()
                    } else {
                        total.text = "0.0"
                    }
                }

            })
            total.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().isNotBlank() && price.text!!.isNotBlank()) {
                        itemList[index].price = price.text.toString().toDouble()
                        itemList[index].total = p0.toString().toDouble()
                    }
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(item: List<Item>) {
        itemList = item.toMutableList()
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<Item> {
        return itemList
    }
}