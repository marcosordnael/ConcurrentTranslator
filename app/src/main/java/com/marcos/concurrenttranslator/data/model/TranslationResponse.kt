package com.marcos.concurrenttranslator.data.model

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    @SerializedName("status")
    val status: String? = null,

    @SerializedName("data")
    val data: TranslationData? = null
)

data class TranslationData(
    @SerializedName("translatedText")
    val translatedText: String? = null,

    @SerializedName("translated_text")
    val translatedTextSnakeCase: String? = null
) {
    fun obterTextoTraduzido(): String? {
        return translatedText ?: translatedTextSnakeCase
    }
}