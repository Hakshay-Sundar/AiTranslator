package com.numad.aitranslator.repositories

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.numad.aitranslator.dao.GenericResponse
import com.numad.aitranslator.dao.TextSelectionResponse
import com.numad.aitranslator.dao.TranslationResults
import com.numad.aitranslator.room.daos.TranslationDao
import com.numad.aitranslator.room.entities.TranslationEntity
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslatorRepository @Inject constructor(
    private val translationDao: TranslationDao
) {
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

    suspend fun fetchTranslations(): List<TranslationEntity> {
        try {
            return translationDao.getAllTranslations()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error fetching translations", e)
            return emptyList()
        }
    }

    suspend fun saveTranslation(
        inputText: String, translatedText: String, languageFrom: String,
        languageTo: String, timeStamp: Long, existingId: Long? = null
    ): GenericResponse {
        return try {
            if (existingId != null) {
                translationDao.updateTranslation(
                    text = inputText,
                    translatedText = translatedText,
                    languageFrom = languageFrom,
                    languageTo = languageTo,
                    timestampMillis = timeStamp,
                    id = existingId
                )
            } else {
                translationDao.insertTranslation(
                    TranslationEntity(
                        text = inputText,
                        translatedText = translatedText,
                        languageFrom = languageFrom,
                        languageTo = languageTo,
                        timestampMillis = timeStamp
                    )
                )
            }
            GenericResponse.Success()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error saving translation", e)
            GenericResponse.Failure(e.message)
        }
    }

    suspend fun fetchTranslationById(id: Long): GenericResponse {
        return try {
            val translation = translationDao.getTranslationById(id)
            if (translation != null) {
                GenericResponse.Success(translation)
            } else {
                GenericResponse.Failure("Translation not found")
            }
        } catch (e: Exception) {
            GenericResponse.Failure(e.message)
        }
    }

    suspend fun deleteTranslation(id: Long): GenericResponse {
        return try {
            translationDao.deleteTranslation(id)
            GenericResponse.Success()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error deleting translation", e)
            GenericResponse.Failure(e.message)
        }
    }

    suspend fun fetchTextFromImage(image: Bitmap): TextSelectionResponse {
        return try {
            val textRecognizer = TextRecognition.getClient()
            val input = InputImage.fromBitmap(image, 0)
            var result = TextSelectionResponse()
            textRecognizer.process(input)
                .addOnSuccessListener { texts ->
                    val textBlocks = texts.textBlocks
                    if (textBlocks.size > 0) {
                        val listOfLines = arrayListOf<String>()
                        textBlocks.stream().map { block ->
                            listOfLines.add(block.text)
                        }
                        result = result.setSuccess(true).setTexts(listOfLines)
                    } else {
                        result = result.setSuccess(false).setError("No text found in the image")
                    }
                }.addOnFailureListener { error ->
                    Log.e("TranslatorRepository", "Error fetching text from image", error)
                    result = result.setSuccess(false).setError(error.message)
                }
            result
        } catch (e: Exception) {
            TextSelectionResponse(false, emptyList(), e.message)
        }
    }
}
