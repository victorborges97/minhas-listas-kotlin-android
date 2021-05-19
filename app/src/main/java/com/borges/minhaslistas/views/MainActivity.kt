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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borges.minhaslistas.R
import com.borges.minhaslistas.model.DataItem
import com.borges.minhaslistas.model.DataList
import com.borges.minhaslistas.model.List
import com.borges.minhaslistas.recycle.MainAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.String
import java.text.DateFormat
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

    private var idCreated: kotlin.String? = ""

    private lateinit var newItens: MutableList<List>

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

        fab.setOnClickListener(View.OnClickListener {
            createData()
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

    private fun createData(){
        val currentUser: FirebaseUser? = mAuth.currentUser

        val c = Calendar.getInstance()
        val data = c.time;
        val brasil = Locale("pt", "BR")
        val f2: DateFormat = DateFormat.getDateInstance(DateFormat.DATE_FIELD, brasil)

        // Create a new user with a first and last name
        val itens = arrayListOf<DataItem>()

//        itens.add(DataItem(comprado = false, nome = "Caixa de leite", preco = 4.30, qt = 2, total = 2*4.30))
//        itens.add(DataItem(comprado = false, nome = "Mussarela", preco = 8.50, qt = 1, total = 1*8.50))
//        itens.add(DataItem(comprado = false, nome = "Manteiga", preco = 5.30, qt = 1, total = 1*5.30))

        val user = hashMapOf(
            "created" to f2.format(data),
            "nomeDaLista" to "Mês de Maio",
            "itens" to itens
        )

        //f2.format(data) -> data anterior ao Timestamp

        db.collection(currentUser?.uid.toString())
            .add(user)
            .addOnSuccessListener {
                idCreated = it.id
                Log.d(TAG, "DocumentSnapshot written with ID: ${it.id}")
                val intent = Intent(applicationContext, AddActivity::class.java)
                intent.putExtra("idCreated", it.id)
                startActivity(intent)
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
            }
    }

    private fun getListsData(id: kotlin.String) {
        newItens = mutableListOf<List>()

        val listsRef = FirebaseFirestore.getInstance()
        val docRef = listsRef.collection(id)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.documents.size != 0) {
                //Limpo arquivos que estava antes da atualização em tempo real
                newItens.clear()
                recycle_main.adapter?.notifyDataSetChanged()

                //Adiciono os itens a um array mutavel da class List
                snapshot.documents.forEach {
                    it.toObject(List::class.java).let { entity ->
                        entity?.id = it.id
                        if (entity != null) {
                            newItens.add(entity)
                        }
                    }
                }

                //Tiro os texto pois a lista não está vazia
                text_bem.visibility = View.INVISIBLE
                text_home.visibility = View.INVISIBLE
                text_facil.visibility = View.INVISIBLE

                //Mantando a Lista Mutavel para o Adapter
                recycle_main.adapter = MainAdapter(newItens, applicationContext)
                Log.d(TAG, "Current data: ${newItens.size}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun goToEdit(currentItem: DataItem) {
//        val intent = Intent(applicationContext, AddActivity::class.java)
//        intent.putExtra("idCreated", currentItem.getUid())
//        ContextCompat.startActivity(intent)
    }
}

/*
*  Função que eu estava usando, mais não era em tempo real
//        docRef
//            .get()
//            .addOnSuccessListener { documents ->
//                if(documents.size() != 0){
//                    documents.forEach {
//                        it.toObject(List::class.java).let { entity ->
//                            entity.id = it.id
//                            newItens.add(entity)
//                        }
//                    }
//                    text_bem.visibility = View.INVISIBLE
//                    text_home.visibility = View.INVISIBLE
//                    text_facil.visibility = View.INVISIBLE
//
//                    recycle_main.adapter = MainAdapter(newItens)
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Error ao carregar a lista: $it", Toast.LENGTH_SHORT).show()
//            }
*
* */




