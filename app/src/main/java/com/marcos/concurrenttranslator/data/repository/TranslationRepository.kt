package com.marcos.concurrenttranslator.data.repository

import android.util.Log
import com.marcos.concurrenttranslator.BuildConfig
import com.marcos.concurrenttranslator.data.api.TranslationApi
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TranslationRepository(
    private val api: TranslationApi
) {

    companion object {
        private const val TAG = "TranslationRepository"
    }

    suspend fun traduzir(
        texto: String,
        idiomaOrigem: String,
        idiomaDestino: String
    ): Result<String> {
        return try {
            Log.d(
                TAG,
                "Iniciando traducao. origem=$idiomaOrigem destino=$idiomaDestino texto='${texto.take(80)}'"
            )

            val response = api.traduzir(
                apiKey = BuildConfig.RAPID_API_KEY,
                apiHost = BuildConfig.RAPID_API_HOST,
                idiomaOrigem = idiomaOrigem,
                idiomaDestino = idiomaDestino,
                texto = texto
            )

            Log.d(
                TAG,
                "Resposta API recebida. status=${response.status} translatedText=${response.data?.translatedText} translated_text=${response.data?.translatedTextSnakeCase}"
            )

            val textoTraduzido = response.data?.obterTextoTraduzido()

            if (textoTraduzido.isNullOrBlank()) {
                Log.w(TAG, "Resposta sem texto traduzido. Payload=$response")
                Result.failure(Exception("A traducao nao retornou resultado."))
            } else {
                Log.d(TAG, "Traducao concluida com sucesso: '$textoTraduzido'")
                Result.success(textoTraduzido)
            }
        } catch (exception: UnknownHostException) {
            Log.e(TAG, "Sem conexao com a internet.", exception)
            Result.failure(Exception("Sem conexao com a internet. Verifique sua rede."))
        } catch (exception: SocketTimeoutException) {
            Log.e(TAG, "Timeout na chamada de traducao.", exception)
            Result.failure(Exception("A traducao demorou demais para responder. Tente novamente."))
        } catch (exception: Exception) {
            Log.e(TAG, "Falha inesperada na traducao.", exception)
            Result.failure(Exception("Nao foi possivel realizar a traducao no momento."))
        }
    }
}