package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.borges.minhaslistas.R
import com.borges.minhaslistas.adapters.AddAdapter
import com.borges.minhaslistas.dialogs.DialogAddItem
import com.borges.minhaslistas.dialogs.DialogEditItem
import com.borges.minhaslistas.models.DataItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
        idItem = intent.extras?.getString("idItem").toString()
        dialog = DialogEditItem()

        fab_add.setOnClickListener {
            val dialog2 = DialogAddItem()
            val args = Bundle()
            args.putString("idList", idItem)
            dialog2.arguments = args
            dialog2.show(supportFragmentManager, "DialogAddItem")
        }

        recycle_view_add.layoutManager = LinearLayoutManager(this)
        recycle_view_add.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()
        val id = FirebaseAuth.getInstance().currentUser?.uid
        if(id != null){
            getListsData(id, idItem)
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
            .orderBy("nome", Query.Direction.ASCENDING)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Error ao carregar a lista. Error: ", e)
                return@addSnapshotListener
            }

            if(snapshot?.isEmpty == true) {
                newItens.clear()
                recycle_view_add.adapter?.notifyDataSetChanged()
                recycle_view_add.visibility = View.GONE;
                Log.w(TAG, "Lista Vazia")
                return@addSnapshotListener
            }

            recycle_view_add.visibility = View.VISIBLE
            val recyclerViewState = recycle_view_add.layoutManager?.onSaveInstanceState()

            for (dc in snapshot!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        dc.document.toObject(DataItem::class.java).let { entity ->
                            entity.idItem = dc.document.id
                            entity.idList = idList
                            Log.d(TAG, "New item: $entity")
                            newItens.add(entity)
                            posGetItem()
                            recycle_view_add.adapter?.notifyDataSetChanged()
                            recycle_view_add.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        }

                    }
                    DocumentChange.Type.MODIFIED -> {
                        dc.document.toObject(DataItem::class.java).let { entity ->
                            entity.idItem = dc.document.id
                            entity.idList = idList
                            val idxItem = findIndex(newItens, dc.document.id)
                            newItens[idxItem] = entity
                            Log.d(TAG,
                                "\nModified item: " +
                                    "\n${newItens[idxItem]}")

                            posGetItem()
                            recycle_view_add.adapter?.notifyItemChanged(idxItem, null)
                            recycle_view_add.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        dc.document.toObject(DataItem::class.java).let { entity ->
                            val idxItem = findIndex(newItens, dc.document.id)
                            entity.idItem = dc.document.id
                            entity.idList = idList
                            Log.d(TAG, "Removed item: $entity")
                            newItens.removeAt(idxItem)
                            posGetItem()
                            recycle_view_add.adapter?.notifyDataSetChanged()
                            recycle_view_add.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun posGetItem() {
        total = 0.00
        newItens.map {
            if (it.comprado == true) {
                val qtf = it.qt?.toDouble()
                val valor = it.preco?.toDouble()

                if (qtf != null && valor != null) {
                    total += multiply(qtf, valor)
                }
            }
        }
        val meuLocal = Locale("pt", "BR")
        val z: NumberFormat = NumberFormat.getCurrencyInstance(meuLocal)
        nomeDaLista.text = "Total do carrinho: ${z.format(total).toString()}"

        //Mantando a Lista Mutavel para o Adapter
        recycle_view_add.adapter = AddAdapter(newItens, applicationContext, dialog)
    }

    private fun findIndex(arr: MutableList<DataItem>?, t: String): Int {
        // if array is Null
        if (arr == null) {
            return -1
        }
        // traverse in the array
        var idx = -1
        for (i in arr.indices) {
            if (arr[i].idItem == t) {
                idx = i
            }
        }
        return idx
    }

    private fun multiply(x: Double, y: Double) = x * y
}

