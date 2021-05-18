package com.borges.minhaslistas.recycle

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.model.DataList
import kotlinx.android.synthetic.main.card_recycle_main.view.*

class MainAdapter(val listData: MutableList<DataList>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_main,
        parent, false)

        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = listData[position]

        holder.bind(currentItem, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(currentItem: DataList, position: Int) {
            with(currentItem){
                itemView.main_nomeDaLista.text = "${position+1}. $nomeDaLista"
                itemView.main_data.text = if(created != "") created else ""
            }
        }
    }

}

// https://www.youtube.com/watch?v=afl_i6uvvU0