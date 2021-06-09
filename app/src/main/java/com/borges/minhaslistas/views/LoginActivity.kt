package com.borges.minhaslistas.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.borges.minhaslistas.R
import com.borges.minhaslistas.repository.FirestoreRepository
import com.borges.minhaslistas.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private var googleSignClient : GoogleSignInClient? = null
    private val RC_SIGN_IN = 1000
    private lateinit var firestoreRepository: FirestoreRepository

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth.getCurrentUser()
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firestoreRepository = FirestoreRepository()
        mAuth = FirebaseAuth.getInstance();

        this.supportActionBar?.hide();

        googleSignClient = firestoreRepository.requestSignInOptions(this)

        btn_register.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener{
            loginEmail()
        }

        sign_in_button.setOnClickListener{
            signInGoogle()
        }

        input_text_password_login.setOnEditorActionListener{ v, actionId, event ->
            var handled = false
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
                loginEmail()
                handled = true
            }
            handled
        }
    }

    private fun loginEmail() {
        val mEmail = input_text_email_login.text.toString().trim()
        val mPassword = input_text_password_login.text.toString().trim()

        if (confirmation(mEmail, mPassword)) return

        changedUiLogin(true)

        mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                gotoProfile()
            } else {
                changedUiLogin(false)
                // Retorno de erro para o usuario
                Utils.USnackbar(sign_in_button, Objects.requireNonNull(task.exception?.message).toString())
            }
        }
    }

    private fun changedUiLogin(el: Boolean) {
        blocksFields(el)
        progressBar_login.visibility = if(el) View.VISIBLE else View.INVISIBLE
        sign_in_button.visibility = if(el) View.INVISIBLE else View.VISIBLE
        btn_login.hint = if(el) "Entrando..." else "Entrar"
    }

    private fun confirmation(mEmail: String, mPassword: String): Boolean {
        if (mEmail.isEmpty()) {
            input_text_email_login.error = "Email is Required."
            return true
        }
        if (mPassword.isEmpty()) {
            input_text_password_login.error = "Password is Required."
            return true
        }
        if (mPassword.length < 6) {
            input_text_password_login.error = "Password Must be >= 6 Characters"
            return true
        }
        return false
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        var credencial = GoogleAuthProvider.getCredential(acct?.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                gotoProfile()
            } else {
                Toast.makeText(this, "Error no login", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun gotoProfile() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w("LOGIN", "Google sign in failed", e)
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            Log.i("LOGIN", currentUser.email.toString());
            gotoProfile();
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun blocksFields(b: Boolean) {
        btn_login.isEnabled = !b
        input_text_password_login.isEnabled = !b
        input_text_email_login.isEnabled = !b
    }
}