package com.borges.minhaslistas.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.borges.minhaslistas.R
import com.borges.minhaslistas.models.DataList
import com.borges.minhaslistas.utils.Firebase
import kotlinx.android.synthetic.main.dialog_duplic_list.view.*


class DialogDuplicList: DialogFragment() {

    lateinit var mNote: DataList
    lateinit var mNome: EditText
    lateinit var mMercado: EditText

    override fun onStart() {
        super.onStart()
        if(mNome.text.toString() != mNote.nomeDaLista.toString()) {
            //Preenchendo os campos
            mNome.setText(mNote.nomeDaLista)
            mMercado.setText(mNote.nomeDoMercado)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_duplic_list, container, false)

        //Recebendo os dados dos campos
        mNote = arguments?.getParcelable("currentItem")!!

        //Buscando os campos no layout
        mNome = rootView.findViewById(R.id.dialog_duplic_list_edittext_nome)
        mMercado = rootView.findViewById(R.id.dialog_duplic_list_edittext_mercado)

        rootView.dialog_duplic_list_btn_cancelar.setOnClickListener {
            dialog?.dismiss()
        }

        rootView.dialog_duplic_list_btn_salvar.setOnClickListener {
            if (mNome.text?.isEmpty() == true) {
                mNome.error = "Nome é Requerido."
                return@setOnClickListener
            }
            duplicList()
            dialog?.dismiss()
        }

        return rootView
    }

    private fun duplicList() {
        val fb = Firebase()
        val nome: String = mNome.text.toString()
        val mercado: String = mMercado.text.toString()
        fb.dupliqueList(mNote.idList.toString(), nome, mercado)
    }




}