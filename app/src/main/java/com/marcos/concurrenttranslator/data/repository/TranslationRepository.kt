package com.marcos.concurrenttranslator.data.repository

import com.marcos.concurrenttranslator.BuildConfig
import com.marcos.concurrenttranslator.data.api.TranslationApi
import retrofit2.HttpException
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
            if (BuildConfig.RAPID_API_KEY.isBlank()) {
                return Result.failure(
                    Exception("A chave da API não foi configurada.")
                )
            }

            val response = api.traduzir(
                apiKey = BuildConfig.RAPID_API_KEY,
                apiHost = BuildConfig.RAPID_API_HOST,
                idiomaOrigem = idiomaOrigem,
                idiomaDestino = idiomaDestino,
                texto = texto
            )

            val textoTraduzido = response.data?.translatedText

            if (textoTraduzido.isNullOrBlank()) {
                Result.failure(
                    Exception("A tradução não retornou resultado.")
                )
            } else {
                Result.success(textoTraduzido)
            }
        } catch (exception: UnknownHostException) {
            Result.failure(
                Exception("Sem conexão com a internet. Verifique sua rede.")
            )
        } catch (exception: SocketTimeoutException) {
            Result.failure(
                Exception("A tradução demorou demais para responder. Tente novamente.")
            )
        } catch (exception: HttpException) {
            Result.failure(
                Exception(tratarErroHttp(exception.code()))
            )
        } catch (exception: Exception) {
            Result.failure(
                Exception("Não foi possível realizar a tradução no momento.")
            )
        }
    }

    private fun tratarErroHttp(codigo: Int): String {
        return when (codigo) {
            400 -> "A solicitação enviada para tradução é inválida."
            401 -> "A chave da API não foi autorizada."
            403 -> "Acesso negado à API de tradução."
            404 -> "Serviço de tradução não encontrado."
            429 -> "Limite de requisições da API atingido. Tente novamente mais tarde."
            in 500..599 -> "O serviço de tradução está indisponível no momento."
            else -> "Erro ao se comunicar com a API de tradução."
        }
    }
}