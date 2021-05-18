package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.borges.minhaslistas.R
import com.borges.minhaslistas.dialog.DialogAddFragment
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setBackgroundActionBar()

        fab_add.setOnClickListener {
            val dialog = DialogAddFragment()
            dialog.show(supportFragmentManager, "DialogAddItem")
        }


    }

    @SuppressLint("RestrictedApi")
    private fun setBackgroundActionBar() {
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.appPrimary)))
        this.supportActionBar?.title = "Nova Lista"
    }

    fun signOut() {
    }
}