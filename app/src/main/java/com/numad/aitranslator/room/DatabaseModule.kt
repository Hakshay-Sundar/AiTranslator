package com.numad.aitranslator.room

import android.content.Context
import com.numad.aitranslator.room.daos.TranslationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AiTranslatorDatabase {
        return AiTranslatorDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesTranslationDao(database: AiTranslatorDatabase): TranslationDao {
        return database.getTranslationDao()
    }
}
