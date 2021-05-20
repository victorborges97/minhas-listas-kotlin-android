package com.borges.minhaslistas.model


class List() {
    var id: String? = null
    var nomeDaLista: String? = null
    var created: String? = null


    fun List(id: String, nomeDaLista: String, created: String) {
        this.id = id
        this.nomeDaLista = nomeDaLista
        this.created = created
    }

    fun getUid(): String? {
        return this.id
    }

    fun getCreatedData(): String? {
        return this.created
    }

    fun getNome(): String? {
        return this.nomeDaLista
    }


    override fun toString(): String {
        return "\n\nID: ${this.id}" +
                "\nNOME: ${this.nomeDaLista}" +
                "\nDATA CRIADA: ${this.created}"
    }

}