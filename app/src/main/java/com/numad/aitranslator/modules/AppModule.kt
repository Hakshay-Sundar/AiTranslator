package com.numad.aitranslator.modules

import android.content.Context
import com.numad.aitranslator.repositories.TranslatorRepository
import com.numad.aitranslator.room.daos.TranslationDao
import com.numad.aitranslator.utils.LanguageUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideTranslatorRepository(translationDao: TranslationDao): TranslatorRepository {
        return TranslatorRepository(translationDao = translationDao)
    }

    @Provides
    @Singleton
    fun provideLanguageUtils(): LanguageUtils {
        return LanguageUtils
    }
}
