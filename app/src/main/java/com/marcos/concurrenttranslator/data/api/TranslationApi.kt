package com.marcos.concurrenttranslator.data.api

import com.marcos.concurrenttranslator.data.model.TranslationResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface TranslationApi {

    @FormUrlEncoded
    @POST("translate")
    suspend fun traduzir(
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String,
        @Field("source_language") idiomaOrigem: String,
        @Field("target_language") idiomaDestino: String,
        @Field("text") texto: String
    ): TranslationResponse
}