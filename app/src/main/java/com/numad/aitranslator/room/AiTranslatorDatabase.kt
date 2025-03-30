package com.numad.aitranslator.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.numad.aitranslator.room.daos.TranslationDao
import com.numad.aitranslator.room.entities.TranslationEntity
import kotlin.concurrent.Volatile

@Database(
    entities = [
        TranslationEntity::class
    ],
    version = 1, exportSchema = false
)
abstract class AiTranslatorDatabase : RoomDatabase() {

    abstract fun getTranslationDao(): TranslationDao

    companion object {
        private const val DATABASE_NAME = "ai_translator_db"

        @Volatile
        private var instance: AiTranslatorDatabase? = null

        fun getInstance(context: Context): AiTranslatorDatabase {
            if (instance == null) {
                synchronized(AiTranslatorDatabase::class.java) {
                    if (instance == null) {
                        instance = databaseBuilder(
                            context.applicationContext,
                            AiTranslatorDatabase::class.java,
                            DATABASE_NAME
                        )
                            .addMigrations(*Migrations.MIGRATIONS)
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}
