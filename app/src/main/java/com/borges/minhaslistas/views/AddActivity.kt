package com.borges.minhaslistas.views

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.borges.minhaslistas.R
import com.borges.minhaslistas.dialog.DialogAddFragment
import com.borges.minhaslistas.model.List
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private val TAG: String = "ADD"
    private var idCreated: String? = null
    private lateinit var newItens: List

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setBackgroundActionBar()

        val id = FirebaseAuth.getInstance().currentUser?.uid
        val extras = intent.extras
        if (extras != null) {
            idCreated = extras.getParcelable("idCreated")
        }

        idCreated?.let { Log.i(TAG, it) }

        fab_add.setOnClickListener {
            val dialog = DialogAddFragment()
            dialog.show(supportFragmentManager, "DialogAddItem")
        }

        recycle_view_add.layoutManager = LinearLayoutManager(this)
        recycle_view_add.setHasFixedSize(true)


//        if(id != null && idCreated != null){
//            getItemList(id, idCreated)
//        }


    }

    @SuppressLint("RestrictedApi")
    private fun setBackgroundActionBar() {
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.appPrimary)))
        this.supportActionBar?.title = "Nova Lista"
    }

    fun signOut() {
    }

    private fun getItemList(id: String, idCreated: String?) {

    }
}