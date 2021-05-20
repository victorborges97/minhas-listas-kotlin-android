package com.borges.minhaslistas.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.borges.minhaslistas.R
import kotlinx.android.synthetic.main.dialog_add_list.view.*

class DialogAddList: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_add_list, container, false)

        rootView.dialog_add_list_btn_cancelar.setOnClickListener {
            dismiss()
        }

        return rootView
    }



}