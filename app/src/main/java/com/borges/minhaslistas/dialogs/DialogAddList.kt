package com.borges.minhaslistas.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.borges.minhaslistas.R
import com.borges.minhaslistas.utils.Firebase
import kotlinx.android.synthetic.main.dialog_add_list.view.*

class DialogAddList: DialogFragment() {

    private var mNome: EditText? = null
    private var mMercado: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_add_list, container, false)

        mNome = rootView.findViewById(R.id.dialog_add_list_edittext_nome)
        mMercado = rootView.findViewById(R.id.dialog_add_list_edittext_mercado)

        rootView.dialog_add_list_btn_cancelar.setOnClickListener {
            dialog?.dismiss()
        }

        rootView.dialog_add_list_btn_criar.setOnClickListener {
            if (mNome?.text?.isEmpty() == true) {
                mNome?.error = "Nome é Requerido."
                return@setOnClickListener
            }
            createData(mNome?.text?.toString(), mMercado?.text?.toString())
            dialog?.dismiss()
        }

        return rootView
    }

    private fun createData(nomeDaLista: String?, mercado: String?){
        val fb = Firebase()
        fb.createList(nomeDaLista, mercado)
    }




}