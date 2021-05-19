package com.borges.minhaslistas.model


class List() {
    var id: String? = null
    var nomeDaLista: String? = null
    var created: String? = null
    var itens: ArrayList<DataItem>? = null

    fun List(id: String, nomeDaLista: String, created: String, itens: ArrayList<DataItem>) {
        this.id = id
        this.nomeDaLista = nomeDaLista
        this.created = created
        this.itens = itens
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

    fun getListItens(): ArrayList<DataItem>? {
        return this.itens
    }

    override fun toString(): String {
        return "\n\nID: ${this.id}" +
                "\nNOME: ${this.nomeDaLista}" +
                "\nDATA CRIADA: ${this.created}" +
                "\nITENS: ${this.itens}"
    }

}