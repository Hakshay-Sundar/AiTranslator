package com.numad.aitranslator.modules

import com.numad.aitranslator.repositories.TranslatorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTranslatorRepository(): TranslatorRepository {
        return TranslatorRepository()
    }
}
