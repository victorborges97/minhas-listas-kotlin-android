package com.borges.minhaslistas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    var googleSignClient : GoogleSignInClient? = null
    val RC_SIGN_IN = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();

        this.supportActionBar?.hide();

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(this, gso)

        btn_register.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        })

        btn_login.setOnClickListener(View.OnClickListener {
            val mEmail = input_text_email_login.text.toString().trim()
            val mPassword = input_text_password_login.text.toString().trim()

            if (mEmail.isEmpty()) {
                input_text_email_login.error = "Email is Required."
                return@OnClickListener
            }
            if (mPassword.isEmpty()) {
                input_text_password_login.error = "Password is Required."
                return@OnClickListener
            }
            if (mPassword.length < 6) {
                input_text_password_login.error = "Password Must be >= 6 Characters"
                return@OnClickListener
            }

            blocksFields(true)
            progressBar_login.visibility = View.VISIBLE
            sign_in_button.visibility = View.INVISIBLE
            btn_login.hint = "Entrando..."

            mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Log.i("LOGIN", "Logged in Successfully.")
                        gotoProfile()
                    } else {
                        btn_login.hint = "Entrar"
                        blocksFields(false)
                        progressBar_login.visibility = View.INVISIBLE
                        sign_in_button.visibility = View.VISIBLE
                        Log.w("LOGIN",
                            "Error ! " + Objects.requireNonNull(task.exception?.message).toString())
                        Toast.makeText(this, "Error ! " + Objects.requireNonNull(task.exception?.message).toString(), Toast.LENGTH_LONG).show()
                    }
                })
        })

        sign_in_button.setOnClickListener(View.OnClickListener {
            signInGoogle()
        })
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth.getCurrentUser()
        updateUI(currentUser)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        var credencial = GoogleAuthProvider.getCredential(acct?.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login efetuado com sucesso.", Toast.LENGTH_LONG).show()
                gotoProfile()
            } else {
                Toast.makeText(this, "Algo de errado no login", Toast.LENGTH_LONG).show()
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
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            Log.i("LOGIN", currentUser.getEmail().toString());
            gotoProfile();
        }
    }

    private fun signInGoogle() {
        var signInIntent = googleSignClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun blocksFields(b: Boolean) {
        btn_login.isEnabled = !b
        input_text_password_login.isEnabled = !b
        input_text_email_login.isEnabled = !b
    }
}