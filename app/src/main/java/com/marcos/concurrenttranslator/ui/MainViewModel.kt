package com.marcos.concurrenttranslator.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcos.concurrenttranslator.data.api.RetrofitClient
import com.marcos.concurrenttranslator.data.repository.TranslationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: TranslationRepository = TranslationRepository(RetrofitClient.api)
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _estado = MutableLiveData<EstadoTraducao>(EstadoTraducao.Inicial)
    val estado: LiveData<EstadoTraducao> = _estado

    fun traduzir(
        texto: String,
        idiomaOrigem: String,
        idiomaDestino: String
    ) {
        if (texto.isBlank()) {
            Log.w(TAG, "Tentativa de traducao com texto vazio")
            _estado.value = EstadoTraducao.Erro("Digite um texto para traduzir.")
            return
        }

        if (idiomaOrigem == idiomaDestino) {
            Log.w(TAG, "Tentativa de traducao com idiomas iguais: $idiomaOrigem")
            _estado.value = EstadoTraducao.Erro("Escolha idiomas diferentes.")
            return
        }

        viewModelScope.launch {
            _estado.value = EstadoTraducao.Carregando
            Log.d(TAG, "Chamando repository.traduzir...")

            val resultado = withContext(Dispatchers.IO) {
                repository.traduzir(
                    texto = texto,
                    idiomaOrigem = idiomaOrigem,
                    idiomaDestino = idiomaDestino
                )
            }

            resultado
                .onSuccess { traducao ->
                    Log.d(TAG, "Traducao recebida com sucesso")
                    _estado.value = EstadoTraducao.Sucesso(traducao)
                }
                .onFailure { erro ->
                    Log.e(TAG, "Falha ao traduzir", erro)
                    _estado.value = EstadoTraducao.Erro(
                        erro.message ?: "Erro inesperado ao traduzir."
                    )
                }
        }
    }
}