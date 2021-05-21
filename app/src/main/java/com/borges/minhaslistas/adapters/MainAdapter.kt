package com.borges.minhaslistas.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.models.DataList
import com.borges.minhaslistas.views.AddActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.card_recycle_main.view.*

class MainAdapter(val listData: MutableList<DataList>, val contextMain: Context, var dialog: DialogFragment) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


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

        fun bind(currentItem: DataList, position: Int) {
            with(currentItem){
                itemView.card_main_number.text = "${position + 1}. "
                itemView.main_nomeDaLista.text = "$nomeDaLista"
                itemView.main_data.text = if(created != "") created else ""
            }
        }

        fun excluir(currentItem: DataList, position: Int) {
            itemView.card_main.setOnLongClickListener {

                val ft = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val args = Bundle()

                args.putParcelable("currentItem", currentItem)
                dialog.arguments = args
                dialog.show(ft, "DialogEditList")

                return@setOnLongClickListener true
            }
        }

        fun goToItem(currentItem: DataList) {
            with(itemView){
                card_main.setOnClickListener {
                    val intent = Intent(context, AddActivity::class.java)
                    intent.putExtra("idItem", currentItem.idList)
                    context.startActivity(intent)
                }
            }
        }
    }

}

// https://www.youtube.com/watch?v=afl_i6uvvU0