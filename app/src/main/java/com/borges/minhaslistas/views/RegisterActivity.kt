package com.borges.minhaslistas.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.borges.minhaslistas.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.supportActionBar?.hide();

        val mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        btn_newregister.setOnClickListener(View.OnClickListener {
            val mFullName = input_text_nome_register.text.toString().trim()
            val mEmail = input_text_email_register.text.toString().trim()
            val mPassword = input_text_password_register.text.toString().trim()
            val mCPassword = input_text_confirmPassword.text.toString().trim()

            if(mEmail.isEmpty()){
                input_text_email_register.error = "Email is Required."
                return@OnClickListener
            }
            if(mPassword.isEmpty()){
                input_text_password_register.error = "Password is Required."
                return@OnClickListener
            }
            if(mPassword.length < 6){
                input_text_password_register.error = "Password Must be >= 6 Characters"
                return@OnClickListener
            }
            if(mPassword != mCPassword) {
                input_text_password_register.error = "Passwords are not the same"
                input_text_confirmPassword.error = "Passwords are not the same"
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE

            mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Log.i("REGISTER", "Usuário Criado.")
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    } else {
                        progressBar.visibility = View.INVISIBLE
                        Log.w("REGISTER","Error ! " + Objects.requireNonNull(task.exception?.message))
                    }
                })

        })

        btn_voltar.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

}