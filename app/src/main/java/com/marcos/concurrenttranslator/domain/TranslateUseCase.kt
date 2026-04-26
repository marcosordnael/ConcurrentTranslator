package com.marcos.concurrenttranslator.domain

import com.marcos.concurrenttranslator.data.repository.TranslationRepository

class TranslateUseCase(
    private val repository: TranslationRepository
) {

    suspend fun executar(
        texto: String,
        idiomaOrigem: String,
        idiomaDestino: String
    ): Result<String> {
        if (texto.isBlank()) {
            return Result.failure(Exception("Digite um texto para traduzir."))
        }

        if (idiomaOrigem == idiomaDestino) {
            return Result.failure(Exception("Escolha idiomas diferentes."))
        }

        return repository.traduzir(
            texto = texto.trim(),
            idiomaOrigem = idiomaOrigem,
            idiomaDestino = idiomaDestino
        )
    }
}