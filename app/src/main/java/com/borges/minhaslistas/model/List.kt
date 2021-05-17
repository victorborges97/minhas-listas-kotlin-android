package com.borges.minhaslistas.model

class Item {
    private var preco: Double? = 0.0
    private var total: Double? = 0.0
    private var qt: Int? = 0
    private lateinit var nome: String
    private var comprado: Boolean? = false

    constructor(nome: String, comprado: Boolean, qt: Int, preco: Double) {
        this.nome = nome
        this.comprado = comprado
        this.qt = qt
        this.preco = preco
        this.total = qt*preco
    }

}

class List<T> {
    private var id: String
    private var nome: String
    private var itens: ArrayList<Item>
    private var created: String

    constructor(id: String, nome: String, created: String, itens: ArrayList<Item>) {
        this.id = id
        this.nome = nome
        this.created = created
        this.itens = itens
    }
}