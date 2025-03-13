package com.numad.aitranslator.repositories

import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslatorRepository @Inject constructor() {
    suspend fun translateText(
        text: String,
        sourceLanguage: String = TranslateLanguage.ENGLISH,
        targetLanguage: String = TranslateLanguage.SPANISH
    ): String {
        val translationOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(translationOptions)

        return try {
            translator.downloadModelIfNeeded().await()
            translator.translate(text).await()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error translating text", e)
            "Error: Translation failed"
        }
    }

    suspend fun detectLanguage(text: String): String {
        val identificationOptions = LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(0.6f) // 60% confidence threshold on the identified language. Unknown otherwise
            .build()

        val languageIdentifier = LanguageIdentification.getClient(identificationOptions)

        return try {
            languageIdentifier.identifyLanguage(text).await()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error identifying language of text", e)
            "Error: Failure in identifying language."
        }
    }
}