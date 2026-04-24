package com.marcos.concurrenttranslator.data.model

data class TranslationRequest(
    val text: String,
    val sourceLanguage: String,
    val targetLanguage: String
)