package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.borges.minhaslistas.R
import com.borges.minhaslistas.dialog.DialogAddFragment
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private val TAG: String = "ADD"
    private var idCreated: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setBackgroundActionBar()

        val extras = intent.extras
        if (extras != null) {
            idCreated = extras.getString("idCreated")
        }

        fab_add.setOnClickListener {
            val dialog = DialogAddFragment()
            dialog.show(supportFragmentManager, "DialogAddItem")
        }

        Log.i(TAG, "$idCreated")


    }

    @SuppressLint("RestrictedApi")
    private fun setBackgroundActionBar() {
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.appPrimary)))
        this.supportActionBar?.title = "Nova Lista"
    }

    fun signOut() {
    }
}