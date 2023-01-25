package com.borges.minhaslistas.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
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

        fun getErrorCodeAuthFirebase(code: String, message: String): String {
            return when (code) {
                "ERROR_INVALID_CUSTOM_TOKEN" -> "O formato de token personalizado está incorreto. Verifique a documentação."
                "ERROR_CUSTOM_TOKEN_MISMATCH" -> "O token personalizado corresponde a um público diferente."
                "ERROR_INVALID_CREDENTIAL" -> "A credencial de autenticação fornecida está incorreta ou expirou."
                "ERROR_INVALID_EMAIL" -> "O endereço de e-mail está inválido"
                "ERROR_WRONG_PASSWORD" -> "A senha é inválida ou o usuário não possui uma senha."
                "ERROR_USER_MISMATCH" -> "As credenciais fornecidas não correspondem ao usuário conectado anteriormente."
                "ERROR_REQUIRES_RECENT_LOGIN" -> "Esta operação é confidencial e requer autenticação recente. Faça login novamente antes de tentar novamente esta solicitação."
                "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> "Já existe uma conta com o mesmo endereço de e-mail, mas credenciais de login diferentes. Entre usando um provedor associado a este endereço de e-mail."
                "ERROR_EMAIL_ALREADY_IN_USE" -> "O endereço de e-mail já está sendo usado por outra conta."
                "ERROR_CREDENTIAL_ALREADY_IN_USE" -> "Esta credencial já está associada a uma conta de usuário diferente."
                "ERROR_USER_DISABLED" -> "A conta do usuário foi desativada por um administrador."
                "ERROR_USER_TOKEN_EXPIRED" -> "A credencial do usuário não é mais válida. O usuário deve entrar novamente."
                "ERROR_USER_NOT_FOUND" -> "Não há registro de usuário correspondente a este identificador. O usuário pode ter sido excluído."
                "ERROR_INVALID_USER_TOKEN" -> "A credencial do usuário não é mais válida. O usuário deve entrar novamente."
                "ERROR_OPERATION_NOT_ALLOWED" -> "Esta operação não é permitida. Você deve habilitar este serviço no console."
                "ERROR_WEAK_PASSWORD" -> "A senha fornecida é inválida."
                else -> message
            }
        }
    }
}