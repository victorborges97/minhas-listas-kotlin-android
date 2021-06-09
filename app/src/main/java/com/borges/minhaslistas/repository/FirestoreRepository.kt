package com.borges.minhaslistas.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.borges.minhaslistas.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val TAG = "FIREBASE_CLASSE"
    private var googleSignClient : GoogleSignInClient? = null

    fun requestSignInOptions(c: Context): GoogleSignInClient? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(c.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(c, gso)
        return googleSignClient
    }

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

    fun createItemList(nome: String, quantInt: Int, valorDouble: Double, multiply: Double, idList: String, now: Timestamp) {
        val comprado = false
        val item = hashMapOf<String, Any>()

        item["comprado"] = comprado
        item["nome"] = nome.toLowerCase(Locale.ROOT)
        item["preco"] = valorDouble
        item["qt"] = quantInt
        item["total"] = multiply
        item["timestamp"] = now

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

    fun updateList(idList: String, nomeDaLista: String, mercado: String) {
        val item = hashMapOf<String, Any>()
        item["nomeDaLista"] = nomeDaLista
        item["nomeDoMercado"] = mercado

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

    fun createList(nomeDaLista: String?, mercado: String?) {
        val item = hashMapOf<String, Any>()

        item["nomeDaLista"] = nomeDaLista.toString()
        item["nomeDoMercado"] = mercado.toString()
        item["timestamp"] = Timestamp.now()

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

    fun excluirList(idList: String, context: Context) {
        db.collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Lista excluida com sucesso!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e -> Log.w("DELETE_FIREBASE_LISTA", "Error deleting document", e) }
    }

    fun dupliqueList(idList: String, nomeNovo: String, mercadoNovo: String) {
        val list = hashMapOf<String, Any>()

        list["nomeDaLista"] = nomeNovo.toString()
        list["nomeDoMercado"] = mercadoNovo.toString()
        list["timestamp"] = Timestamp.now()

        //Criar a lista nova e salvo o id
        db.collection(mAuth.currentUser?.uid.toString())
            .add(list)
            .addOnSuccessListener {
                //Pegar os itens da lista atual e criar os itens novos com preços zerados
                db.collection(mAuth.currentUser?.uid.toString())
                    .document(idList)
                    .collection("itens")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            createItemList(
                                document.get("nome").toString(),
                                0,
                                0.00,
                                0.00,
                                it.id,
                                document.get("timestamp") as Timestamp
                            )
                        }
                    }
                    .addOnFailureListener {
                        Log.w("CREATE_NEW_LIST/ITEM", "OnFailure CreateItem: ", it)
                        return@addOnFailureListener
                    }

            }
            .addOnFailureListener { e ->
                Log.w("CREATE_NEW_LIST", "OnFailure Update: ", e)
                return@addOnFailureListener
            }

    }

    fun getListShared(id: String, idList: String): Task<QuerySnapshot> {
        val listsRef = FirebaseFirestore.getInstance()

        return listsRef
            .collection(id)
            .document(idList)
            .collection("itens")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get();
    }

    fun getListsItem(idList: String): Query {
        val listsRef = FirebaseFirestore.getInstance()
        return listsRef
            .collection(mAuth.currentUser?.uid.toString())
            .document(idList)
            .collection("itens")
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    fun getLists(): Query {
        val listsRef = FirebaseFirestore.getInstance()

        return listsRef
            .collection(mAuth.currentUser?.uid.toString())
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

}