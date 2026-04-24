package com.marcos.concurrenttranslator.data.model

data class Idioma(
    val nome: String,
    val codigo: String
) {
    override fun toString(): String {
        return nome
    }
}