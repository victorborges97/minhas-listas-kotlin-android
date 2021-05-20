package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.borges.minhaslistas.R
import com.borges.minhaslistas.dialog.DialogAddList
import com.borges.minhaslistas.dialog.DialogEditList
import com.borges.minhaslistas.model.DataList
import com.borges.minhaslistas.recycle.MainAdapter
import com.borges.minhaslistas.utils.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.String
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var db: FirebaseFirestore
    private var nome: kotlin.String? = ""
    private var id: kotlin.String? = ""
    private var email: kotlin.String? = ""
    private var url_photo: kotlin.String? = null
    private var TAG = "MAIN"
    private lateinit var dialog: DialogFragment
    private var idCreated: kotlin.String? = ""
    private lateinit var newList: MutableList<DataList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBackgroundActionBar()

        dialog = DialogEditList()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        db = FirebaseFirestore.getInstance()

        fab.setOnClickListener(View.OnClickListener {
            val dialog2 = DialogAddList()
            val ft = supportFragmentManager.beginTransaction()
            dialog2.show(ft, "DialogAddList")
        })

        recycle_main.layoutManager = LinearLayoutManager(this)
        recycle_main.setHasFixedSize(true)

    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth.currentUser

        val nome = currentUser?.displayName.toString()
        val id = currentUser?.uid.toString()
        val email = currentUser?.email.toString()
        val url_photo = String.valueOf(currentUser?.photoUrl)

        if(id != null && id != "null"){
            getListsData(id)
        }

        if(nome != null && nome != "null") {
            text_bem.text = "Seja bem vindo, $nome"
        } else {
            text_bem.text = "Seja bem vindo"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.menu_sair) {
            signOut()
        }
        return super.onOptionsItemSelected(item)
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

    private fun getListsData(id: kotlin.String) {
        newList = mutableListOf<DataList>()

        val listsRef = FirebaseFirestore.getInstance()
        val docRef = listsRef.collection(id)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.documents.size != 0) {
                //Limpo arquivos que estava antes da atualização em tempo real
                newList.clear()
                recycle_main.adapter?.notifyDataSetChanged()

                //Adiciono os itens a um array mutavel da class List
                snapshot.documents.forEach {
                    it.toObject(DataList::class.java).let { entity ->
                        entity?.idList = it.id
                        if (entity != null) {
                            newList.add(entity)
                        }
                    }
                }

                //Tiro os texto pois a lista não está vazia
                text_bem.visibility = View.INVISIBLE
                text_home.visibility = View.INVISIBLE
                text_facil.visibility = View.INVISIBLE

                //Mantando a Lista Mutavel para o Adapter
                recycle_main.adapter = MainAdapter(newList, applicationContext, dialog)
                Log.d(TAG, "Current data: ${newList.size}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

}




