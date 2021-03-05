package com.borges.minhaslistas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setBackgroundActionBar()

        findViewById<EditText>(R.id.search).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    Toast.makeText(this, "Foi", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

//        val listview = findViewById<ListView>(R.id.add_listview)
//        val redColor = Color.parseColor("#FF0000")

        // this needs to be my custom adapter telling my list what to render
//        listview.adapter = MyCustomAdapter(this)

    }

//    private class MyCustomAdapter(context: Context): BaseAdapter() {
//        private val mContext: Context
//        init {
//            mContext = context
//        }
//        // responsible for how many rows in my list
//        override fun getCount(): Int {
//            return 5
//        }
//
//        // you can also ignore this
//        override fun getItemId(position: Int): Long {
//            return position.toLong()
//        }
//
//        // you can ignore this for now
//        override fun getItem(position: Int): Any {
//            return "TESTE STRING"
//        }
//
//        // responsible for rendering out each row
//        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
//            val layoutInflater = LayoutInflater.from(mContext)
//            val row_add = layoutInflater.inflate(R.layout.row_add, viewGroup, false)
//            return row_add
////            val textView = TextView(mContext)
////            textView.text = "HERE is my ROW for my LISTVIEW"
////            return textView
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    @SuppressLint("RestrictedApi")
    private fun setBackgroundActionBar() {
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.appPrimary)))
        this.supportActionBar?.title = "Nova Lista"
    }

    fun signOut(item: MenuItem) {

    }
}