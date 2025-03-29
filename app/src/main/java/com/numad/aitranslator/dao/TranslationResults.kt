package com.numad.aitranslator.dao

sealed class TranslationResults {
    data class Success(val translatedText: String) : TranslationResults()
    data class Error(val errorMessage: String) : TranslationResults()
}
