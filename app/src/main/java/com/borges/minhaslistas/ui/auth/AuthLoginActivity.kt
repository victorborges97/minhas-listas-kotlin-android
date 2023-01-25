package com.borges.minhaslistas.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.borges.minhaslistas.R
import com.borges.minhaslistas.data.CResource
import com.borges.minhaslistas.databinding.ActivityLoginBinding
import com.borges.minhaslistas.repository.FirestoreRepository
import com.borges.minhaslistas.utils.Utils
import com.borges.minhaslistas.ui.home.activity.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

@AndroidEntryPoint
class AuthLoginActivity : AppCompatActivity(R.layout.activity_login) {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignClient : GoogleSignInClient
    private val RC_SIGN_IN = 1000
    private lateinit var firestoreRepository: FirestoreRepository

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val authViewModel: AuthViewModel by viewModels()

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.supportActionBar?.hide();

        firestoreRepository = FirestoreRepository()

        mAuth = FirebaseAuth.getInstance();

        googleSignClient = firestoreRepository.requestSignInOptions(this)

        binding.btnRegister.setOnClickListener{
            val intent = Intent(applicationContext, AuthSignupActivity::class.java)
            startActivity(intent)
        }

        binding.inputTextEmailLogin.text = "borges.jvdo@gmail.com".toEditable()
        binding.inputTextPasswordLogin.text = "lari2504".toEditable()

        configureLoginEmailState()
        configureLoginCredencial()

        binding.btnLogin.setOnClickListener {
            val email = binding.inputTextEmailLogin.toString().trim()
            val senha = binding.inputTextPasswordLogin.toString().trim()
            if(confirmation(email, senha)) {
                return@setOnClickListener
            }
            authViewModel.login(
                email,
                senha
            )
        }

        binding.signInButton.setOnClickListener{
            signInGoogle()
        }

        binding.inputTextPasswordLogin.setOnEditorActionListener{ v, actionId, event ->
            var handled = false
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
                val email = binding.inputTextEmailLogin.toString().trim()
                val senha = binding.inputTextPasswordLogin.toString().trim()
                if(confirmation(email, senha)) {
                    return@setOnEditorActionListener handled
                }
                authViewModel.login(
                    email,
                    senha
                )
                handled = true
            }
            handled
        }
    }

    private fun configureLoginCredencial() {
        authViewModel.signInUserState.observe(this) { state ->
            when (state) {
                is CResource.Loading -> {
                    changedUiLogin(true)
                }
                is CResource.Failure -> {
                    changedUiLogin(false)
                    Log.e("AUTH_LOGIN", state.exception.code)
                    Utils.USnackbar(
                        binding.signInButton,
                        Objects.requireNonNull(state.exception.message).toString()
                    )
                }
                is CResource.Success -> {
                    gotoProfile()
                }
                else -> {

                }
            }
        }
    }

    private fun configureLoginEmailState() {
        authViewModel.loginUserState.observe(this) { state ->
            when (state) {
                is CResource.Loading -> {
                    changedUiLogin(true)
                }
                is CResource.Failure -> {
                    changedUiLogin(false)
                    Log.e("AUTH_LOGIN", state.exception.code)
                    Utils.USnackbar(
                        binding.signInButton,
                        Objects.requireNonNull(state.exception.message).toString()
                    )
                }
                is CResource.Success -> {
                    gotoProfile()
                }
                else -> {

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth.getCurrentUser()
        updateUI(currentUser)
    }

    // PRIVATES
    private fun changedUiLogin(el: Boolean) {
        blocksFields(el)
        progressBar_login.visibility = if(el) View.VISIBLE else View.INVISIBLE
        sign_in_button.visibility = if(el) View.INVISIBLE else View.VISIBLE
        btn_login.hint = if(el) "Entrando..." else "Entrar"
    }

    private fun confirmation(mEmail: String, mPassword: String): Boolean {
        if (mEmail.isEmpty()) {
            input_text_email_login.error = "E-mail Obrigatório."
            return true
        }
        if (mPassword.isEmpty()) {
            input_text_password_login.error = "Senha Obrigatória."
            return true
        }
        if (mPassword.length < 6) {
            input_text_password_login.error = "A senha deve ser >= 6 caracteres."
            return true
        }
        return false
    }

    private fun signInGoogle() {
        try {
            val signInIntent = googleSignClient.signInIntent
            launcher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e("AUTH_LOGIN", "${e.message}");
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            authViewModel.signInCredencial(task)

//            if(result.resultCode == RC_SIGN_IN) {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//                authViewModel.signInCredencial(task)
//            } else {
//                throw Exception()
//            }
        } catch (e: Exception) {
            throw  e
        }
    }

    private fun blocksFields(b: Boolean) {
        btn_login.isEnabled = !b
        input_text_password_login.isEnabled = !b
        input_text_email_login.isEnabled = !b
    }

    private fun gotoProfile() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            Log.i("LOGIN", currentUser.email.toString());
            gotoProfile();
        }
    }


}