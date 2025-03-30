package com.numad.aitranslator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numad.aitranslator.dao.GenericResponse
import com.numad.aitranslator.repositories.TranslatorRepository
import com.numad.aitranslator.room.entities.TranslationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val translatorRepository: TranslatorRepository
) : ViewModel() {
    private val _translations = MutableStateFlow<List<TranslationEntity>>(emptyList())
    val translations = _translations.asStateFlow()

    init {
        fetchTranslations()
    }

    fun fetchTranslations() {
        viewModelScope.launch {
            _translations.value = translatorRepository.fetchTranslations()
        }
    }

    fun deleteTranslation(id: Long, onCompletion: (GenericResponse) -> Unit) {
        viewModelScope.launch {
            val result = translatorRepository.deleteTranslation(id)
            when (result) {
                is GenericResponse.Success -> {
                    fetchTranslations()
                }

                is GenericResponse.Failure -> {
                    // Handle failure
                }
            }
            onCompletion(result)
        }
    }
}