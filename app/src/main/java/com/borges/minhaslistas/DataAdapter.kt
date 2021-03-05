package com.borges.minhaslistas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class DataAdapter : RecyclerView.Adapter<DataAdapter.DataAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_add,
        parent, false)

        return DataAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text_view_name_item)
        val quantidade: TextInputEditText = itemView.findViewById(R.id.text_view_quantidade_item)
        val preco: TextInputEditText = itemView.findViewById(R.id.text_view_preco_item)
    }

}

// https://www.youtube.com/watch?v=afl_i6uvvU0