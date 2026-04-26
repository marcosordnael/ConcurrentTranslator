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
        val textoFormatado = texto.trim()

        if (textoFormatado.isBlank()) {
            return Result.failure(
                Exception("Digite um texto para traduzir.")
            )
        }

        if (textoFormatado.length > 15000) {
            return Result.failure(
                Exception("O texto ultrapassa o limite de 15.000 caracteres.")
            )
        }

        if (idiomaOrigem == idiomaDestino) {
            return Result.failure(
                Exception("Escolha idiomas diferentes.")
            )
        }

        return repository.traduzir(
            texto = textoFormatado,
            idiomaOrigem = idiomaOrigem,
            idiomaDestino = idiomaDestino
        )
    }
}