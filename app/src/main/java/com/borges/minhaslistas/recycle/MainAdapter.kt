package com.borges.minhaslistas.recycle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.model.DataList
import com.borges.minhaslistas.model.List
import com.borges.minhaslistas.views.AddActivity
import com.borges.minhaslistas.views.MainActivity
import kotlinx.android.synthetic.main.card_recycle_main.view.*

class MainAdapter(val listData: MutableList<List>, val contextMain: Context) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_main,
        parent, false)

        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = listData[position]

        holder.excluir(currentItem, position)
        holder.goToItem(currentItem)
        holder.bind(currentItem, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")

        fun bind(currentItem: List, position: Int) {
            with(currentItem){
                itemView.main_nomeDaLista.text = "${position+1}. ${getNome()}"
                itemView.main_data.text = if(getCreatedData() != "") getCreatedData() else ""
            }
        }

        fun excluir(currentItem: List, position: Int) {
            itemView.card_main.setOnLongClickListener {
                Toast.makeText(itemView.context, "Excluir Card: ${currentItem.getUid()}", Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener false
            }
        }

        fun goToItem(currentItem: List) {
            with(itemView){
                card_main.setOnClickListener {

                    val intent = Intent(context, AddActivity::class.java)
                    intent.putExtra("idCreated", currentItem.getUid())
                    context.startActivity(intent)

                    Toast.makeText(itemView.context, "Card: ${currentItem.getUid()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

// https://www.youtube.com/watch?v=afl_i6uvvU0