package com.borges.minhaslistas.utils

import android.content.Context
import android.content.Intent
import android.view.View
import com.borges.minhaslistas.models.DataItem
import com.borges.minhaslistas.models.DataList
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {
        fun USnackbar(view: View, mensagem: String) {
            Snackbar.make(
                view,
                mensagem,
                Snackbar.LENGTH_LONG )
                .show()
        }

        fun sendShared(c: Context, texto: String) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, texto)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            c.startActivity(shareIntent)
        }

        fun templateItens(i: DataItem): String {
            return "\n${if(i.comprado == false) "❌" else "✅"} [${i.qt}] - ${i.nome}"
        }

        fun templateShared(currentItem: DataList, itens: String, total: Double): String {
            val meuLocal = Locale("pt", "BR")
            val z: NumberFormat = NumberFormat.getCurrencyInstance(meuLocal)
            // Retirei o total -->  "\n\nTotal do carrinho: ${z.format(total)}"
            return "Lista: ${currentItem.nomeDaLista}" +
                    "\nMercado: ${if(currentItem.nomeDoMercado != "") currentItem.nomeDoMercado else "-"}" +
                    "\nQuantidade  -  Nome \n" +
                    "$itens"
        }

        fun multiply(x: Double, y: Double) = x * y

        fun findIndex(arr: MutableList<DataList>?, t: String): Int {
            if (arr == null) {
                return -1
            }
            var idx = -1
            for (i in arr.indices) {
                if (arr[i].idList == t) {
                    idx = i
                }
            }
            return idx
        }
    }
}