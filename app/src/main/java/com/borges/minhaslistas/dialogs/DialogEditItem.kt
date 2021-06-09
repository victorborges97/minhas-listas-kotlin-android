package com.borges.minhaslistas.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.borges.minhaslistas.R
import com.borges.minhaslistas.models.DataItem
import com.borges.minhaslistas.utils.FirestoreRepository
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.dialog_edit_item.view.*


class DialogEditItem: DialogFragment() {

    lateinit var mNote: DataItem
    lateinit  var mNome: EditText
    lateinit  var mQuant: EditText
    lateinit  var mValor: EditText

    override fun onStart() {
        super.onStart()

        if(mNome.text.toString() != mNote.nome.toString()) {
            //Preenchendo os campos
            mNome.setText(mNote.nome)
            mValor.setText(mNote.preco.toString())
            mQuant.setText(mNote.qt.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_edit_item, container, false)

        //Recebendo os dados dos campos
        mNote = arguments?.getParcelable("currentItem")!!

        //Buscando os campos no layout
        mNome = rootView.findViewById(R.id.dialog_edit_item_edittext_nome)
        mQuant = rootView.findViewById(R.id.dialog_edit_item_edittext_quanti)
        mValor = rootView.findViewById(R.id.dialog_edit_item_edittext_valor)

        rootView.dialog_edit_item_btn_cancelar.setOnClickListener {
            val fb = FirestoreRepository()
            fb.excluirItemList(mNote.idList.toString(), mNote.idItem.toString())
            dialog?.dismiss()
        }

        rootView.dialog_edit_item_btn_salvar.setOnClickListener {
            if (mNome?.text?.isEmpty() == true) {
                mNome?.error = "Nome é Requerido."
                return@setOnClickListener
            }
            updateUserListItem()
        }
        return rootView
    }

    private fun updateUserListItem() {
        val nome: String = mNome.text.toString()
        val quantInt: Int = mQuant.text.toString().toInt()
        val valorDouble: Double = mValor.text.toString().toDouble()

        val qtMultiply = quantInt.toDouble()
        val multiply = multiply(qtMultiply, valorDouble)

        val fire = FirestoreRepository()
        fire.updateItemList(
            nome,
            quantInt,
            valorDouble,
            multiply,
            mNote.idList.toString(),
            mNote.idItem.toString()
        )
        dialog?.dismiss()
    }

    fun multiply(x: Double, y: Double) = x * y

}
