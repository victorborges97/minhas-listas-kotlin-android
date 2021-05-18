package com.borges.minhaslistas.model

class List<T> {
    private var id: String
    private var nome: String
    private var itens: ArrayList<DataItem>
    private var created: String

    constructor(id: String, nome: String, created: String, itens: ArrayList<DataItem>) {
        this.id = id
        this.nome = nome
        this.created = created
        this.itens = itens
    }
}