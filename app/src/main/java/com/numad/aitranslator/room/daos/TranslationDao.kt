package com.numad.aitranslator.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.numad.aitranslator.room.entities.TranslationEntity

@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity) : Long

    @Query("UPDATE translations SET text = :text, translated_text = :translatedText, language_from = :languageFrom, language_to = :languageTo, timestamp_millis = :timestampMillis WHERE id = :id")
    suspend fun updateTranslation(
        id: Long,
        text: String,
        translatedText: String,
        languageFrom: String,
        languageTo: String,
        timestampMillis: Long
    )

    @Query("DELETE FROM translations WHERE id = :id")
    suspend fun deleteTranslation(id: Long)

    @Query("SELECT * FROM translations")
    suspend fun getAllTranslations(): List<TranslationEntity>

    @Query("SELECT * FROM translations WHERE id = :id LIMIT 1")
    suspend fun getTranslationById(id: Long): TranslationEntity?
}