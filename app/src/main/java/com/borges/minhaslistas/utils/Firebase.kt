package com.borges.minhaslistas.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.*

class Firebase {
    val db = FirebaseFirestore.getInstance()
    val mAuth = FirebaseAuth.getInstance();

    fun updateItemListComprado(idList: String, idItem: String, comprado: Boolean ) {

        val item = hashMapOf<String, Any>()

        item["comprado"] = comprado == false

        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .collection("itens")
            .document(idItem)
            .update(item)
            .addOnSuccessListener {
                Log.d("UPDATE_COMPRADO", "OnSuccess Update:")
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                    e -> Log.w("UPDATE_COMPRADO", "OnFailure Update: ", e)
                return@addOnFailureListener
            }

    }

    fun updateItemList(nome: String, quantInt: Int, valorDouble: Double, multiply: Double, idList: String, idItem: String ) {

        val item = hashMapOf<String, Any>()

        item["nome"] = nome
        item["qt"] = quantInt
        item["preco"] = valorDouble
        item["total"] = multiply

        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .collection("itens")
            .document(idItem)
            .update(item)
            .addOnSuccessListener {
                Log.d("UPDATE_FIREBASE", "OnSuccess Update:")
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                    e -> Log.w("UPDATE_FIREBASE", "OnFailure Update: ", e)
                return@addOnFailureListener
            }

    }

    fun createItemList(nome: String, quantInt: Int, valorDouble: Double, multiply: Double, idList: String) {

        val comprado = false

        val item = hashMapOf<String, Any>()

        item["comprado"] = comprado
        item["nome"] = nome
        item["preco"] = valorDouble
        item["qt"] = quantInt
        item["total"] = multiply

        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .collection("itens")
            .add(item)
            .addOnSuccessListener {
                Log.d("UPDATE_FIREBASE", "OnSuccess Created id: ${it.id}")
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                    e -> Log.w("UPDATE_FIREBASE", "OnFailure Update: ", e)
                return@addOnFailureListener
            }
    }

    fun excluirItemList(idList: String, idItem: String) {
        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .collection("itens")
            .document(idItem)
            .delete()
            .addOnSuccessListener { Log.d("DELETE_FIREBASE_ITEM", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("DELETE_FIREBASE_ITEM", "Error deleting document", e) }
    }

    fun updateList(idList: String, nomeDaLista: String) {
        val item = hashMapOf<String, Any>()
        item["nomeDaLista"] = nomeDaLista

        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .update(item)
            .addOnSuccessListener {
                Log.d("UPDATE_FIREBASE_LISTA", "OnSuccess Update:")
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                    e -> Log.w("UPDATE_FIREBASE_LISTA", "OnFailure Update: ", e)
                return@addOnFailureListener
            }
    }

    fun createList(nomeDaLista: String?) {

        val data = Calendar.getInstance().time
        val brasil = Locale("pt", "BR")
        val f2: DateFormat = DateFormat.getDateInstance(DateFormat.DATE_FIELD, brasil)

        val item = hashMapOf<String, Any>()

        item["created"] = f2.format(data)
        item["nomeDaLista"] = nomeDaLista.toString()

        Log.i("NOTIFY_CREATE_FIREBASE", "CRIANDO NEWLIST")
        db.collection(mAuth.currentUser?.uid.toString())
            .add(item)
            .addOnSuccessListener {
                Log.d("CREATE_FIREBASE", "OnSuccess Created id: ${it.id}")
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                    e -> Log.w("CREATE_FIREBASE", "OnFailure Update: ", e)
                return@addOnFailureListener
            }


    }

    fun excluirList(idList: String) {
        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .delete()
            .addOnSuccessListener { Log.d("DELETE_FIREBASE_LISTA", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("DELETE_FIREBASE_LISTA", "Error deleting document", e) }
    }

}