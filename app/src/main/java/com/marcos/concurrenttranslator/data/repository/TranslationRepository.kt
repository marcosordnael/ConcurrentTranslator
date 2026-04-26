package com.marcos.concurrenttranslator.data.repository

import com.marcos.concurrenttranslator.BuildConfig
import com.marcos.concurrenttranslator.data.api.TranslationApi
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TranslationRepository(
    private val api: TranslationApi
) {

    suspend fun traduzir(
        texto: String,
        idiomaOrigem: String,
        idiomaDestino: String
    ): Result<String> {
        return try {
            val response = api.traduzir(
                apiKey = BuildConfig.RAPID_API_KEY,
                apiHost = BuildConfig.RAPID_API_HOST,
                idiomaOrigem = idiomaOrigem,
                idiomaDestino = idiomaDestino,
                texto = texto
            )

            val textoTraduzido = response.data?.translatedText

            if (textoTraduzido.isNullOrBlank()) {
                Result.failure(Exception("A tradução não retornou resultado."))
            } else {
                Result.success(textoTraduzido)
            }
        } catch (exception: UnknownHostException) {
            Result.failure(Exception("Sem conexão com a internet. Verifique sua rede."))
        } catch (exception: SocketTimeoutException) {
            Result.failure(Exception("A tradução demorou demais para responder. Tente novamente."))
        } catch (exception: Exception) {
            Result.failure(Exception("Não foi possível realizar a tradução no momento."))
        }
    }
}