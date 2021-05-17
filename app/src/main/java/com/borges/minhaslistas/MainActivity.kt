package com.borges.minhaslistas

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.borges.minhaslistas.model.DataList
import com.borges.minhaslistas.model.Item
import com.borges.minhaslistas.model.List
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.String
import java.sql.Array
import java.sql.Timestamp
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var db: FirebaseFirestore
    private var nome: kotlin.String? = null
    private var id: kotlin.String? = null
    private var email: kotlin.String? = null
    private var url_photo: kotlin.String? = null
    private var TAG = "MAIN"

    private lateinit var newItens: ArrayList<DataList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBackgroundActionBar()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        db = FirebaseFirestore.getInstance()
        val c = Calendar.getInstance()
        val data = c.time;
        val brasil = Locale("pt", "BR")
        val f2: DateFormat = DateFormat.getDateInstance(DateFormat.FULL, brasil)

        // Create a new user with a first and last name
        // Create a new user with a first and last name
        val itens = hashMapOf(
        "comprado" to false,
        "nome" to "Caixa de Leite",
        "preco" to 4.30,
        "qt" to 2,
        "total" to 2*4.30,
        )

        val user = hashMapOf(
        "created" to f2.format(data),
        "nomeDaLista" to "Mês Março",
        "itens" to arrayListOf(
                itens,
                itens
            ),
        )

        fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(intent)
        })

        getListsData()


    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth.getCurrentUser()

        this.nome = currentUser?.displayName.toString()
        this.id = currentUser?.uid.toString()
        this.email = currentUser?.email.toString()
        this.url_photo = String.valueOf(currentUser?.photoUrl)

        Log.i("MAIN", currentUser?.displayName.toString())

        if(nome != null && nome != "null") {
            text_bem.text = "Seja bem vindo, "+nome
        } else {
            text_bem.text = "Seja bem vindo"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
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

    fun signOut(item: MenuItem) {
        logout()
    }

    private fun createData(){
//        db.collection("lists")
//            .document("$id")
//            .set(user)
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    private fun getListsData() {
        newItens = arrayListOf<DataList>()

        val listsRef = FirebaseFirestore.getInstance()

        val docRef = listsRef.collection("lists")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val cities = ArrayList<DataList>()
            for (doc in snapshot!!) {
                var nome: kotlin.String = doc.data["nomeDaLista"] as kotlin.String
                var created: kotlin.String = doc.data["created"] as kotlin.String
                var itens: Item = doc.data["itens"] as Item

                val user = hashMapOf(
                    "created" to created,
                    "nomeDaLista" to nome,
                    "itens" to itens
                )

//                cities.add(user)

                Log.d(TAG, "Current: ${user}")
            }
        }
    }
}






