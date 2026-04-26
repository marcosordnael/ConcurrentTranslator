package com.marcos.concurrenttranslator.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _estado = MutableLiveData<EstadoTraducao>(EstadoTraducao.Inicial)
    val estado: LiveData<EstadoTraducao> = _estado

    fun traduzir(
        texto: String,
        idiomaOrigem: String,
        idiomaDestino: String
    ) {
        if (texto.isBlank()) {
            _estado.value = EstadoTraducao.Erro("Digite um texto para traduzir.")
            return
        }

        if (idiomaOrigem == idiomaDestino) {
            _estado.value = EstadoTraducao.Erro("Escolha idiomas diferentes.")
            return
        }

        _estado.value = EstadoTraducao.Sucesso("Tradução simulada: $texto")
    }
}