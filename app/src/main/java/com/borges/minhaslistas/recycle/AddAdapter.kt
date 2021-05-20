package com.borges.minhaslistas.recycle

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.model.DataItem
import kotlinx.android.synthetic.main.card_recycle_add.view.*

class AddAdapter(val listData: MutableList<DataItem>, val context: Context) : RecyclerView.Adapter<AddAdapter.AddViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_add,
            parent, false)
        return AddViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        val currentItem = listData[position]

        holder.excluirItem(currentItem, position)
        holder.goToItem(currentItem)
        holder.bind(currentItem, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(currentItem: DataItem, position: Int) {
            with(currentItem){
                var icon = if(comprado == true) R.drawable.ic_circle_true else R.drawable.ic_circle_false

                itemView.text_view_name_item.text = nome
                itemView.text_view_quantidade_item.text = qt.toString()
                itemView.text_view_preco_item.text = preco?.format(2).toString()
                itemView.buttom_delete.setBackgroundResource(icon)
            }
        }

        fun excluirItem(currentItem: DataItem, position: Int) {
            itemView.buttom_delete.setOnClickListener {
                Toast.makeText(itemView.context, "Excluir Card: ${currentItem.nome}", Toast.LENGTH_SHORT).show()
            }
        }

        fun goToItem(currentItem: DataItem) {
            itemView.card_add_container.setOnClickListener {
                Toast.makeText(itemView.context, "Card: ${currentItem.nome}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}

