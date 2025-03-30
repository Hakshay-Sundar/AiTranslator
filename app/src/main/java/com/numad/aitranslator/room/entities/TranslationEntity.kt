package com.numad.aitranslator.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "language_from")
    val languageFrom: String,

    @ColumnInfo(name = "language_to")
    val languageTo: String,

    @ColumnInfo(name = "translated_text")
    val translatedText: String,

    @ColumnInfo(name = "timestamp_millis")
    val timestampMillis: Long
)
