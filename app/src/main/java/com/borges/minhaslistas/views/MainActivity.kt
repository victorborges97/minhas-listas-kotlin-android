package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.borges.minhaslistas.R
import com.borges.minhaslistas.adapters.MainAdapter
import com.borges.minhaslistas.dialogs.DialogAddList
import com.borges.minhaslistas.dialogs.DialogDuplicList
import com.borges.minhaslistas.dialogs.DialogEditList
import com.borges.minhaslistas.models.DataList
import com.borges.minhaslistas.utils.FirestoreRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var db: FirebaseFirestore
    private var nome: String? = ""
    private var id: String? = ""
    private var email: String? = ""
    private var url_photo: String? = null
    private var TAG = "MAIN"
    private lateinit var dialog: DialogFragment
    private lateinit var dialog3: DialogFragment
    private lateinit var newList: MutableList<DataList>
    private lateinit var firestoreRepository: FirestoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBackgroundActionBar()
        firestoreRepository = FirestoreRepository()
        dialog = DialogEditList()
        dialog3 = DialogDuplicList()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        db = FirebaseFirestore.getInstance()

        fab.setOnClickListener{
            val dialog2 = DialogAddList()
            val ft = supportFragmentManager.beginTransaction()
            dialog2.show(ft, "DialogAddList")
        }

        recycle_main.layoutManager = LinearLayoutManager(this)
        recycle_main.setHasFixedSize(true)

    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth.currentUser
        val nome = currentUser?.displayName.toString()

        if(nome != null && nome != "null") {
            text_bem.text = "Seja bem vindo, $nome"
        } else {
            text_bem.text = "Seja bem vindo"
        }
    }

    override fun onResume() {
        super.onResume()

        val currentUser: FirebaseUser? = mAuth.currentUser

        getListsData(currentUser?.uid.toString())

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sair -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setBackgroundActionBar() {
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.appPrimary)))
    }

    private fun signOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
            OnCompleteListener<Void?> { })
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        signOutGoogle()
        this.nome = ""
        this.email = ""
        this.id = ""
        this.url_photo = ""
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signOut() {
        logout()
    }

    private fun getListsData(id: String) {
        newList = mutableListOf<DataList>()

        val listsRef = FirebaseFirestore.getInstance()
        val docRef = listsRef
            .collection(id)
            .orderBy("timestamp", Query.Direction.ASCENDING)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if(snapshot?.isEmpty == true) {
                newList.clear()
                recycle_main.adapter?.notifyDataSetChanged()
                recycle_main.visibility = View.GONE;
                setTextVisible()
                Log.w(TAG, "Lista Vazia")
                return@addSnapshotListener
            }
            setTextInvisible()
            val recyclerViewState = recycle_main.layoutManager?.onSaveInstanceState()
            for (dc in snapshot!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        dc.document.toObject(DataList::class.java).let { entity ->
                            entity.idList = dc.document.id
                            Log.d(TAG, "New item: $entity")
                            newList.add(0, entity)
                            posGetLists()
                            recycle_main.adapter?.notifyDataSetChanged()
                            recycle_main.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        dc.document.toObject(DataList::class.java).let { entity ->
                            entity.idList = dc.document.id
                            val idxItem = findIndex(newList, dc.document.id)
                            newList[idxItem] = entity
                            Log.d(TAG,
                                "\nModified item: " +
                                        "\n${newList[idxItem]}")

                            posGetLists()
                            recycle_main.adapter?.notifyItemChanged(idxItem, null)
                            recycle_main.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        dc.document.toObject(DataList::class.java).let { entity ->
                            val idxItem = findIndex(newList, dc.document.id)
                            entity.idList = dc.document.id
                            Log.d(TAG, "Removed item: $entity")
                            newList.removeAt(idxItem)
                            posGetLists()
                            recycle_main.adapter?.notifyDataSetChanged()
                            recycle_main.layoutManager?.onRestoreInstanceState(recyclerViewState)
                        }
                    }
                }
            }
        }
    }

    private fun setTextInvisible() {
        //Tiro os texto pois a lista não está vazia
        recycle_main.visibility = View.VISIBLE;
        text_bem.visibility = View.INVISIBLE
        text_home.visibility = View.INVISIBLE
        text_facil.visibility = View.INVISIBLE
    }

    private fun setTextVisible() {
        //Tiro os texto pois a lista não está vazia
        text_bem.visibility = View.VISIBLE
        text_home.visibility = View.VISIBLE
        text_facil.visibility = View.VISIBLE
    }

    private fun posGetLists() {
        //Mandando a Lista Mutavel para o Adapter
        recycle_main.adapter = MainAdapter(newList, applicationContext, dialog, dialog3)
        Log.d(TAG, "Current data: ${newList.size}")
    }

    private fun findIndex(arr: MutableList<DataList>?, t: String): Int {
        if (arr == null) {
            return -1
        }
        var idx = -1
        for (i in arr.indices) {
            if (arr[i].idList == t) {
                idx = i
            }
        }
        return idx
    }
}



