package com.marcos.concurrenttranslator.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcos.concurrenttranslator.data.api.RetrofitClient
import com.marcos.concurrenttranslator.data.repository.TranslationRepository
import com.marcos.concurrenttranslator.domain.TranslateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val repository = TranslationRepository(RetrofitClient.api)
    private val translateUseCase = TranslateUseCase(repository)

    private val _estado = MutableLiveData<EstadoTraducao>(EstadoTraducao.Inicial)
    val estado: LiveData<EstadoTraducao> = _estado

    fun traduzir(
        texto: String,
        idiomaOrigem: String,
        idiomaDestino: String
    ) {
        viewModelScope.launch {
            _estado.value = EstadoTraducao.Carregando

            val resultado = withContext(Dispatchers.IO) {
                translateUseCase.executar(
                    texto = texto,
                    idiomaOrigem = idiomaOrigem,
                    idiomaDestino = idiomaDestino
                )
            }

            resultado
                .onSuccess { textoTraduzido ->
                    _estado.value = EstadoTraducao.Sucesso(textoTraduzido)
                }
                .onFailure { exception ->
                    _estado.value = EstadoTraducao.Erro(
                        exception.message ?: "Não foi possível realizar a tradução."
                    )
                }
        }
    }
}