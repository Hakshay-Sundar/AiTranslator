package com.numad.aitranslator.repositories

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.numad.aitranslator.dao.GenericResponse
import com.numad.aitranslator.dao.TextSelectionResponse
import com.numad.aitranslator.dao.TranslationResults
import com.numad.aitranslator.room.daos.TranslationDao
import com.numad.aitranslator.room.entities.TranslationEntity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Repository that is responsible for all the translation related operations.
 * It communicates to the Firebase ML models as well as the local storage via Google Room.
 * */
@Singleton
class TranslatorRepository @Inject constructor(
    private val translationDao: TranslationDao
) {

    /**
     * This function is responsible for translating the given text from one language to another.
     * @param text The text to be translated.
     * @param sourceLanguage The language code of the source language.
     * @param targetLanguage The language code of the target language.
     * The languages are denoted by the ISO 639-1 codes.
     * @return A [TranslationResults] object that contains the translated text if the translation
     * is successful, or an error message if the translation fails.
     * <br><br> This call to the model has a timeout of 5 seconds. If the model takes longer than
     * 10 seconds, the operation is terminated and the function returns an error.
     * */
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
            // Set a timeout of 10 seconds for the translation operation.
            val result = withTimeoutOrNull(10000L) {
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

    /**
     * This function is responsible for detecting the language of the given text.
     * It calls upon the Firebase ML model for this task.
     * @param text The text that the user has typed in whose source language needs to be detected.
     *
     * @return A [String] that contains the language code of the detected language.
     * */
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

    /**
     * This function is responsible for fetching all the translations from the local storage.
     * @return A [List] of [TranslationEntity] objects that contains all the translations.
     * */
    suspend fun fetchTranslations(): List<TranslationEntity> {
        try {
            return translationDao.getAllTranslations()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error fetching translations", e)
            return emptyList()
        }
    }

    /**
     * This function is used to store a transtion object in the local storage.
     * @param inputText This is the text that the user has typed in.
     * @param translatedText This is the text that has been translated.
     * @param languageFrom This is the language code of the source language.
     * @param languageTo This is the language code of the target language.
     * @param timeStamp This is the timestamp of the translation.
     * @param existingId This is the id of the translation that needs to be updated.
     * In case it is null, a new entry gets created in the table. Otherwise, an existing row is updated.
     * @return A [GenericResponse] object that contains the result of the operation.
     * */
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

    /**
     * This function is used to fetch a translation from the local storage by its id.
     * @param id This is the id of the translation that needs to be fetched.
     * @return A [GenericResponse] object that contains the result of the operation.
     * */
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

    /**
     * This function is used to delete a translation from the local storage by its id.
     * @param id This is the id of the translation that needs to be deleted.
     * @return A [GenericResponse] object that contains the result of the operation.
     * */
    suspend fun deleteTranslation(id: Long): GenericResponse {
        return try {
            translationDao.deleteTranslation(id)
            GenericResponse.Success()
        } catch (e: Exception) {
            Log.e("TranslatorRepository", "Error deleting translation", e)
            GenericResponse.Failure(e.message)
        }
    }

    /**
     * This function is used to fetch the text from the image.
     * It takes the bitmap of an image and feeds it to the Firebase ML model that is used to
     * fetch the identified text from the image.
     * @param image This is the image that is fetched either from the camera or from the gallery.
     * @return A [TextSelectionResponse] object that contains the result of the operation.
     * */
    suspend fun fetchTextFromImage(image: Bitmap): TextSelectionResponse {
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val input = InputImage.fromBitmap(image, 0)
        return suspendCancellableCoroutine { continuation ->
            try {
                textRecognizer.process(input)
                    .addOnSuccessListener { texts ->
                        val textBlocks = texts.textBlocks
                        if (textBlocks.size > 0) {
                            val listOfLines = arrayListOf<String>()
                            textBlocks.stream().map { block ->
                                listOfLines.add(block.text)
                            }.collect(Collectors.toList())
                            continuation.resume(
                                TextSelectionResponse(true, listOfLines, null)
                            )
                        } else {
                            continuation.resume(
                                TextSelectionResponse(
                                    false,
                                    emptyList(),
                                    "No text found in the image"
                                )
                            )
                        }
                    }.addOnFailureListener { error ->
                        Log.e("TranslatorRepository", "Error fetching text from image", error)
                        continuation.resume(
                            TextSelectionResponse(false, emptyList(), error.message)
                        )
                    }
                continuation.invokeOnCancellation {
                    textRecognizer.close()
                    continuation.resume(
                        TextSelectionResponse(false, emptyList(), "Operation cancelled")
                    )
                }
            } catch (e: Exception) {
                continuation.resume(
                    TextSelectionResponse(false, emptyList(), e.message)
                )
            }
        }
    }
}
