package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.borges.minhaslistas.R
import com.borges.minhaslistas.dialogs.DialogAddItem
import com.borges.minhaslistas.dialogs.DialogEditItem
import com.borges.minhaslistas.models.DataItem
import com.borges.minhaslistas.adapters.AddAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private val TAG: String = "ADD"
    private lateinit var idItem: String
    private lateinit var newItens: MutableList<DataItem>
    private var total: Double = 0.00
    private lateinit var dialog: DialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setBackgroundActionBar()

        dialog = DialogEditItem()

        val id = FirebaseAuth.getInstance().currentUser?.uid
        idItem = intent.extras?.getString("idItem").toString()

        fab_add.setOnClickListener {
            val dialog2 = DialogAddItem()
            val args = Bundle()
            args.putString("idList", idItem)
            dialog2.arguments = args
            dialog2.show(supportFragmentManager, "DialogAddItem")
        }

        recycle_view_add.layoutManager = LinearLayoutManager(this)
        recycle_view_add.setHasFixedSize(true)


        if(id != null){
            idItem.let {
                getListsData(id, it)
            }
        }

    }

    @SuppressLint("RestrictedApi")
    private fun setBackgroundActionBar() {
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.appPrimary)))
        this.supportActionBar?.title = "Meus Produtos"
    }

    fun signOut() {
    }

    @SuppressLint("SetTextI18n")
    private fun getListsData(id: String, idList: String) {
        newItens = mutableListOf<DataItem>()

        val listsRef = FirebaseFirestore.getInstance()
        val docRef = listsRef
            .collection(id)
            .document(idList)
            .collection("itens")

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.documents.size != 0) {
                //Limpo arquivos que estava antes da atualização em tempo real
                newItens.clear()
                recycle_view_add.adapter?.notifyDataSetChanged()
                total = 0.00
                //Adiciono os itens a um array mutavel da class List
                snapshot.documents.forEach {
                    it.toObject(DataItem::class.java).let { entity ->
                        entity?.idItem = it.id
                        entity?.idList = idList
                        if (entity != null) {
                            newItens.add(entity)
                        }
                    }
                }

                newItens.map {
                    if(it.comprado == true){
                        var qtf = it.qt?.toDouble()
                        var valor = it.preco?.toDouble()

                        if(qtf != null && valor != null) {
                            total += multiply(qtf, valor)
                        }
                    }
                }
                val meuLocal = Locale("pt", "BR")
                val z: NumberFormat = NumberFormat.getCurrencyInstance(meuLocal)
                nomeDaLista.text = "Total do carrinho: ${z.format(total).toString()}"

                //Mantando a Lista Mutavel para o Adapter
                recycle_view_add.adapter = AddAdapter(newItens, applicationContext, dialog)
                Log.d(TAG, "Current data: ${newItens.size}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun multiply(x: Double, y: Double) = x * y
}

