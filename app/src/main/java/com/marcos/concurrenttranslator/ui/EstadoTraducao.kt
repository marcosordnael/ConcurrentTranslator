package com.marcos.concurrenttranslator.ui

sealed class EstadoTraducao {
    data object Inicial : EstadoTraducao()
    data object Carregando : EstadoTraducao()
    data class Sucesso(val textoTraduzido: String) : EstadoTraducao()
    data class Erro(val mensagem: String) : EstadoTraducao()
}