package com.borges.minhaslistas.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.borges.minhaslistas.R
import com.borges.minhaslistas.utils.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_add_item.view.*
import kotlinx.android.synthetic.main.dialog_add_list.view.*
import kotlinx.android.synthetic.main.dialog_edit_item.view.*

class DialogAddItem: DialogFragment() {

    lateinit var mIdList: String
    private  var mNome: EditText? = null
    private  var mQuant: EditText? = null
    private  var mValor: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mIdList = arguments?.getString("idList")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_add_item, container, false)

        mNome = rootView.findViewById(R.id.dialog_add_item_edittext_nome)
        mQuant = rootView.findViewById(R.id.dialog_add_item_edittext_quant)
        mValor = rootView.findViewById(R.id.dialog_add_item_edittext_valor)

        rootView.dialog_add_item_btn_cancelar.setOnClickListener {
            dismiss()
        }

        rootView.dialog_add_item_btn_salvar.setOnClickListener {
            if (mNome?.text?.isEmpty() == true) {
                mNome?.error = "Nome é Requerido."
                return@setOnClickListener
            }
            createdItem(mIdList)
        }

        return rootView
    }

    private fun createdItem(idList: String) {
        val nome: String = mNome?.text.toString()
        val quantInt: Int = if(mQuant?.text?.isEmpty() == true) 0 else mQuant?.text.toString().toInt()
        val valorDouble: Double = if(mValor?.text?.isEmpty() == true) 0.0 else mValor?.text.toString().toDouble()


        val qtMultiply = quantInt.toDouble()
        val multiply = multiply(qtMultiply, valorDouble)

        val fire = Firebase()
        fire.createItemList(nome, quantInt, valorDouble, multiply, idList)
        dismiss()
    }

    fun multiply(x: Double, y: Double) = x * y

}