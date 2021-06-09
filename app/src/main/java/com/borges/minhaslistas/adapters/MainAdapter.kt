package com.borges.minhaslistas.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.models.DataItem
import com.borges.minhaslistas.models.DataList
import com.borges.minhaslistas.repository.FirestoreRepository
import com.borges.minhaslistas.utils.Utils
import com.borges.minhaslistas.views.AddActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.card_recycle_main.view.*
import java.util.*


class MainAdapter(
    private val listData: MutableList<DataList>,
    val contextMain: Context,
    var dialog: DialogFragment,
    var dialog3: DialogFragment
)
    : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_recycle_main,
            parent, false
        )

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
                itemView.card_main_number.text = "${position + 1}. "
                itemView.main_nomeDaLista.text = if(nomeDaLista != null && nomeDaLista != "") "$nomeDaLista" else "Lista sem nome"
                itemView.main_data.getShortDate(timestamp)
                itemView.card_main_textview_mercado.text = if(nomeDoMercado != null && nomeDoMercado != "") "Mercado: $nomeDoMercado" else "Mercado: "

                itemView.card_main_menu_list.setOnClickListener{
                    showPopupMenu(itemView.card_main_menu_list, position)
                }

                itemView.card_main.setOnLongClickListener {
                    editarDialog(currentItem, itemView)
                    return@setOnLongClickListener true
                }

                itemView.card_main.setOnClickListener {
                    goToItem(currentItem, itemView.context)
                }

            }
        }

        fun showPopupMenu(view: View, position: Int) {
            // inflate menu
            val popup = PopupMenu(view.context, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_list, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_list_duplic -> {
                        duplicarDialog(listData[position], itemView)
                        true
                    }
                    R.id.menu_list_edit -> {
                        editarDialog(listData[position], itemView)
                        true
                    }
                    R.id.menu_list_excluir -> {
                        modelExcluir(listData[position], itemView.context)
                        true
                    }
                    R.id.menu_list_shared -> {
                        shared(listData[position], itemView.context)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    fun TextView.getShortDate(ts: Timestamp?) {
        if(ts != null) {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.timeInMillis = ts.toDate().time
            this.text = android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString()
        } else {
            this.text = ""
        }
    }

    private fun editarDialog(currentItem: DataList, itemView: View) {
        val ft = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val args = Bundle()

        args.putParcelable("currentItem", currentItem)
        dialog.arguments = args
        dialog.show(ft, "DialogEditList")
    }

    private fun duplicarDialog(currentItem: DataList, itemView: View) {
        val ft = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        val args = Bundle()

        args.putParcelable("currentItem", currentItem)
        dialog3.arguments = args
        dialog3.show(ft, "DialogDuplicList")
    }

    private fun goToItem(currentItem: DataList, context: Context) {
        val intent = Intent(context, AddActivity::class.java)
        intent.putExtra("idItem", currentItem.idList)
        context.startActivity(intent)
    }

    private fun modelExcluir(currentItem: DataList, contextMain: Context) {
        //Cria o gerador do AlertDialog
        val builder = AlertDialog.Builder(contextMain)
        builder.setTitle("Tem certeza que quer excluir essa lista ?")
        builder.setPositiveButton("Sim") { _, _ ->
            FirestoreRepository().excluirList(currentItem.idList.toString(), contextMain)
        }
        builder.setNegativeButton("Não") { _, _ ->
            return@setNegativeButton
        }
        val alerta: AlertDialog = builder.create()
        alerta.show()
    }

    private fun shared(currentItem: DataList, c: Context) {
        FirestoreRepository()
            .getListShared(FirebaseAuth.getInstance().currentUser?.uid.toString(), currentItem.idList.toString())
            .addOnSuccessListener { itemList ->
                val newList = mutableListOf<DataItem>()
                for (dc in itemList!!.documentChanges) {
                    dc.document.toObject(DataItem::class.java).let { entity ->
                        newList.add(entity)
                    }
                }

                var total = 0.00
                newList.map {
                    if (it.comprado == true) {
                        val qtf = it.qt?.toDouble()
                        val valor = it.preco?.toDouble()

                        if (qtf != null && valor != null) {
                            total += Utils.multiply(qtf, valor)
                        }
                    }
                }
                Collections.sort(newList, Comparator<DataItem> { lhs, rhs ->
                    lhs.comprado!!.compareTo(rhs.comprado!!);
                })
                var itens: String = ""
                for(i in newList) {
                    itens += Utils.templateItens(i)
                }
                val mensagem: String = Utils.templateShared(currentItem, itens, total)

                Utils.sendShared(c, mensagem)

            }
    }
}
