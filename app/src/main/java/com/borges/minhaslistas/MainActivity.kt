package com.borges.minhaslistas

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.String


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var nome: kotlin.String? = null
    private var id: kotlin.String? = null
    private var email: kotlin.String? = null
    private var url_photo: kotlin.String? = null

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

//    .collection("iDIcdI76iIZ4iJloDM3n1vnX4Oo2")
//    .orderBy("nomeDaLista", "asc")
}

