package com.borges.minhaslistas.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.models.DataItem
import com.borges.minhaslistas.utils.Firebase
import kotlinx.android.synthetic.main.card_recycle_add.view.*
import java.text.NumberFormat
import java.util.*


class AddAdapter(
    val listData: MutableList<DataItem>,
    val context: Context,
    val dialog: DialogFragment
) : RecyclerView.Adapter<AddAdapter.AddViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_recycle_add,
            parent, false
        )
        return AddViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        val currentItem = listData[position]

        holder.mudarComprado(currentItem, position)
        holder.goToItem(currentItem)
        holder.bind(currentItem, position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun multiply(x: Double, y: Double) = x * y

        @SuppressLint("SetTextI18n")
        fun bind(currentItem: DataItem, position: Int) {
            with(currentItem){
                val meuLocal = Locale("pt", "BR")
                val icon = if(comprado == true) R.drawable.ic_circle_true else R.drawable.ic_circle_false
                val taxado = if(comprado == true) Paint.STRIKE_THRU_TEXT_FLAG else itemView.text_view_name_item.paintFlags
                val z: NumberFormat = NumberFormat.getCurrencyInstance(meuLocal)

                itemView.text_view_name_item.text = nome
                itemView.text_view_name_item.paintFlags = taxado
                itemView.text_view_quantidade_item.text = qt.toString()
                itemView.text_view_preco_item.text = z.format(preco).toString()
                itemView.buttom_delete2.setBackgroundResource(icon)
            }
        }

        fun mudarComprado(currentItem: DataItem?, position: Int) {
            val fb = Firebase()
            with(currentItem){
                itemView.buttom_delete.setOnClickListener {
                    this?.idList?.let { it1 ->
                        this.idItem?.let { it2 ->
                            this.comprado?.let { it3 ->
                                fb.updateItemListComprado(it1, it2, it3)
                            }
                        }
                    }

                }

                itemView.buttom_delete2.setOnClickListener {
                    this?.idList?.let { it1 ->
                        this.idItem?.let { it2 ->
                            this.comprado?.let { it3 ->
                                fb.updateItemListComprado(it1, it2, it3)
                            }
                        }
                    }

                }
            }

        }

        fun goToItem(currentItem: DataItem) {
            itemView.card_add_container.setOnClickListener {

                val ft = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val args = Bundle()

                args.putParcelable("currentItem", currentItem)
                dialog.arguments = args

                dialog.show(ft, "DialogAddList")
            }
        }
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}


