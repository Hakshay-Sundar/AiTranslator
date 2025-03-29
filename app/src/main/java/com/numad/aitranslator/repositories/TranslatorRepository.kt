package com.numad.aitranslator.repositories

import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.numad.aitranslator.dao.TranslationResults
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslatorRepository @Inject constructor() {
    suspend fun translateText(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): TranslationResults {
        val translationOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(translationOptions)

        return try {
            // Set a timeout of 5 seconds for the translation operation.
            val result = withTimeoutOrNull(5000L) {
                translator.downloadModelIfNeeded().await()
                translator.translate(text).await()
            }
            if (result == null) {
                // Result has timed out.
                Log.e("TranslatorRepository", "Translation timed out")
                TranslationResults.Error("Translation timed out. Please try again.")
            } else {
                TranslationResults.Success(result)
            }
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error translating text", e)
            TranslationResults.Error("Translation failed: ${e.message}")
        }
    }

    suspend fun detectLanguage(text: String): String {
        val identificationOptions = LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(0.6f) // 60% confidence threshold on the identified language. Unknown otherwise
            .build()

        val languageIdentifier = LanguageIdentification.getClient(identificationOptions)

        return try {
            val detectedLang = languageIdentifier.identifyLanguage(text).await()
            if (detectedLang == "und") {
                ""
            } else {
                detectedLang
            }
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error identifying language of text", e)
            "Error: Failure in identifying language."
        }
    }
}